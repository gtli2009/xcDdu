package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.TeachplanMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator.
 */
public interface TeachPlanMediaRepository extends JpaRepository<TeachplanMedia,String> {
    /**
     * 根据课程id查询列表
     * @param couserId
     * @return
     */
    List<TeachplanMedia> findByCourseId(String couserId);
}
