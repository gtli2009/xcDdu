package com.xuecheng.api.cource;

import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程管理接口", description = "课程管理接口，提供数据模型管理。查询接口")
public interface CourceControllerApi {
    /**
     * 课程计划查询
     *
     * @param courceId
     * @return
     */
    @ApiOperation("课程计划查询")
    TeachplanNode findTechPlanList(String courceId);

    /**
     * 添加课程计划
     *
     * @param teachplan
     * @return
     */
    @ApiOperation("课程计划查询")
    ResponseResult addTeachPlan(Teachplan teachplan);

    /**
     * 我的课程查询
     *
     * @param page
     * @param size
     * @param courseListRequest
     * @return
     */
    @ApiOperation("我的课程查询")
    QueryResponseResult findCourceList(int page, int size, CourseListRequest courseListRequest);

    /**
     * 添加我的课程基本信息
     *
     * @param courseBase
     * @return
     */
    @ApiOperation("我的课程基本信息的添加")
    AddCourseResult addCourseBase(CourseBase courseBase);

    /**
     * 查询我的课程基本信息
     *
     * @param
     * @return
     */
    @ApiOperation("查询我的课程基本信息")
    CourseBase getCourceBaseById(String courseId);

    /**
     * 更改我的课程基本信息
     *
     * @param id
     * @param courseBase
     * @return
     */
    @ApiOperation("更改我的课程基本信息")
    ResponseResult updateCourceBase(String id, CourseBase courseBase);

    /**
     * 查询我的课程营销信息
     *
     * @param courseId
     * @return
     */
    @ApiOperation("查询我的课程营销信息")
    CourseMarket getCourceMarketById(String courseId);

    /**
     * 修改我的课程营销信息
     *
     * @param id
     * @return
     */
    @ApiOperation("修改我的课程营销信息")
    ResponseResult updateCourseMarket(String id, CourseMarket courseMarket);

    @ApiOperation("添加图片相关信息")
    ResponseResult addCoursePic(String courseId, String pic);

    @ApiOperation("查询图片相关信息")
    CoursePic getFileSystem(String courseId);

    @ApiOperation("删除图片相关信息")
    ResponseResult deleteCoursePic(String courseId);

    @ApiOperation("课程视图查询")
    CourseView courseview(String id);

    @ApiOperation("预览课程")
    CoursePublishResult preview(String id);

    @ApiOperation("课程发布")
    CoursePublishResult publish(String id);

    @ApiOperation("保存课程计划与媒资")
    ResponseResult savemedia(TeachplanMedia teachplanMedia);



}
