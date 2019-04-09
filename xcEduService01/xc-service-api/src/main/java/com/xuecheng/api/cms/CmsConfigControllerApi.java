package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "cms配置管理接口",description = "cms配置管理接口，提供数据模型管理。查询接口")
public interface CmsConfigControllerApi {
    /**
     * 根据id查询层面上配置信息
     * @param id
     * @return
     */
    @ApiOperation("根据id查询CMS的配置信息")
    CmsConfig getmodel(String id);
}
