package com.xuecheng.manage_course.exception;

import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_course.exception
 * @date 2019/8/1 14:00
 * @Description
 */
@ControllerAdvice
public class CustomExceptionCatch extends ExceptionCatch {

    static {
        //定义异常类型所对应的错误代码
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }
}
