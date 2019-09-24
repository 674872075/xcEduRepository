package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms.service
 * @date 2019/7/10 14:05
 * @Description
 */

@Service
public class CmsConfigService {

    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    /**
     根据id查询配置管理信息
     */
    public CmsConfig getModel(String id){
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        //判断Optional是否有元素
        if(optional.isPresent()){
            CmsConfig cmsConfig = optional.get();
            return cmsConfig;
        }
        return null;
    }
}
