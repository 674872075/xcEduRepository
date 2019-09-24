package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_course.dao
 * @date 2019/7/15 20:49
 * @Description
 */
public interface CoursePicRepository extends JpaRepository<CoursePic,String> {

    Long deleteByCourseid(String courseid);
}
