package com.xuecheng.manage_course.web.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_course.web.controller
 * @date 2019/7/13 10:47
 * @Description
 */
@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    @Autowired
    private CourseService courseService;

    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return courseService.selectList(courseId);
    }

    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult<CourseInfo> findCourseList(@PathVariable("page") int page,@PathVariable("size") int size, CourseListRequest courseListRequest) {
        return courseService.findCourseList(page,size,courseListRequest);
    }

    @PostMapping("/coursebase/add")
    public ResponseResult saveCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.saveCourseBase(courseBase);
    }

    @GetMapping("/manage/baseinfo/{courseid}")
    public CourseBase getCourseBaseById(@PathVariable("courseid") String courseId) {
        return courseService.findCourseByCourseId(courseId);
    }

    @PostMapping("/coursebase/update")
    public ResponseResult updateCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.saveCourseBase(courseBase);
    }

    @GetMapping("/manage/marketinfo/{courseid}")
    public CourseMarket getCourseMarketById(@PathVariable("courseid") String courseId) {
        return courseService.getCourseMarketById(courseId);
    }

    @PostMapping("/marketinfo/save")
    public ResponseResult updateCourseMarket(@RequestBody CourseMarket courseMarket) {
        return courseService.saveCourseMarket(courseMarket);
    }

}
