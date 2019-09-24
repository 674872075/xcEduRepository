package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.manage_cms.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms.web.controller
 * @date 2019/7/10 14:40
 * @Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CmsConfigControllerTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PageService pageService;

    @Test
    public void testRestTemplate(){
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        Map map = responseEntity.getBody();
        System.out.println(map);
    }

    @Test
    public void testTemplate(){
        String pageHtml = pageService.getPageHtml("5a795ac7dd573c04508f3a56");
        System.out.println(pageHtml);
    }

    class MyRun<T> implements Runnable{
        @Override
        public void run() {

        }
    }

    /*@Test
    public void test(){
       Runnable r= new Runnable(){
            @Override
            public void run() {

            }
        };
       Runnable r1=new MyRun<CmsConfigControllerTest>();
        Class<?>[] interfaces = r1.getClass().getInterfaces();
        Type[] genericInterfaces = r1.getClass().getGenericInterfaces();
        System.out.println(interfaces[0]);
        System.out.println(genericInterfaces[0]);
        //System.out.println(r.getClass().getGenericInterfaces().toString());
    }*/

}
