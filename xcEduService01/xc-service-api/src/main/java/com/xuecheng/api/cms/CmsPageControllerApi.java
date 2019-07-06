package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.api.cms
 * @date 2019/7/6 10:19
 * @Description
 * 1、接口集中管理
 * 2、Api工程的接口将作为各微服务远程调用使用
 * 3.此接口编写后会在CMS服务工程编写Controller类实现此接口
 */
public interface CmsPageControllerApi {
    /**
     * 页面查询
     * @param page 第几页
     * @param size 页面记录数
     * @param queryPageRequest 查询条件
     * @return
     * 查询结果集
     */
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

}
