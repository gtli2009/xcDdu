package com.xuecheng.api.cource;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程管理接口",description = "课程管理接口，提供数据模型管理。查询接口")
public interface CategoryControllerApi {
    @ApiOperation("课程计划查询")
    CategoryNode findList();
}
