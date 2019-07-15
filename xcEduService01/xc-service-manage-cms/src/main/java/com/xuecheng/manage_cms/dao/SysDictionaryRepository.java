package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms.dao
 * @date 2019/7/14 14:58
 * @Description
 */
public interface SysDictionaryRepository extends MongoRepository<SysDictionary,String> {

    /**
     * 根据type查询数据字典信息
     * @param type
     * @return
     */
    SysDictionary findByDType(String type);
}
