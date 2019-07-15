package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.api.cms
 * @date 2019/7/14 14:55
 * @Description
 */

@Api(value = "数据字典接口",description = "提供数据字典接口的管理、查询功能")
public interface SysDicthinaryControllerApi {
    //数据字典
    @ApiOperation(value="数据字典查询接口")
    public SysDictionary getByType(String type);
}

