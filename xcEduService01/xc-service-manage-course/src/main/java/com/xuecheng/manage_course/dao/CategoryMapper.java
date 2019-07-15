package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_course.dao
 * @date 2019/7/14 12:48
 * @Description
 */
@Mapper
public interface CategoryMapper {
     /**
      * 查找课程分类(三级节点)
      * @return
      */
     CategoryNode findList();
}
