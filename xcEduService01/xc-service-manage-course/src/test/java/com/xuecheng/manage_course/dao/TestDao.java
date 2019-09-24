package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private CourseMapper courseMapper;

    @Test
    @Transactional  //在junit测试时，@Transactional注解默认不提交事务，即默认回滚事务，防止污染数据库
    @Rollback(false)//关闭回滚
    public void testCourseBaseRepository(){
        Optional<CourseBase> optional = courseBaseRepository.findById("402885816240d276016240f7e5000002");
        CourseBase courseBase=null;
        if(optional.isPresent()){
            courseBase = optional.get();
            System.out.println(courseBase);
        }

        courseBase.setName("test2");
        courseBaseRepository.save(courseBase);
        Optional<CourseBase> optiona2 = courseBaseRepository.findById("402885816240d276016240f7e5000002");
        if(optiona2.isPresent()){
            CourseBase courseBase1 = optional.get();
            System.out.println(courseBase1);
        }
    }

    @Test
    public void testCourseMapper(){
        CourseBase courseBase = courseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
        System.out.println(courseBase);
    }

    @Test
    public  void testPage(){
        //查询第一页，每页显示10条记录
        PageHelper.startPage(1,10);
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(new CourseListRequest());
        courseListPage.forEach(System.out::println);
    }

}
