package com.xuecheng.manage_course.client;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_course.client
 * @date 2019/7/16 20:25
 * @Description
 */

/**
 * SpringCloud对Feign进行了增强兼容了SpringMVC的注解，我们在使用SpringMVC的注解时需要注意：
 * 1、feignClient接口有参数在参数必须加@PathVariable("XXX")和@RequestParam("XXX")
 * 2、feignClient返回值为复杂对象时其类型必须有无参构造函数。
 */
@FeignClient(value = XcServiceList.XC_SERVICE_MANAGE_CMS)
public interface CmsPageClient {
    //根据页面id获取页面内容
    @GetMapping("/cms/page/get/{id}")
    CmsPageResult findById(@PathVariable("id") String id);

    //添加页面，用于课程预览
    @PostMapping("/cms/page/save")
    CmsPageResult saveCmsPage(CmsPage cmsPage);

    //一键发布页面
    @PostMapping("/cms/page/postPageQuick")
    CmsPostPageResult postPageQuick(CmsPage cmsPage);

}

