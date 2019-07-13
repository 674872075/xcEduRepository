package com.xuecheng.manage_cms_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms_client
 * @date 2019/7/12 17:48
 * @Description
 */

@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.cms")//扫描实体类
@ComponentScan(basePackages={"com.xuecheng.framework"})//扫描common下的所有类
@ComponentScan(basePackages={"com.xuecheng.manage_cms_client"})
public class ManageCmsClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageCmsClientApplication.class,args);
    }
}
