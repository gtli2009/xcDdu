package com.xuecheng.manage_course.controller;

import com.xuecheng.api.cource.SysDictionaryControlerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_course.service.CourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/dictionary")
public class SysDictionaryControllerImpl implements SysDictionaryControlerApi {
    @Autowired
    private CourceService  courceService;
    @Override
    @GetMapping("/get/{dType}")
    public SysDictionary getByType(@PathVariable("dType") String dType) {
         return courceService.findByType(dType);
    }
}
