package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.api.cms.CmsConfigControllerApi;
import com.xuecheng.manage_cms.service.CmsConfigService;
import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms.web.controller
 * @date 2019/7/10 14:10
 * @Description
 */
@RestController
@RequestMapping("/cms/config")
public class CmsConfigController implements CmsConfigControllerApi {

    @Autowired
    private CmsConfigService cmsConfigService;

    @Override
    @GetMapping("/getmodel/{id}")
    public CmsConfig getModel(@PathVariable("id") String id) {
        return cmsConfigService.getModel(id);
    }
}
