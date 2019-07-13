package com.xuecheng.manage_cms_client.dao;
import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms_client.dao
 * @date 2019/7/12 19:59
 * @Description
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {

}
