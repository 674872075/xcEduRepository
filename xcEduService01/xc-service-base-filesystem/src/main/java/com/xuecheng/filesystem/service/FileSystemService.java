package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.filesystem.service
 * @date 2019/7/15 19:31
 * @Description
 */

@Service
public class FileSystemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemService.class);

    @Value("${xuecheng.fastdfs.tracker_servers}")
    String tracker_servers;
    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    int network_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.charset}")
    String charset;

    @Autowired
    FileSystemRepository fileSystemRepository;

    /**
     * 上传文件到fdfs，返回文件id
     *
     * @param file
     * @return
     */
    private String fdfs_upload(MultipartFile file) {
        //初始化文件配置
        initFdfsConfig();
        //创建tracker client
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = null;
        try {
            //获取trackerServer
            trackerServer = trackerClient.getConnection();
            //获取storage
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            //创建storage client
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storeStorage);
            //获取文件字节
            byte[] bytes = file.getBytes();
            //文件原始名称
            String originalFilename = file.getOriginalFilename();
            //文件扩展名
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            //上传文件，返回文件id
            String file_id = storageClient1.upload_file1(bytes, ext, null);
            return file_id;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 初始化fastdfs配置
     */
    private void initFdfsConfig() {
        try {
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
            ClientGlobal.setG_charset(charset);
        } catch (Exception e) {
            e.printStackTrace();
            //初始化文件系统出错
            ExceptionCast.cast(FileSystemCode.FS_INITFDFSERROR);
        }
    }

    /**
     * 上传文件
     * @param multipartFile
     * 文件流
     * @param filetag
     * 文件标签
     * @param businesskey
     * 业务标识
     * @param metadata
     * 文件相关的元数据
     * @return
     */
    public UploadFileResult upload(MultipartFile multipartFile,
                            String filetag,
                            String businesskey,
                            String metadata){
        //上传文件为空
        if(multipartFile==null){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        //上传文件到fdfs，返回文件id
        String file_id = this.fdfs_upload(multipartFile);
        //创建文件信息对象
        FileSystem fileSystem=new FileSystem();
        //文件id
        fileSystem.setFileId(file_id);
        //文件在文件系统中的路径
        fileSystem.setFilePath(file_id);
        //业务标识
        fileSystem.setBusinesskey(businesskey);
        //文件名称
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        //文件大小
        fileSystem.setFileSize(multipartFile.getSize());
        //文件类型
        fileSystem.setFileType(multipartFile.getContentType());
        //业务标签
        fileSystem.setFiletag(filetag);
        //元数据不为空
        if(StringUtils.isNotEmpty(metadata)){
            //文件元信息
            fileSystem.setMetadata(JSON.parseObject(metadata, Map.class));
        }
        fileSystemRepository.save(fileSystem);

        return new UploadFileResult(CommonCode.SUCCESS,fileSystem);
    }

}
