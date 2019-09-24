package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.framework.domain.course.ext
 * @date 2019/7/16 21:57
 * @Description
 */
@Data
@NoArgsConstructor
@ToString
public class CourseView implements Serializable {

    CourseBase courseBase;//基础信息
    CourseMarket courseMarket;//课程营销
    CoursePic coursePic;//课程图片
    TeachplanNode TeachplanNode;//教学计划

}
