package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.domain.cms.CmsPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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

@Api(value="cms页面管理接口",description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {

    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value = "页码"
                    ,required=true,paramType="path",dataType="int"),
                    @ApiImplicitParam(name="size",value = "每页记录数",
                            required=true,paramType="path",dataType="int")
                    })
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    @ApiOperation("添加页面")
    CmsPageResult add(CmsPage cmsPage);

    @ApiOperation("通过id查询页面")
    @ApiImplicitParam(name="id",value = "页面主键id"
            ,required=true,paramType="path",dataType="String")
    CmsPageResult findById(String id);

    @ApiOperation("修改页面")
    @ApiImplicitParam(name="id",value = "页面主键id"
            ,required=true,paramType="path",dataType="String")
    CmsPageResult edit(String id,CmsPage cmsPage);

    @ApiOperation("通过id删除页面")
    @ApiImplicitParam(name="id",value = "页面主键id"
            ,required=true,paramType="path",dataType="String")
    ResponseResult delete(String id);
    
    @ApiOperation("发布页面")
    public ResponseResult post(String pageId);


    @ApiOperation("保存页面")
    public CmsPageResult save(CmsPage cmsPage);

    @ApiOperation("一键发布页面")
    public CmsPostPageResult postPageQuick(CmsPage cmsPage);


}
