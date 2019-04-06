package com.xuecheng.test.freemarker;


import com.xuecheng.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@SpringBootTest(classes = FreemarkerTest.class)
@RunWith(SpringRunner.class)
public class FreemarkerTest {
       @Test
    public void GenerateHtml() throws IOException, TemplateException {
        //1.定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //2.得到classpath路径
        String path = this.getClass().getResource("/").getPath();
        //3.定义模板文件
        configuration.setDirectoryForTemplateLoading(new File(path + "/templates/"));
        //4，获取模板文件
        Template template = configuration.getTemplate("test1.ftl");
        //5.定义数据模型
        Map map = getMap();
        //6.静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        //     System.out.println(content);
        //7.输出
        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream outputStream = new FileOutputStream(new File("E:\\xcEdu\\xcEdu\\xcEduService01\\test-freemarker\\src\\test\\resources\\test1.html"));
        int copy = IOUtils.copy(inputStream, outputStream);
        outputStream.close();
    }
    @Test
    public void GenerateHtmlByString() throws IOException, TemplateException {
        //1.定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //3.定义模板文件
        String templateString="" +
                "<html>\n" +
                "    <head></head>\n" +
                "    <body>\n" +
                "    名称：${name}\n" +
                "    </body>\n" +
                "</html>";
        //4，获取模板加载器
        StringTemplateLoader stringTemplateLoader=new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateString);
        configuration.setTemplateLoader(stringTemplateLoader);

        //5.获取模板内容

        Template template = configuration.getTemplate("template", "utf-8");
        //6.静态化
        Map map = getMap();
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        //     System.out.println(content);
        //7.输出
        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream outputStream = new FileOutputStream(new File("E:\\xcEdu\\xcEdu\\xcEduService01\\test-freemarker\\src\\test\\resources\\test1.html"));
        int copy = IOUtils.copy(inputStream, outputStream);
        outputStream.close();
    }

    public Map getMap() {
        Map map = new HashMap();
        //向数据模型放数据
        map.put("name", "黑马程序员");
        Student stu1 = new Student();
        stu1.setName("小明");
        stu1.setAge(18);
        stu1.setMondy(1000.86f);
        stu1.setBirthday(new Date());
        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMondy(200.1f);
        stu2.setAge(19);
//        stu2.setBirthday(new Date());
        List<Student> friends = new ArrayList<>();
        friends.add(stu1);
        stu2.setFriends(friends);
        stu2.setBestFriend(stu1);
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);
        //向数据模型放数据
        map.put("stus", stus);
        //准备map数据
        HashMap<String, Student> stuMap = new HashMap<>();
        stuMap.put("stu1", stu1);
        stuMap.put("stu2", stu2);
        //向数据模型放数据
        map.put("stu1", stu1);
        //向数据模型放数据
        map.put("stuMap", stuMap);
        //返回模板文件名称
        return map;
    }

}
