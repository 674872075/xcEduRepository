package com.xuecheng.framework.domain.learning.respones;

import com.xuecheng.framework.model.response.ResultCode;
import lombok.ToString;


@ToString
public enum LearningCode implements ResultCode {
    LEARNING_GETMEDIA_ERROR(false,23001,"获取学习地址失败！"),
    CHOOSECOURSE_USERISNULl(false,23002,"选课的用户不存在！"),
    CHOOSECOURSE_TASKISNULL(false,23003,"选课任务不存在！");
    //操作代码
    private boolean success;
    //操作代码
    private int code;
    //提示信息
    private String message;

    private LearningCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
