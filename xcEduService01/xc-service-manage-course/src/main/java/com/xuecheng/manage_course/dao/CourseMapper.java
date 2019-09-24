package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by Administrator.
 */
@Mapper
public interface CourseMapper {
   /**
    * 查询课程基本信息
   * @param id
    * 课程id
    * @return
    */
   CourseBase findCourseBaseById(String id);

   /**
    *分页查询课程信息(course_base与course_pic关联，把课程图片也查询出来)
    * @param courseListRequest
    * 查询条件
    * @return
    */
   Page<CourseInfo> findCourseListPage(CourseListRequest courseListRequest);
}
