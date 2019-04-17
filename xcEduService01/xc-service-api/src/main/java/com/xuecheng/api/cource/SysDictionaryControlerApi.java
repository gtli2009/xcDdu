package com.xuecheng.api.cource;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "数据字典接口", description = "数据字典接口，查询接口")
public interface SysDictionaryControlerApi {
    @ApiOperation("数据字典查询")
    SysDictionary getByType(String type);
}
