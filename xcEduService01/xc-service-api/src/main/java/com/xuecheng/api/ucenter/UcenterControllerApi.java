package com.xuecheng.api.ucenter;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.api.ucenter
 * @date 2019/7/31 11:42
 * @Description
 */
@Api(value = "用户中心",description = "用户中心管理")
public interface UcenterControllerApi {

    @ApiOperation("根据用户名获取用户信息!")
    @ApiImplicitParam(name="username",value = "用户名"
            ,required=true,paramType="query",dataType="String")
    XcUserExt getUserExt(String username);
}
