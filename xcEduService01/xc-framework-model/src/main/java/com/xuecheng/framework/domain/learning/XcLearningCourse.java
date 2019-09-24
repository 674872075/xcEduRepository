package com.xuecheng.framework.domain.learning;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by admin on 2018/2/10.
 */
@Data
@ToString
@Entity
@Table(name="xc_learning_course")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class XcLearningCourse implements Serializable {
    private static final long serialVersionUID = -916357210051789799L;
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;
    @Column(name = "course_id")
    private String courseId;
    @Column(name = "user_id")
    private String userId;
    private String valid;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    private String status;

    /*
    *  "d_value" : [
        {
            "sd_name" : "正常",
            "sd_id" : "501001",
            "sd_status" : "1"
        },
        {
            "sd_name" : "结束",
            "sd_id" : "501002",
            "sd_status" : "1"
        },
        {
            "sd_name" : "取消",
            "sd_id" : "501003",
            "sd_status" : "1"
        },
        {
            "sd_name" : "未选课",
            "sd_id" : "501004",
            "sd_status" : "1"
        }
    ]
    * */

}
