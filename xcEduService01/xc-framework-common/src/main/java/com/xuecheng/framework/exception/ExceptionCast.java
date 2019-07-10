package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.framework.exception
 * @date 2019/7/9 22:52
 * @Description
 */
public class ExceptionCast {

    //使用此静态方法抛出自定义异常
    public static void cast(ResultCode resultCode){
            throw new CustomException(resultCode);
    }

}
