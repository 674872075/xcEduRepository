package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_course.service
 * @date 2019/7/13 10:40
 * @Description
 */
@Service
public class CourseService {

    @Autowired
    private TeachplanMapper teachplanMapper;
    @Autowired
    private TeachplanRepository teachplanRepository;
    @Autowired
    private  CourseBaseRepository courseBaseRepository;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CourseMarketRepository courseMarketRepository;

    public TeachplanNode selectList(String courseId) {
        return teachplanMapper.selectList(courseId);
    }

    //添加课程计划
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        //校验课程id和课程计划名称
        if(teachplan==null || StringUtils.isEmpty(teachplan.getCourseid())
                || StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }
        //取出父节点
        String parentid = teachplan.getParentid();
        /**
         *  1.isEmpty 没有忽略空格参数，是以是否为空和是否存在为判断依据。
         *  2.isBlank 是在 isEmpty 的基础上进行了为空（字符串都为空格、制表符、tab 的情况）的判断。（一般更为常用）
         */
        if(StringUtils.isBlank(parentid)){
            //如果父节点为空则取出根节点
            parentid=getTeachplanRoot(teachplan.getCourseid());
        }

        //取出父节点信息
        Optional<Teachplan> teachplanOptional = teachplanRepository.findById(parentid);
        if(! teachplanOptional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }
        //父节点
        Teachplan parentTeachplan = teachplanOptional.get();
        //父节点级别
        String parentGrade = parentTeachplan.getGrade();
        //设置父节点
        teachplan.setParentid(parentid);
        //未发布
        teachplan.setStatus("0");
        //子节点的级别，根据父节点来判断
        if(parentGrade.equals("1")){
            teachplan.setGrade("2");
        }else if(parentGrade.equals("2")){
            teachplan.setGrade("3");
        }
        //设置课程id
        teachplan.setCourseid(parentTeachplan.getCourseid());
        //持久化到数据库
        teachplanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 获取课程根结点，如果没有则添加根结点
     * @param courseid
     * 课程id
     * @return
     */
    @Transactional
    public String getTeachplanRoot(String courseid) {
        //校验课程id
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseid);
        if(! courseBaseOptional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }
        CourseBase courseBase = courseBaseOptional.get();
        //取出课程计划根结点
        List<Teachplan> teachplans = teachplanRepository.findByCourseidAndParentid(courseid, "0");
        //没有根节点则添加根结点
        if(teachplans==null||teachplans.size()==0){
            //新增一个根节点
            Teachplan teachplanRoot=new Teachplan();
            teachplanRoot.setCourseid(courseid);
            teachplanRoot.setGrade("1");
            //未发布
            teachplanRoot.setStatus("0");
            //父节点是自己(自己是根节点，没有父节点)
            teachplanRoot.setParentid("0");
            teachplanRoot.setPname(courseBase.getName());
            teachplanRepository.save(teachplanRoot);
            return teachplanRoot.getId();
        }
        return teachplans.get(0).getId();
    }

    public QueryResponseResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest) {
        if(courseListRequest==null){
            courseListRequest=new CourseListRequest();
        }
        if(page<=0){
            page=1;
        }
        if(size<=0){
            size=8;
        }
        //设置分页参数
        PageHelper.startPage(page,size);
        //分页查询
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);
        //判断查询的记录数是否大于零
        if(courseListPage.getTotal()>0) {
            QueryResult<CourseInfo> courseInfoResult = new QueryResult<>();
            //查询列表
            courseInfoResult.setList(courseListPage.getResult());
            //总记录数
            courseInfoResult.setTotal(courseListPage.getTotal());
            //查询结果集
            QueryResponseResult<CourseInfo> courseInfoResponseResult = new QueryResponseResult<CourseInfo>(CommonCode.SUCCESS,courseInfoResult);
           return courseInfoResponseResult;
        }
        return new QueryResponseResult<CourseInfo>(CommonCode.FAIL,null);
    }

    @Transactional
    public ResponseResult saveCourseBase(CourseBase courseBase) {
        if(courseBase==null){
            return  ResponseResult.FAIL();
        }
        CourseBase cb = courseBaseRepository.save(courseBase);
        if(! StringUtils.isBlank(cb.getId())){
            return ResponseResult.SUCCESS();
        }
        return ResponseResult.FAIL();
    }

    public CourseBase findCourseByCourseId(String courseId) {
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseId);
        if(courseBaseOptional.isPresent()){
            return courseBaseOptional.get();
        }
        return null;
    }

    public CourseMarket getCourseMarketById(String courseId) {
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(courseId);
        if(courseMarketOptional.isPresent()){
            return courseMarketOptional.get();
        }
        return null;
    }

    @Transactional
    public ResponseResult saveCourseMarket(CourseMarket courseMarket) {
        //当courseMarket为空或courseMarket.getId()为空时响应失败
        if(courseMarket==null || StringUtils.isBlank(courseMarket.getId())){
            return ResponseResult.FAIL();
        }

        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(courseMarket.getId());
        //当查询到数据时再执行操作 防止空指针
        if(courseMarketOptional.isPresent()){
            //获取营销信息数据
            CourseMarket cm = courseMarketOptional.get();
            //当新提交的价格和数据库的价格不一样时更新老价格
            if(! cm.getPrice().equals(courseMarket.getPrice())) {
                courseMarket.setPrice_old(cm.getPrice());
            }
        }
        //持久化到数据库
        courseMarketRepository.save(courseMarket);
        return ResponseResult.SUCCESS();
    }
}
