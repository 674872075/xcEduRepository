package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseMarket;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_course.dao
 * @date 2019/7/15 0:41
 * @Description
 */
public interface CourseMarketRepository extends JpaRepository<CourseMarket,String> {
}
