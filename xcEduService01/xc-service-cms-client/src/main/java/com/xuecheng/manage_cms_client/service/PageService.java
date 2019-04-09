package com.xuecheng.manage_cms_client.service;


import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class PageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(PageService.class);
    /**
     * 保存文件的物理路径
     *
     * @param pageId
     */
    public void savePagetoServerPath(String pageId) {
        //从html文件得到文件id
        CmsPage cmsPage = this.findCmsPageById(pageId);
        String htmlFileId = cmsPage.getHtmlFileId();
        //从gridsFS查HTML文件
        InputStream inputStream = this.getFileById(htmlFileId);
        if(inputStream==null){
            LOGGER.error("getFileById is null htmlFileId:{}",htmlFileId);
            return;
        }
        //根据站点id得到站点信息
        String siteId = cmsPage.getSiteId();
        CmsSite cmsSite = this.findCmsSiteById(siteId);
        //将html保存至服务器物理路径
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();
        //页面的物理路径
     String pagePath=  sitePhysicalPath+cmsPage.getPagePhysicalPath()+cmsPage.getPageName();
        FileOutputStream fileOutputStream=null;
     try {
             fileOutputStream =new FileOutputStream(pagePath);
         IOUtils.copy(inputStream,fileOutputStream);
        } catch (IOException e) {
         e.printStackTrace();
     }finally {
         try {
             fileOutputStream.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
         try {
             inputStream.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

    }

    /**
     * 根据页面id查询页面信息
     *
     * @param pageId
     */
    public CmsPage findCmsPageById(String pageId) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    /**
     * 根据站点id得到站点信息
     * @param siteId
     * @return
     */
    public CmsSite findCmsSiteById(String siteId) {
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }



    /**
     * 根据fileId从GridFs查询文件
     * @param fileId
     * @return
     */
    public InputStream getFileById(String fileId) {
        //获取文件对象
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        //获取流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //定义GridsResources
        GridFsResource gridFsResource=new GridFsResource(gridFSFile,gridFSDownloadStream);
        try {
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
