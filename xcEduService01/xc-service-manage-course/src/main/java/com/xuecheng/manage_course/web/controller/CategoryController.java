package com.xuecheng.manage_course.web.controller;

import com.xuecheng.api.course.CategoryControllerApi;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_course.web.controller
 * @date 2019/7/14 12:46
 * @Description
 */

@RestController
@RequestMapping("/category")
public class CategoryController implements CategoryControllerApi {

    @Autowired
    private CategoryService categoryService;

    @Override
    @GetMapping("/list")
    public CategoryNode findList() {
        return categoryService.findList();
    }

   /* @GetMapping(value = "/list",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String List() {
        return "hello";
    }*/
}
