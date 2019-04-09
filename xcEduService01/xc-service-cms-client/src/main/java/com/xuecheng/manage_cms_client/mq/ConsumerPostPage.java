package com.xuecheng.manage_cms_client.mq;


import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConsumerPostPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(PageService.class);
    @Autowired
    private PageService pageService;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg) {
        //解析消息
        Map map = JSON.parseObject(msg, Map.class);
        //校验页面是否合法
        String pageId = (String) map.get("pageId");
        CmsPage cmsPage = pageService.findCmsPageById(pageId);
        //添加日志
        if (cmsPage == null) {
            LOGGER.error("receive postpage msg,CmsPage is null , pageId:{}", pageId);
            return;
        }
        //调用方法将页面从GridsFs下载到服务器
        pageService.savePagetoServerPath(pageId);
    }

}
