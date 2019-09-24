package com.xuecheng.framework.domain.cms.response;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.framework.domain.cms.response
 * @date 2019/7/17 11:35
 * @Description
 */

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor//无参构造器注解
public class CmsPostPageResult extends ResponseResult {
    String pageUrl; //页面发布url

    public CmsPostPageResult(ResultCode resultCode, String pageUrl) {
        super(resultCode);
        this.pageUrl = pageUrl;
    }
}

