package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms.dao
 * @date 2019/7/10 19:11
 * @Description
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {
}
