package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "通用文件管理接口",description = "文件管理接口，提供文件管理，查询接口")
public interface FileSystemControllerApi {
    @ApiOperation("文件上传")
    UploadFileResult  upload(MultipartFile multipartFile, String businesskey,String filetag, String metadata);
}
