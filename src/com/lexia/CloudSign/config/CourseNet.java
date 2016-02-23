package com.lexia.CloudSign.config;
//网络端存储所有老师开设的课程
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;
import com.lexia.CloudSign.RegUser;

public class CourseNet extends BmobObject{
    private String courseName;  //老师开设的课程名
    private String teacherBtAddress; //老师蓝牙名
    private RegUser teacher;     //课程老师(一对一关系)
    private BmobRelation students;  //选课的所有学生（多对多关系）
    private boolean isable=false;   //课程是否正在上（可签到）
    public boolean getIsable() {
        return isable;
    }

    public void setIsable(boolean isable) {
        this.isable = isable;
    }
    public BmobRelation getStudents() {
        return students;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTeacherBtAddress() {
        return teacherBtAddress;
    }

    public RegUser getTeacher() {
        return teacher;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setTeacherBtAddress(String teacherBtAddress) {
        this.teacherBtAddress = teacherBtAddress;
    }

    public void setTeacher(RegUser teacher) {
        this.teacher = teacher;
    }

    public void setStudents(BmobRelation students) {
        this.students = students;
    }
}
