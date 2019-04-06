package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;


@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Test
    public void testFindAll() {
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }

    @Test
    public void testFindAllByExample() {
        //分页参数
        int page = 0;//从0开始
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        //条件查询
        CmsPage cmsPage = new CmsPage();
        //cmsPage.setPageName("index_banner.html");
        cmsPage.setPageAliase("轮播");
        //条件匹配
        ExampleMatcher exampleMatcher =ExampleMatcher.matching();
        //包含关键字
         exampleMatcher = exampleMatcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        //定义example
        Example<CmsPage> example =Example.of(cmsPage,exampleMatcher);
        Page<CmsPage> all=cmsPageRepository.findAll(example,pageable);
        List<CmsPage> content = all.getContent();
        System.out.println(content);

    }

    //分页查询
    @Test
    public void testFindPage() {
        //分页参数
        int page = 0;//从0开始
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }


    //修改
    @Test
    public void testUpdate() {
        //查询对象
        Optional<CmsPage> optional = cmsPageRepository.findById("5b4b1d8bf73c6623b03f8cec");
        if (optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            //设置要修改值
            cmsPage.setPageAliase("test01");
            //...
            //修改
            CmsPage save = cmsPageRepository.save(cmsPage);
            System.out.println(save);
        }

    }

    //根据页面名称查询
    @Test
    public void testfindByPageName() {
        //  CmsPage cmsPage = cmsPageRepository.findByPageName("测试页面");
        //   System.out.println(cmsPage);
    }
}
