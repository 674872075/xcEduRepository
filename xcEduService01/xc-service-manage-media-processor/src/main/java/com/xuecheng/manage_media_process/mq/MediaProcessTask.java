package com.xuecheng.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.MediaFileProcess_m3u8;
import com.xuecheng.framework.utils.HlsVideoUtil;
import com.xuecheng.framework.utils.Mp4VideoUtil;
import com.xuecheng.manage_media_process.dao.MediaFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_media_process.mq
 * @date 2019/7/25 9:43
 * @Description
 */
@Component
public class MediaProcessTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaProcessTask.class);


    //ffmpeg绝对路径
    @Value("${xc-service-manage-media.ffmpeg-path}")
    private String ffmpeg_path;

    //上传文件根目录
    @Value("${xc-service-manage-media.video-location}")
    private String serverPath;

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @RabbitListener(queues = {"${xc-service-manage-media.mq.queue-media-video-processor}"},containerFactory = "customContainerFactory")
    public void receiveMediaProcessTask(String msg, Message message, Channel channel) {
        //解析消息,解析json｛“mediaId”:XXX｝
        Map msgMap = JSON.parseObject(msg, Map.class);
        LOGGER.info("receive media process task msg :{} ", msgMap);
        //获取媒资文件id
        String mediaId = (String) msgMap.get("mediaId");
        //根据mediaId去mongodb数据库查询媒资文件信息
        Optional<MediaFile> mediaFileOptional = mediaFileRepository.findById(mediaId);
        //不存在说明文件上传没有成功，放弃处理，直接返回
        if (!mediaFileOptional.isPresent()) {
            return;
        }

        //文件信息存在则获取文件信息
        MediaFile mediaFile = mediaFileOptional.get();
        //获取文件类型(文件扩展名)
        String fileType = mediaFile.getFileType();
        //判断是不是avi类型，暂时只处理avi类型的视频
        //不是avi类型 更新处理状态为303004(无需处理) 持久化到数据库 然后直接返回
        if (!"avi".equals(fileType)) {
            //303004 无需处理
            mediaFile.setProcessStatus("303004");
            mediaFileRepository.save(mediaFile);
            return;
        }

        //如果代码执行到这，说明文件类型为avi,需要处理
        //更新处理状态为303001(处理中)，持久化到数据库
        mediaFile.setProcessStatus("303001");
        mediaFileRepository.save(mediaFile);

        //3、使用工具类将avi文件生成mp4
        //String ffmpeg_path, String video_path, String mp4_name, String mp4folder_path
        //要处理的视频文件(avi)的路径 video_path
        String video_path=serverPath+mediaFile.getFilePath()+mediaFile.getFileName();
        //生成的mp4的文件名称
        String mp4_name = mediaFile.getFileId() + ".mp4";
        //生成的mp4所在的路径
        String mp4folder_path = serverPath + mediaFile.getFilePath();
        //创建工具类对象
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path,video_path,mp4_name,mp4folder_path);
        //进行处理
        String result = mp4VideoUtil.generateMp4();
        if(result==null || ! "success".equals(result)){
            //处理失败
            //更新处理状态为303003（处理失败）
            mediaFile.setProcessStatus("303003");
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            //设置错误信息
            mediaFileProcess_m3u8.setErrormsg(result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            //持久化到数据库
            mediaFileRepository.save(mediaFile);
            return;
        }

        //4、将mp4生成m3u8和ts文件
        //String ffmpeg_path, String video_path, String m3u8_name,String m3u8folder_path
        //mp4视频文件路径
        String mp4_video_path =mp4folder_path+mp4_name;
        //m3u8_name文件名称
        String m3u8_name = mediaFile.getFileId() + ".m3u8";
        //m3u8文件所在目录
        String m3u8folder_path = serverPath + mediaFile.getFilePath() + "hls/";
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpeg_path,mp4_video_path,m3u8_name,m3u8folder_path);
        //生成m3u8和ts文件
        String tsResult = hlsVideoUtil.generateM3u8();
        if(result==null || !"success".equals(tsResult)){
            //处理失败
            mediaFile.setProcessStatus("303003");
            //定义mediaFileProcess_m3u8
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            //记录失败原因
            mediaFileProcess_m3u8.setErrormsg(result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            return ;
        }

        //处理成功
        //获取ts文件列表
        List<String> ts_list = hlsVideoUtil.get_ts_list();
        //更新处理状态为303002(处理成功)
        mediaFile.setProcessStatus("303002");
        //定义mediaFileProcess_m3u8
        MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
        mediaFileProcess_m3u8.setTslist(ts_list);
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);

        //保存fileUrl（此url就是视频播放的相对路径）
        String fileUrl =mediaFile.getFilePath() + "hls/"+m3u8_name;
        mediaFile.setFileUrl(fileUrl);
        mediaFileRepository.save(mediaFile);
    }
}
