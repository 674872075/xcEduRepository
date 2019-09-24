package com.xuecheng.manage_cms.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms.config
 * @date 2019/7/10 16:34
 * @Description
 */

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.database}")
    private String database;


    /**
     * MongoClient 是mongodb客户端 用于连接mongodb数据库 由spring容器创建
     */
    @Bean
    public GridFSBucket getGridFSBucket(MongoClient mongoClient){
        MongoDatabase mongoDatabase = mongoClient.getDatabase(this.database);
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoDatabase);
        return  gridFSBucket;
    }
}
