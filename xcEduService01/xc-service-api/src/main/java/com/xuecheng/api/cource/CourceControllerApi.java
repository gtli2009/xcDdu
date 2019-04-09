package com.xuecheng.api.cource;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api(value = "课程管理接口",description = "课程管理接口，提供数据模型管理。查询接口")
public interface CourceControllerApi {
    /**
     * 课程计划查询
     * @param courceId
     * @return
     */
    @ApiOperation("课程计划查询")
    TeachplanNode findTechPlanList(String courceId);

    /**
     * 添加课程计划
     * @param teachplan
     * @return
     */
    @ApiOperation("课程计划查询")
    ResponseResult addTeachPlan (Teachplan  teachplan);

    @ApiOperation("我的课程查询")
    QueryResponseResult findCourceList(int page, int size,QueryPageRequest queryPageRequest);

}
