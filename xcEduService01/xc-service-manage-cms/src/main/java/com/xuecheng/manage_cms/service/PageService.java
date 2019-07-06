package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public QueryResponseResult findList( int page,  int size, QueryPageRequest queryPageRequest) {

        if(queryPageRequest==null){
            queryPageRequest=new QueryPageRequest();
        }
        if(page<=0){
            page=1;
        }
        page=page-1;

        if(size<=0){
            size=20;
        }
        /*获取总页数
        page.getTotalPages();
        获取总元素个数
        page.getTotalElements();
        获取该分页的列表
        page.getContent();*/
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> cmsPages = cmsPageRepository.findAll(pageable);
        QueryResult<CmsPage> queryResult = new QueryResult<>();
        queryResult.setList(cmsPages.getContent());
        queryResult.setTotal(cmsPages.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }
}
