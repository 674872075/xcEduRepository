package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
}
