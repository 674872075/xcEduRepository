package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_course.dao
 * @date 2019/7/13 14:45
 * @Description
 */
public interface TeachplanRepository extends JpaRepository<Teachplan,String> {
    /**
     定义方法根据课程id和父结点id查询出结点列表，可以使用此方法实现查询根结点
     */
   List<Teachplan> findByCourseidAndParentid(String courseId, String parentId);
}
