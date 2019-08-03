package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_course.dao
 * @date 2019/7/13 10:34
 * @Description
 */
@Mapper
public interface TeachplanMapper {
    /**
     * 采用自连接根据课程id查询课程计划(三级树形结构)
     * @param courseId
     * @return
     */
    TeachplanNode selectList(String courseId);

}
