package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CourseMarketRepository courseMarketRepository;
    @Autowired
    private CoursePicRepository coursePicRepository;
    @Autowired
    private CoursePubRepository coursePubRepository;
    @Autowired
    private TeachplanMediaRepository teachplanMediaRepository;
    @Autowired
    private TeachplanMediaPubRepository teachplanMediaPubRepository;
    @Autowired
    private CmsPageClient cmsPageClient;

    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;

    public TeachplanNode selectList(String courseId) {
        return teachplanMapper.selectList(courseId);
    }

    //添加课程计划
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        //校验课程id和课程计划名称
        if(teachplan==null || StringUtils.isEmpty(teachplan.getCourseid())
                || StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
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
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
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
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
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
        //课程状态默认为未发布
        courseBase.setStatus("202001");
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

    @Transactional
    public ResponseResult saveCoursePic(String courseId, String pic) {
        //查询课程图片
        Optional<CoursePic> coursePicOptional = coursePicRepository.findById(courseId);
        CoursePic coursePic=null;
        if(coursePicOptional.isPresent()){
           coursePic = coursePicOptional.get();
        }
        //没有课程图片则创建新对象
        if(coursePic==null){
            coursePic=new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        //保存课程图片
        coursePicRepository.save(coursePic);
        return ResponseResult.SUCCESS();
    }


    public CoursePic findCoursePic(String courseId) {
        if(StringUtils.isBlank(courseId)){
            ExceptionCast.cast(CourseCode.COURSE_PIC_NOTEXIST);
        }
        Optional<CoursePic> coursePicOptional = coursePicRepository.findById(courseId);
        if(coursePicOptional.isPresent()){
            CoursePic coursePic = coursePicOptional.get();
            return coursePic;
        }
        return null;
    }


    /**
     * 删除课程图片
     */
    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        Long effectRow = coursePicRepository.deleteByCourseid(courseId);
        //执行删除，返回1表示删除成功，返回0表示删除失败
        if(effectRow>0){
            return ResponseResult.SUCCESS();
        }
        return ResponseResult.FAIL();
    }

    /**
     *根据课程id查询课程视图
     * @param id
     * @return
     */
    public CourseView getCoruseView(String id) {
        if(StringUtils.isBlank(id)){
            return null;
        }
        CourseView courseView = new CourseView();
        //查询课程基础信息
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(id);
        if(courseBaseOptional.isPresent()){
            courseView.setCourseBase(courseBaseOptional.get());
        }
        //查询课程图片
        Optional<CoursePic> coursePicOptional = coursePicRepository.findById(id);
        if(coursePicOptional.isPresent()){
            courseView.setCoursePic(coursePicOptional.get());
        }

        //查询教学计划
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        courseView.setTeachplanNode(teachplanNode);

        //查询课程营销信息
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(id);
        if(courseMarketOptional.isPresent()){
            courseView.setCourseMarket(courseMarketOptional.get());
        }
        return courseView;
    }

    public CoursePublishResult preview(String id) {
        //查询课程
        CourseBase courseBaseById = this.findCourseByCourseId(id);
        //请求cms添加页面
        //准备cmsPage信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);//站点id
        cmsPage.setDataUrl(publish_dataUrlPre+id);//数据模型url
        cmsPage.setPageName(id+".html");//页面名称
        cmsPage.setPageAliase(courseBaseById.getName());//页面别名，就是课程名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);//页面物理路径
        cmsPage.setPageWebPath(publish_page_webpath);//页面webpath
        cmsPage.setTemplateId(publish_templateId);//页面模板id

        //远程调用cms
        CmsPageResult cmsPageResult = cmsPageClient.saveCmsPage(cmsPage);
        if(!cmsPageResult.isSuccess()){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }

        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        String pageId = cmsPage1.getPageId();
        //拼装页面预览的url
        String url = previewUrl+pageId;
        //返回CoursePublishResult对象（当中包含了页面预览的url）
        return new CoursePublishResult(CommonCode.SUCCESS,url);
    }

    //课程发布
    @Transactional
    public CoursePublishResult publish(String id) {
        //查询课程
        CourseBase courseBaseById = this.findCourseByCourseId(id);

        //准备页面信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);//站点id
        cmsPage.setDataUrl(publish_dataUrlPre+id);//数据模型url
        cmsPage.setPageName(id+".html");//页面名称
        cmsPage.setPageAliase(courseBaseById.getName());//页面别名，就是课程名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);//页面物理路径
        cmsPage.setPageWebPath(publish_page_webpath);//页面webpath
        cmsPage.setTemplateId(publish_templateId);//页面模板id
        //调用cms一键发布接口将课程详情页面发布到服务器
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if(!cmsPostPageResult.isSuccess()){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }

        //保存课程的发布状态为“已发布”
        CourseBase courseBase = this.saveCoursePubState(id);
        if(courseBase == null){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }

        //保存课程索引信息
         CoursePub coursePub = this.createCoursePub(id);
        //向数据库保存课程索引信息 带id参数是为了代码的可读性
        CoursePub newCoursePub = this.saveCoursePub(id, coursePub);

         if(newCoursePub==null) {
             //创建课程索引信息失败
             ExceptionCast.cast(CourseCode.COURSE_PUBLISH_CREATE_INDEX_ERROR);
         }
        //缓存课程的信息
        //...
        //得到页面的url
        String pageUrl = cmsPostPageResult.getPageUrl();
        //向teachplanMediaPub中保存课程媒资信息
        this.saveTeachplanMediaPub(id);
        return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
    }

    //向teachplanMediaPub中保存课程媒资信息
    private void saveTeachplanMediaPub(String courseId){
        //先删除teachplanMediaPub中的数据
        teachplanMediaPubRepository.deleteByCourseId(courseId);
        //从teachplanMedia中查询
        List<TeachplanMedia> teachplanMediaList = teachplanMediaRepository.findByCourseId(courseId);
        List<TeachplanMediaPub> teachplanMediaPubs = new ArrayList<>();
        //将teachplanMediaList数据放到teachplanMediaPubs中
        for(TeachplanMedia teachplanMedia:teachplanMediaList){
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            BeanUtils.copyProperties(teachplanMedia,teachplanMediaPub);
            //添加时间戳
            teachplanMediaPub.setTimestamp(new Date());
            teachplanMediaPubs.add(teachplanMediaPub);
        }

        //将teachplanMediaList插入到teachplanMediaPub
        teachplanMediaPubRepository.saveAll(teachplanMediaPubs);
    }

    /**
     *  向数据库保存课程索引信息
     *  思路:
     *     1.根据课程id查询,没有则添加
     *     2.有则更新
     * @param id
     * @param coursePub
     * @return
     */
    private CoursePub saveCoursePub(String id, CoursePub coursePub) {
        //如果id为空
        if(StringUtils.isBlank(id)){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }

        CoursePub coursePubNew=null;
        //先根据课程id查询
        Optional<CoursePub> coursePubOptional = coursePubRepository.findById(id);
        if(coursePubOptional.isPresent()){
            coursePubNew = coursePubOptional.get();
        }

        if(coursePubNew==null){
            coursePubNew=new CoursePub();
        }

        BeanUtils.copyProperties(coursePub,coursePubNew);
        //再次设置主键(可不设因为前面设置过,为了严瑾再次设置一下)
        coursePubNew.setId(id);
        //时间戳logstash使用
        coursePubNew.setTimestamp(new Date());
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String pubTime = date.format(dateTimeFormatter);
        //设置发布时间
        coursePubNew.setPubTime(pubTime);
        CoursePub save = coursePubRepository.save(coursePubNew);

        return save;
    }

    /**
     * 保存课程索引信息
     * @param courseId
     * @return
     */
    private CoursePub  createCoursePub(String courseId){
        CoursePub coursePub=new CoursePub();
        //提前设置id,防止查询不出信息还能返回id提示用户
        coursePub.setId(courseId);
        //查询课程基础信息
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseId);
        if(courseBaseOptional.isPresent()){
            CourseBase courseBase = courseBaseOptional.get();
            BeanUtils.copyProperties(courseBase,coursePub);
        }
        //查询课程图片
        Optional<CoursePic> coursePicOptional = coursePicRepository.findById(courseId);
        if(coursePicOptional.isPresent()){
            CoursePic coursePic = coursePicOptional.get();
            BeanUtils.copyProperties(coursePic,coursePub);
        }

        //查询教学计划
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        //将课程计划转化为json
        String teachplanStr= JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(teachplanStr);

        //查询课程营销信息
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(courseId);
        if(courseMarketOptional.isPresent()){
            CourseMarket courseMarket = courseMarketOptional.get();
            BeanUtils.copyProperties(courseMarket,coursePub);
        }
        return coursePub;
    }


    //更新课程状态为已发布 202002
    private CourseBase  saveCoursePubState(String courseId){
        CourseBase courseBaseById = this.findCourseByCourseId(courseId);
        courseBaseById.setStatus("202002");
        courseBaseRepository.save(courseBaseById);
        return courseBaseById;
    }

    //保存课程计划与媒资文件的关联信息
    public ResponseResult savemedia(TeachplanMedia teachplanMedia) {
        if(teachplanMedia == null || StringUtils.isEmpty(teachplanMedia.getTeachplanId())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //校验课程计划是否是3级
        //课程计划
        String teachplanId = teachplanMedia.getTeachplanId();
        //查询到课程计划
        Optional<Teachplan> optional = teachplanRepository.findById(teachplanId);
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //查询到教学计划
        Teachplan teachplan = optional.get();
        //取出等级
        String grade = teachplan.getGrade();
        if(StringUtils.isEmpty(grade) || !grade.equals("3")){
            //只允许选择第三级的课程计划关联视频
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_GRADEERROR);
        }
        //查询teachplanMedia
        Optional<TeachplanMedia> mediaOptional = teachplanMediaRepository.findById(teachplanId);
        TeachplanMedia one = null;
        if(mediaOptional.isPresent()){
            one = mediaOptional.get();
        }else{
            one = new TeachplanMedia();
        }

        //将one保存到数据库
        one.setCourseId(teachplan.getCourseid());//课程id
        one.setMediaId(teachplanMedia.getMediaId());//媒资文件的id
        one.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());//媒资文件的原始名称
        one.setMediaUrl(teachplanMedia.getMediaUrl());//媒资文件的url
        one.setTeachplanId(teachplanId);
        teachplanMediaRepository.save(one);

        return ResponseResult.SUCCESS();
    }

}
