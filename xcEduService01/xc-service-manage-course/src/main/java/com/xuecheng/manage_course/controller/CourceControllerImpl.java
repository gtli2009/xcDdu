package com.xuecheng.manage_course.controller;

import com.xuecheng.api.cource.CourceControllerApi;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourceControllerImpl  implements CourceControllerApi{
    @Autowired
    private CourceService courceService;
    @Override
    @GetMapping("/teachplan/list/{courceId}")
    public TeachplanNode findTechPlanList( @PathVariable("courceId") String courceId) {
        return courceService.selectLisst(courceId);
    }

    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachPlan( @RequestBody Teachplan teachplan) {
        return courceService.addTeachPlan(teachplan);
    }

    @Override
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult findCourceList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        return courceService.findCourceList(page,size,queryPageRequest);
    }
}
