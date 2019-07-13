package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms_client.service
 * @date 2019/7/12 20:02
 * @Description
 */

@Service
public class PageService {

    private  static  final Logger LOGGER= LoggerFactory.getLogger(PageService.class);
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;

    /**
     * 保存页面到服务器物理路径
     * @param pageId
     */
    public void savePageToServerPath(String pageId) {
        //根据页面id获取页面信息
        CmsPage cmsPage =this.getCmsPage(pageId);
        //根据站点id获取站点信息
        CmsSite cmsSite =this.getCmsSite(cmsPage.getSiteId());
        //获取页面物理路径=站点物理路径+页面物理路径+页面名字
        String  pagePath = cmsSite.getSitePhysicalPath()+ cmsPage.getPagePhysicalPath() +  cmsPage.getPageName();
        //GridFS查询静态文件内容
        InputStream inputStream=null;
        FileOutputStream fileOutputStream = null;
        try {
            inputStream = getFileById(cmsPage.getHtmlFileId());
            //将静态文件内容保存到页面物理路径
            if(inputStream ==null){
                LOGGER.error("getFileById inputStream is null,HtmlFileId:",cmsPage.getHtmlFileId());
                ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
            }
            fileOutputStream = new FileOutputStream(new File(pagePath));
            //将文件内容保存到服务物理路径
            IOUtils.copy(inputStream,fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据文件id获取文件内容
     * @param fileId
     * @return
     * @throws IOException
     */
    public InputStream getFileById(String fileId) throws IOException {
        //根据文件id取出fs.files内容
        GridFSFile gridFSFile =
                gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        //打开下载流对象
        GridFSDownloadStream gridFSDownloadStream =
                gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建GridFsResource
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        return  gridFsResource.getInputStream();
    }

    /**
     * 根据站点id获取站点信息
     * @param siteId
     * 站点id
     * @return
     */
    public CmsSite getCmsSite(String siteId) {
        Optional<CmsSite> cmsSiteOptional = cmsSiteRepository.findById(siteId);

        if(! cmsSiteOptional.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_PAGE_SITE_NOTEXISTS);
        }
        CmsSite cmsSite = cmsSiteOptional.get();
        return cmsSite;
    }

    /**
     * 根据页面id获取页面信息
     * @param pageId
     * 页面id
     * @return
     */
    public CmsPage getCmsPage(String pageId) {
        Optional<CmsPage> cmsPageOptional = cmsPageRepository.findById(pageId);
        //页面不存在
        if (!cmsPageOptional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        return cmsPageOptional.get();
    }
}
