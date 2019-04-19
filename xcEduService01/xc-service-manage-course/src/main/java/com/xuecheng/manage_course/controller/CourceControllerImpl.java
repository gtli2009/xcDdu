package com.xuecheng.manage_course.controller;

import com.xuecheng.api.cource.CourceControllerApi;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourceControllerImpl implements CourceControllerApi {
    @Autowired
    private CourceService courceService;

    @Override
    @GetMapping("/teachplan/list/{courceId}")
    public TeachplanNode findTechPlanList(@PathVariable("courceId") String courceId) {
        return courceService.selectLisst(courceId);
    }

    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachPlan(@RequestBody Teachplan teachplan) {
        return courceService.addTeachPlan(teachplan);
    }

    @Override
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult findCourceList(@PathVariable("page") int page, @PathVariable("size") int size, CourseListRequest courseListRequest) {
        return courceService.findCourceList(page, size, courseListRequest);
    }

    @Override
    @PostMapping("/coursebase/add")
    public AddCourseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return courceService.addCourseBase(courseBase);
    }

    @Override
    @GetMapping("/courseview/{courseId}")
    public CourseBase getCourceBaseById(@PathVariable("courseId") String courseId) {
        return courceService.getCourceBaseById(courseId);
    }

    @Override
    @PutMapping("/coursebase/update/{id}")
    public ResponseResult updateCourceBase(@PathVariable("id") String id, @RequestBody CourseBase courseBase) {
        return courceService.updateCourceBase(id, courseBase);
    }

    @Override
    @GetMapping("/coursemarket/get/{id}")
    public CourseMarket getCourceMarketById(@PathVariable("id") String id) {
        return courceService.getCourceMarketById(id);
    }

    @Override
    @PostMapping("/coursemarket/update/{id}")
    public ResponseResult updateCourseMarket(@PathVariable("id") String id, @RequestBody CourseMarket courseMarket) {
        return courceService.updateCourseMarket(id, courseMarket);
    }

    @Override
    @PostMapping("/coursepic/add")
    public ResponseResult addCoursePic(@PathVariable("courseId") String courseId, @PathVariable("pic") String pic) {
        return courceService.addCoursePic(courseId, pic);
    }

    @Override
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic getFileSystem(@PathVariable("courseId") String courseId) {
        return courceService.getFileSystem(courseId);
    }

    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
        return courceService.deleteCoursePic(courseId);
    }

    @Override
    @GetMapping("/courseview/{id}")
    public CourseView courseview(@PathVariable("id") String id) {
        return courceService.getCourseView(id);
    }

    @Override
    @PostMapping("/preview/{id}")
    public CoursePublishResult preview(@PathVariable("id") String id) {
        return courceService.preview(id);
    }

    @Override
    @PostMapping("/publish/{id}")
    public CoursePublishResult publish(@PathVariable String id) {
        return courceService.publish(id);
    }

    @Override
    @PostMapping("/savmedia")
    public ResponseResult savemedia(@RequestBody TeachplanMedia teachplanMedia) {
        return courceService.savemedia(teachplanMedia);
    }

}
