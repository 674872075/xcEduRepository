package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.PageService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms.web.controller
 * @date 2019/7/10 21:10
 * @Description
 */
@Controller
@Api(value = "页面预览",description = "页面预览")
public class CmsPagePreviewController extends BaseController {

    @Autowired
    private PageService pageService;

    @GetMapping("/cms/preview/{pageId}")
    public void preview(@PathVariable("pageId") String pageId){
        String pageHtml = pageService.getPageHtml(pageId);
        if(StringUtils.isNotEmpty(pageHtml)){
            try {
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(pageHtml.getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
