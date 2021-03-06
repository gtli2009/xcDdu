package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by Administrator.
 */
@Mapper
public interface CourseMapper {

    /**
     * 查寻课程信息
     *
     * @return
     */
    CourseBase findCourseBaseById(String id);


    Page<CourseInfo> findCourseListPage(CourseListRequest courseListRequest);

}
