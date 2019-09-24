package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.api.cms
 * @date 2019/7/10 13:58
 * @Description
 */
@Api(value="cms配置管理接口",description = "cms配置管理接口，提供数据模型的管理、查询接口")
public interface CmsConfigControllerApi {

    @ApiOperation("根据id查询CMS配置信息")
    @ApiImplicitParam(name="id",value = "页面主键id"
            ,required=true,paramType="path",dataType="String")
    public CmsConfig getModel(String id);

}
