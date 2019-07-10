package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms.dao
 * @date 2019/7/10 14:01
 * @Description
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {

}
