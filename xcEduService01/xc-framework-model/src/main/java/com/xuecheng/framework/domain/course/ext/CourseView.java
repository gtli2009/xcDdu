package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;

public class CourseView implements java.io.Serializable {
    private CourseBase courseBase;
    private CoursePic coursePic;
    private CourseMarket courseMarket;
    private TeachplanNode teachplanNode;

    public CourseBase getCourseBase() {
        return courseBase;
    }

    public void setCourseBase(CourseBase courseBase) {
        this.courseBase = courseBase;
    }

    public CoursePic getCoursePic() {
        return coursePic;
    }

    public void setCoursePic(CoursePic coursePic) {
        this.coursePic = coursePic;
    }

    public CourseMarket getCourseMarket() {
        return courseMarket;
    }

    public void setCourseMarket(CourseMarket courseMarket) {
        this.courseMarket = courseMarket;
    }

    public TeachplanNode getTeachplanNode() {
        return teachplanNode;
    }

    public void setTeachplanNode(TeachplanNode teachplanNode) {
        this.teachplanNode = teachplanNode;
    }
}
