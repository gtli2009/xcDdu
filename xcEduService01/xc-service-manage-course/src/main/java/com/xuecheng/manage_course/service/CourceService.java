package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.netflix.discovery.converters.Auto;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.domain.system.SysDictionary;
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

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CourceService {
    @Autowired
    private TeachPlanMapper teachPlanMapper;
    @Autowired
    private TeachPlanRepository teachPlanRepository;
    @Autowired
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SysDictionaryRepository sysDictionaryRepository;
    @Autowired
    private CourseMarketRepository courseMarketRepository;
    @Autowired
    private CoursePicRepository coursePicRepository;
    @Autowired
    private CmsPageClient cmsPageClient;
    @Autowired
    private CoursePubRepository coursePubRepository;
    @Autowired
    private TeachPlanMediaRepository teachPlanMediaRepository;
    @Autowired
    private TeachPlanMediaPubRepository teachPlanMediaPubRepository;

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


    /**
     * 课程查询
     *
     * @param courceId
     * @return
     */
    public TeachplanNode selectLisst(String courceId) {
        return teachPlanMapper.selectList(courceId);
    }

    /**
     * 添加课程计划
     *
     * @param teachplan
     * @return
     */
    @Transactional
    public ResponseResult addTeachPlan(Teachplan teachplan) {
        //判断数据的合法性
        if (teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) ||
                StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //课程id
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();
        //添加根节点
        //如果父节点为空，则添加
        if (StringUtils.isEmpty(parentid)) {
            parentid = this.getTeachPlanRoot(courseid);
        }
        Optional<Teachplan> optional = teachPlanRepository.findById(parentid);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //获取父节点
        Teachplan teachplanparent = optional.get();
        //获取父节点级别
        String teachplanparentGrade = teachplanparent.getGrade();

        //设置父节点
        teachplan.setParentid(parentid);
        //未发布
        teachplan.setStatus("0");
        //根据父节点的级别来确定子节点
        if ("1".equals(teachplanparentGrade)) {
            teachplan.setGrade("2");
        } else if ("2".equals(teachplanparentGrade)) {
            teachplan.setGrade("3");
        }
        teachPlanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 获取课程跟节点，没有则添加
     *
     * @param courseId
     * @return
     */
    private String getTeachPlanRoot(String courseId) {
        //根据coursrid查询基本课程
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            return null;
        }
        CourseBase courseBase = optional.get();

        List<Teachplan> teachplans = teachPlanRepository.findByCourseidAndAndParentid(courseId, "0");
        if (teachplans == null || teachplans.size() == 0) {
            //添加一个根节点
            Teachplan teachplanRoot = new Teachplan();
            teachplanRoot.setCourseid(courseId);
            teachplanRoot.setPname(courseBase.getName());
            teachplanRoot.setParentid("0");
            teachplanRoot.setGrade("1");
            teachplanRoot.setStatus("0");
            teachPlanRepository.save(teachplanRoot);
        }
        //有则直接返回
        Teachplan teachplan = teachplans.get(0);
        return teachplan.getId();
    }

    /**
     * 查询课程信息
     *
     * @param page
     * @param size
     * @param
     * @return
     */
    public QueryResponseResult findCourceList(int page, int size, CourseListRequest courseListRequest) {
        //条件判断
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        if (page <= 0) {
            page = 0;
        }
        if (size < 0) {
            size = 10;
        }
        PageHelper.startPage(page, size);

        //查询课程名称和图片
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);

        //查询列表
        List<CourseInfo> result = courseListPage.getResult();
        //查询总记录数
        long total = courseListPage.getTotal();

        //存入QueryResult中
        QueryResult<CourseInfo> queryResult = new QueryResult();
        queryResult.setList(result);
        queryResult.setTotal(total);
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    /**
     * 查询分类信息
     *
     * @return
     */
    public CategoryNode findList() {
        return categoryMapper.findList();
    }

    /**
     * 查询数据字典
     *
     * @param type
     * @return
     */
    public SysDictionary findByType(String type) {
        return sysDictionaryRepository.findSysDictionaryByDType(type);
    }

    /**
     * 添加课程信息
     *
     * @param courseBase
     * @return
     */
    public AddCourseResult addCourseBase(CourseBase courseBase) {
        //未发布
        courseBase.setStatus("202001");
        courseBaseRepository.save(courseBase);
        return new AddCourseResult(CommonCode.SUCCESS, courseBase.getId());
    }

    /**
     * 查询我的课程基本信息
     *
     * @param courceId
     * @return
     */
    public CourseBase getCourceBaseById(String courceId) {
        Optional<CourseBase> optionalBase = courseBaseRepository.findById(courceId);
        if (optionalBase.isPresent()) {
            return optionalBase.get();
        }
        return null;
    }


    /**
     * 更新我的课程基本信息
     *
     * @param id
     * @param courseBase
     * @return
     */
    @Transactional
    public ResponseResult updateCourceBase(String id, CourseBase courseBase) {
        //查询
        CourseBase one = this.getCourceBaseById(id);
        if (one == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //修改课程
        one.setName(courseBase.getName());
        one.setMt(courseBase.getMt());
        one.setSt(courseBase.getSt());
        one.setGrade(courseBase.getGrade());
        one.setStudymodel(courseBase.getStudymodel());
        one.setUsers(courseBase.getUsers());
        one.setDescription(courseBase.getDescription());
        one.setStudymodel(courseBase.getStudymodel());
        CourseBase save = courseBaseRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 查询我的课程营销信息
     *
     * @param courseId
     * @return
     */
    public CourseMarket getCourceMarketById(String courseId) {
        Optional<CourseMarket> optional = courseMarketRepository.findById(courseId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    /**
     * 修改我的课程营销信息
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseResult updateCourseMarket(String id, CourseMarket courseMarket) {
        CourseMarket one = this.getCourceMarketById(id);
        //有值则更新
        if (one != null) {
            one.setCharge(courseMarket.getCharge());
            one.setStartTime(courseMarket.getStartTime());
            one.setEndTime(courseMarket.getEndTime());
            one.setPrice(courseMarket.getPrice());
            one.setPrice_old(courseMarket.getPrice_old());
            one.setQq(courseMarket.getQq());
            one.setValid(courseMarket.getValid());
            courseMarketRepository.save(one);
        } else {
            //数据库没有则新建
            one = new CourseMarket();
            BeanUtils.copyProperties(courseMarket, one);
            //要添加id
            one.setId(id);
            courseMarketRepository.save(one);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 保存图片信息
     *
     * @param courseId
     * @param pic
     * @return
     */
    @Transactional
    public ResponseResult addCoursePic(String courseId, String pic) {
        CoursePic coursePic = null;
        //查询图片信息
        Optional<CoursePic> optionalPic = coursePicRepository.findById(courseId);
        if (optionalPic.isPresent()) {
            coursePic = optionalPic.get();
        }
        if (coursePic == null) {
            coursePic = new CoursePic();
        }
        //添加
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 查询图片信息
     *
     * @param courseId
     * @return
     */
    public CoursePic getFileSystem(String courseId) {
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    /**
     * 删除图片信息
     *
     * @param courseId
     * @return
     */
    public ResponseResult deleteCoursePic(String courseId) {
        long l = coursePicRepository.deleteByCourseid(courseId);
        if (l > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        } else {
            return new ResponseResult(CommonCode.FAIL);
        }
    }

    /**
     * 课程视图查询
     *
     * @param id
     * @return
     */
    public CourseView getCourseView(String id) {
        CourseView courseView = new CourseView();
        //查询课程基本 信息
        Optional<CourseBase> optionalBase = courseBaseRepository.findById(id);
        if (optionalBase.isPresent()) {
            CourseBase courseBase = optionalBase.get();
            courseView.setCourseBase(courseBase);
        }
        //查询课程图片
        Optional<CoursePic> optionalCoursePic = coursePicRepository.findById(id);
        if (optionalCoursePic.isPresent()) {
            CoursePic coursePic = optionalCoursePic.get();
            courseView.setCoursePic(coursePic);
        }
        //查询课程营销信息
        Optional<CourseMarket> courseMarket = courseMarketRepository.findById(id);
        if (courseMarket.isPresent()) {
            CourseMarket courseMarket1 = courseMarket.get();
            courseView.setCourseMarket(courseMarket1);
        }
        //获取课程计划信息
        TeachplanNode teachplanNode = teachPlanMapper.selectList(id);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }

    /**
     * 课程预览
     *
     * @param id
     * @return
     */
    //课程预览
    public CoursePublishResult preview(String id) {
        //查询课程
        CourseBase courseBaseById = this.findCourseBaseById(id);
        //请求cms添加页面
        //准备cmsPage信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);//站点id
        cmsPage.setDataUrl(publish_dataUrlPre + id);//数据模型url
        cmsPage.setPageName(id + ".html");//页面名称
        cmsPage.setPageAliase(courseBaseById.getName());//页面别名，就是课程名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);//页面物理路径
        cmsPage.setPageWebPath(publish_page_webpath);//页面webpath
        cmsPage.setTemplateId(publish_templateId);//页面模板id

        //远程调用cms
        CmsPageResult cmsPageResult = cmsPageClient.saveCmsPage(cmsPage);
        if (cmsPageResult == null) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }

        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        String pageId = cmsPage1.getPageId();
        //拼装页面预览的url
        String url = previewUrl + pageId;
        //返回CoursePublishResult对象（当中包含了页面预览的url）
        return new CoursePublishResult(CommonCode.SUCCESS, url);
    }

    //根据id查询课程基本信息
    public CourseBase findCourseBaseById(String courseId) {
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(courseId);
        if (baseOptional.isPresent()) {
            CourseBase courseBase = baseOptional.get();
            return courseBase;
        }
        ExceptionCast.cast(CourseCode.COURSE_DENIED_DELETE);
        return null;
    }

    /**
     * 课程发布
     *
     * @param id
     * @return
     */
    @Transactional
    public CoursePublishResult publish(String id) {
        //调用cms一键发布，将页面详情发布到服器
        //准备页面信息
        //查询课程
        CourseBase courseBaseById = this.findCourseBaseById(id);
        //准备cmsPage信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);//站点id
        cmsPage.setDataUrl(publish_dataUrlPre + id);//数据模型url
        cmsPage.setPageName(id + ".html");//页面名称
        cmsPage.setPageAliase(courseBaseById.getName());//页面别名，就是课程名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);//页面物理路径
        cmsPage.setPageWebPath(publish_page_webpath);//页面webpath
        cmsPage.setTemplateId(publish_templateId);//页面模板id
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if (cmsPostPageResult == null) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        //保存课程发布的状态为已经发布
        CourseBase courseBase = this.saveCoursePuStatue(id);
        if (courseBase == null) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }

        //保存汇总的课程
        CoursePub coursePub = createCoursePub(id);
        //保存coursepub信息
        CoursePub coursePub1 = saveCoursePub(id, coursePub);
        String pageUrl = cmsPostPageResult.getPageUrl();
        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
    }

    //向teachplanMediaPub中保存课程媒资信息
    private void saveTeachplanMediaPub(String courseId){
        //先删除teachplanMediaPub中的数据
        teachPlanMediaPubRepository.deleteByCourseId(courseId);
        //从teachplanMedia中查询
        List<TeachplanMedia> teachplanMediaList = teachPlanMediaRepository.findByCourseId(courseId);
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
        teachPlanMediaPubRepository.saveAll(teachplanMediaPubs);
    }




    //保存课程信息
    private CoursePub saveCoursePub(String id, CoursePub coursePub) {
        if (StringUtils.isNotEmpty(id)) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        CoursePub coursePubNew = null;
        //查询课程
        Optional<CoursePub> optionalPub = coursePubRepository.findById(id);
        if (optionalPub.isPresent()) {
            coursePubNew = optionalPub.get();
        } else {
            coursePubNew = new CoursePub();
        }
        BeanUtils.copyProperties(coursePub, coursePubNew);
        //更新时间戳
        coursePub.setTimestamp(new Date());
        //更新发布时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY‐MM‐dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        coursePub.setPubTime(date);
        coursePubRepository.save(coursePub);
        return coursePub;
    }


    /**
     * 修改课程状态
     *
     * @param courseId
     * @return
     */
    private CourseBase saveCoursePuStatue(String courseId) {
        CourseBase courseBaseById = this.findCourseBaseById(courseId);
        courseBaseById.setStatus("202002");
        courseBaseRepository.save(courseBaseById);
        return courseBaseById;
    }

    /**
     * 查询课程汇总信息
     *
     * @param id
     * @return
     */
    private CoursePub createCoursePub(String id) {
        CoursePub coursePub = new CoursePub();
        Optional<CourseBase> optionalBase = courseBaseRepository.findById(id);
        if (optionalBase.isPresent()) {
            CourseBase courseBase = optionalBase.get();
            //拷贝课程基本信息
            BeanUtils.copyProperties(courseBase, coursePub);
        }
        //查询pic
        Optional<CoursePic> picOptional = coursePicRepository.findById(id);
        if (picOptional.isPresent()) {
            CoursePic coursePic = picOptional.get();
            BeanUtils.copyProperties(coursePic, coursePub);
        }


        //课程营销信息
        Optional<CourseMarket> marketOptional = courseMarketRepository.findById(id);
        if (marketOptional.isPresent()) {
            CourseMarket courseMarket = marketOptional.get();
            BeanUtils.copyProperties(courseMarket, coursePub);
        }

        //课程计划信息
        TeachplanNode teachplanNode = teachPlanMapper.selectList(id);
        String s = JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(s);
        return coursePub;
    }

    /**
     * 保存课程计划与媒资关联
     *
     * @param
     * @return
     */
    public ResponseResult savemedia(TeachplanMedia teachplanMedia) {
        if (teachplanMedia == null||StringUtils.isEmpty(teachplanMedia.getTeachplanId())) {
            ExceptionCast.cast(CommonCode.INVALI_DPARAM);
        }
        //课程计划
        String teachplanId = teachplanMedia.getTeachplanId();
        //查询课程计划
        Optional<Teachplan> optional = teachPlanRepository.findById(teachplanId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_ISNULL);
        }
        Teachplan teachplan = optional.get();
        //只允许为叶子结点课程计划选择视频S
        String grade = teachplan.getGrade();
        if (StringUtils.isEmpty(grade) || !grade.equals("3")) {
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_GRADEERROR);
        }
        TeachplanMedia one = null;
        Optional<TeachplanMedia> teachplanMediaOptional = teachPlanMediaRepository.findById(teachplanId);
        if (!teachplanMediaOptional.isPresent()) {
            one = new TeachplanMedia();
        } else {
            one = teachplanMediaOptional.get();
        }
        //保存媒资信息与课程计划信息
        one.setTeachplanId(teachplanId);
        one.setCourseId(teachplanMedia.getCourseId());
        one.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());
        one.setMediaId(teachplanMedia.getMediaId());
        one.setMediaUrl(teachplanMedia.getMediaUrl());
        teachPlanMediaRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }


}

