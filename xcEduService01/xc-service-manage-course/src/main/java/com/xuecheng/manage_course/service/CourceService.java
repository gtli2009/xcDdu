package com.xuecheng.manage_course.service;

import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.CourseMapper;
import com.xuecheng.manage_course.dao.TeachPlanMapper;
import com.xuecheng.manage_course.dao.TeachPlanRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        if(StringUtils.isEmpty(parentid)){
        parentid = this.getTeachPlanRoot(courseid);
        }
        Optional<Teachplan> optional = teachPlanRepository.findById(parentid);
        if(!optional.isPresent()){
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
        if("1".equals(teachplanparentGrade)){
            teachplan.setGrade("2");
        }else if("2".equals(teachplanparentGrade)){
            teachplan.setGrade("3");
        }
        teachPlanRepository.save(teachplan);
        return  new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 获取课程跟节点，没有则添加
     * @param courseId
     * @return
     */
    private String getTeachPlanRoot(String courseId){
        //根据coursrid查询基本课程
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if(!optional.isPresent()){
            return null;
        }
        CourseBase courseBase = optional.get();

        List<Teachplan> teachplans = teachPlanRepository.findByCourseidAndAndParentid(courseId, "0");
        if(teachplans==null||teachplans.size()==0){
            //添加一个根节点
            Teachplan teachplanRoot =new Teachplan();
            teachplanRoot.setCourseid(courseId);
            teachplanRoot.setPname(courseBase.getName());
            teachplanRoot.setParentid("0");
            teachplanRoot.setGrade("1");
            teachplanRoot.setStatus("0");
            teachPlanRepository.save(teachplanRoot);
        }
        //有则直接返回
        Teachplan teachplan=teachplans.get(0);
        return  teachplan.getId();
    }

    /**
     * 查询课程信息
     * @param page
     * @param size
     * @param
     * @return
     */
    public QueryResponseResult findCourceList(int page, int size,QueryPageRequest queryPageRequest) {
        PageHelper.startPage(page,size);
        //查询课程名称和图片
        //查询失败
        List<CourseInfo> courceList = courseMapper.findCourceList();
        if(courceList==null || courceList.size()==0){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
         }
         //查询成功
        QueryResult queryResult =new QueryResult();
        queryResult.setList(courceList);
        QueryResponseResult queryResponseResult=new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }
}
