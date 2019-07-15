package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.api.course
 * @date 2019/7/14 12:15
 * @Description
 */

//@Api(value = "课程分类管理",description = "课程分类管理",tags = {"课程分类管理"})
@Api(value = "课程分类管理",description = "课程分类管理")
public interface CategoryControllerApi {

    @ApiOperation("查询分类")
    CategoryNode findList();

}
