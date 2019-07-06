package com.xuecheng.framework.domain.cms.request;

import com.xuecheng.framework.model.request.RequestData;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.framework.domain.cms.request
 * @date 2019/7/6 10:07
 * @Description 作为查询条件类型
 */

public class QueryPageRequest extends RequestData {
    //站点id
    private String siteId;
    //页面ID
    private String pageId;
    //页面名称
    private String pageName;
    //别名
    private String pageAliase;
    //模版id
    private String templateId;
}
