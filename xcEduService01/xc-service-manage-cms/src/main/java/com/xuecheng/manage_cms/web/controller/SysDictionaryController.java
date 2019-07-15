package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.api.cms.SysDicthinaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.service.SysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_course.web.controller
 * @date 2019/7/14 15:05
 * @Description
 */
@RestController
@RequestMapping("/sys/dictionary")
public class SysDictionaryController implements SysDicthinaryControllerApi {

    @Autowired
    private SysDictionaryService sysDictionaryService;

    @Override
    @GetMapping("/get/{type}")
    public SysDictionary getByType(@PathVariable("type") String type) {
        return sysDictionaryService.getByType(type);
    }
}
