package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.dao.SysDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms.service
 * @date 2019/7/14 15:01
 * @Description
 */
@Service
public class SysDictionaryService {

    @Autowired
    private SysDictionaryRepository sysDictionaryRepository;

    public SysDictionary getByType(String type){
        return sysDictionaryRepository.findByDType(type);
    }
}
