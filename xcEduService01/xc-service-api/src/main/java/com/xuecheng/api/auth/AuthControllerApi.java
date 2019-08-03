package com.xuecheng.api.auth;

import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.api.auth
 * @date 2019/7/30 16:41
 * @Description
 */
@Api(value = "用户认证", description = "用户认证接口")
public interface AuthControllerApi {

    @ApiOperation("登录")
    /**
     * 登录功能
     */
    LoginResult login(LoginRequest loginRequest);

    @ApiOperation("退出")
    /**
     * 退出功能
     */
    ResponseResult logout();

    @ApiOperation("查询userjwt令牌")
    JwtResult   userjwt();

}
