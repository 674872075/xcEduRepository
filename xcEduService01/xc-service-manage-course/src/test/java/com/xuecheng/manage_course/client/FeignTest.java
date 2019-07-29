package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms.client
 * @date 2019/7/16 20:28
 * @Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FeignTest {

    @Autowired
    private CmsPageClient cmsPageClient;

    @Test
    public void findCmgPageById(){
        CmsPageResult cmsPage = cmsPageClient.findById("5a795ac7dd573c04508f3a56");
        System.out.println(cmsPage);
    }
}
