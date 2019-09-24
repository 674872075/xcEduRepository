package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.filesystem.dao
 * @date 2019/7/15 19:15
 * @Description
 */
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {
}
