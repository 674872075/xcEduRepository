package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms.service
 * @date 2019/7/6 20:47
 * @Description
 */

@Service
public class PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    //GridFsTemplate在SpringBoot工程下可以直接@Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
       /*获取总页数
        page.getTotalPages();
        获取总元素个数
        page.getTotalElements();
        获取该分页的列表
        page.getContent();*/
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        //自定义条件查询
        //定义条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        //条件值对象
        CmsPage cmsPage = new CmsPage();
        //设置条件值（站点id）
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //设置模板id作为查询条件
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //设置页面别名作为查询条件
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        //定义条件对象Example
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        //分页参数
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);//实现自定义条件查询并且分页查询
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    public CmsPageResult add(CmsPage cmsPage) {

        //校验页面是否存在 根据页面名称、站点Id、页面webpath查询
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());

        if (cmsPage1 != null) {
            //页面已存在
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPage.setPageId(null);
        cmsPageRepository.save(cmsPage);
        CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        return cmsPageResult;
    }

    public CmsPageResult findById(String id) {
        Optional<CmsPage> cmsPages = cmsPageRepository.findById(id);
        if (cmsPages.isPresent()) {
            return new CmsPageResult(CommonCode.SUCCESS, cmsPages.get());
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    public CmsPageResult update(String id, CmsPage cmsPage) {
        //根据id查询页面信息
        CmsPageResult cmsPageResult = this.findById(id);
        //如果查询到信息,证明信息存在可以进行修改
        if (cmsPageResult.getCode() == 10000) {
            cmsPage.setPageId(cmsPageResult.getCmsPage().getPageId());
            CmsPage save = cmsPageRepository.save(cmsPage);
            if (save != null) {
                return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
            }
        }
        //记录不存在，不能修改
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    public ResponseResult deleteById(String id) {
        CmsPage cmsPage = this.findById(id).getCmsPage();
        if (cmsPage != null) {
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 页面静态化
     *
     * @param pageId
     * @return
     */
    public String getPageHtml(String pageId) {
        //获取页面模型数据
        Map model = this.getModelByPageId(pageId);

        //获取页面模型数据为空
        if (model == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }

        //获取页面模板
        String templateContent = this.getTemplateByPageId(pageId);

        //isNotEmpty不去除空格
        //页面模板为空  抛出异常后下面的代码将不再执行
        if (StringUtils.isEmpty(templateContent)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        //执行页面静态化
        String html = generateHtml(templateContent, model);

        if (StringUtils.isEmpty(html)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }

        return html;
    }


    //执行页面静态化
    private String generateHtml(String templateContent, Map model) {
        try {
            //生成配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            //模板加载器
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("template", templateContent);
            //配置模板加载器
            configuration.setTemplateLoader(stringTemplateLoader);
            //获取模板
            Template template1 = configuration.getTemplate("template");
            //模板+数据 页面静态化
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template1, model);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取页面模板
     *
     * @param pageId 页面id
     * @return
     */
    private String getTemplateByPageId(String pageId) {

        CmsPageResult cmsPageResult = this.findById(pageId);
        //页面不存在
        if (cmsPageResult.getCode() != 10000) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        String templateId = cmsPageResult.getCmsPage().getTemplateId();
        //模板不存在
        if (StringUtils.isEmpty(templateId)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);

        if (optional.isPresent()) {
            CmsTemplate cmsTemplate = optional.get();
            //获得模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            //取出模板文件内容
            GridFSFile gridFSFile =
                    gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开下载流对象
            GridFSDownloadStream gridFSDownloadStream =
                    gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建GridFsResource
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);

            try {
                String templateContent = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return templateContent;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    /**
     * 获取页面模型数据
     *
     * @param pageId 页面id
     * @return
     */
    private Map getModelByPageId(String pageId) {

        CmsPageResult cmsPageResult = this.findById(pageId);
        //页面不存在
        if (cmsPageResult.getCode() != 10000) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        String dataUrl = cmsPageResult.getCmsPage().getDataUrl();

        ResponseEntity<Map> responseEntity = restTemplate.getForEntity(dataUrl, Map.class);

        Map body = responseEntity.getBody();

        return body;
    }

}
