package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_course.service
 * @date 2019/7/14 12:48
 * @Description
 */

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public CategoryNode findList(){
        return  categoryMapper.findList();
    }
}
