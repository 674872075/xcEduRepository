package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.api.cms
 * @date 2019/7/13 10:07
 * @Description
 */
@Api(value = "课程管理接口", description = "课程管理接口，对课程进行增删改查操作")
public interface CourseControllerApi {

    @ApiOperation("课程计划查询")
    @ApiImplicitParam(name = "courseId", value = "课程主键id"
            , required = true, paramType = "path", dataType = "String")
    TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    public ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("查询我的课程列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value = "页码"
                    ,required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value = "每页记录数",
                    required=true,paramType="path",dataType="int")
    })
    QueryResponseResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest);

    @ApiOperation("新增课程")
    ResponseResult saveCourseBase(CourseBase courseBase);
    
    @ApiOperation("获取课程基础信息")
    /*@ApiImplicitParam(name = "courseId", value = "课程id"
            , required = true, paramType = "path", dataType = "String")*/
    public CourseBase getCourseBaseById(String courseId);
    
    @ApiOperation("更新课程基础信息")
    public ResponseResult updateCourseBase(CourseBase courseBase);

    @ApiOperation("获取课程营销信息")
    public CourseMarket getCourseMarketById(String courseId);

    @ApiOperation("更新课程营销信息")
    public ResponseResult updateCourseMarket(CourseMarket courseMarket);

}
