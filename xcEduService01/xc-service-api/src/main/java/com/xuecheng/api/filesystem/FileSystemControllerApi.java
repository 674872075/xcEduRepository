package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.api.filesystem
 * @date 2019/7/15 17:46
 * @Description
 */
@Api(value = "文件系统服务",description = "对文件进行增删改查管理")
public interface FileSystemControllerApi {

    /**
     * 上传文件
     * @param multipartFile 文件
     * @param filetag 文件标签
     * @param businesskey 业务key
     * @param metedata 元信息,json格式
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name="multipartFile",value = "文件"
                    ,required=true,paramType="form",dataType="File"),
            @ApiImplicitParam(name="filetag",value = "文件标签"
                    ,paramType="query",dataType="String"),
            @ApiImplicitParam(name="businesskey",value = "业务标识",
                    paramType="query",dataType="String"),
            @ApiImplicitParam(name="metadata",value = "文件相关的元信息",
                    paramType="query",dataType="String")
    })
    UploadFileResult upload(MultipartFile multipartFile,
                    String filetag,
                    String businesskey,
                    String metadata);

}
