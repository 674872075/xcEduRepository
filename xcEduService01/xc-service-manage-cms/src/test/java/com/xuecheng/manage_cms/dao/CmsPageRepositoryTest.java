package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms.dao
 * @date 2019/7/6 16:52
 * @Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Test
    public void testFindPage(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<CmsPage> cmsPages = cmsPageRepository.findAll(pageable);
        cmsPages.forEach(p-> System.out.println(p));
    }

    //自定义条件查询
    @Test
    public void testFindAll(){
        //ExampleMatcher.GenericPropertyMatchers.contains() 包含
        //ExampleMatcher.GenericPropertyMatchers.startsWith()//开头匹配
        //条件匹配器
        ExampleMatcher matching = ExampleMatcher.matching()
                //模糊匹配
        .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage=new CmsPage();
        //条件值
        cmsPage.setPageAliase("轮播");
        Example<CmsPage> example = Example.of(cmsPage, matching);
        Pageable pageable = PageRequest.of(0, 10);
        Page<CmsPage> pages = cmsPageRepository.findAll(example, pageable);
        pages.forEach(System.out::println);
    }
}
