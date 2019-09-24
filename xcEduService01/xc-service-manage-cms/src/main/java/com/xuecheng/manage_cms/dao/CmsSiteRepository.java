package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms.dao
 * @date 2019/7/17 11:38
 * @Description
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
}
