package com.lexia.CloudSign;

import cn.bmob.v3.BmobUser;

import java.util.ArrayList;
import java.util.List;


public class RegUser extends BmobUser {
    private String sex;
    private Integer age;
    private String name;
    private String career;

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {

        this.courses = courses;
    }

    private List<String> courses;

    public String getSchoolName() {
        return schoolName;
    }

    public String getAcademyName() {
        return academyName;
    }

    public String getClassName() {
        return className;
    }

    public String getStudentid() {
        return studentid;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getBtAddres() {
        return btAddres;
    }

    private String schoolName="",academyName="",className="",studentid="",phoneNum="",btAddres="";
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setAcademyName(String academyName) {
        this.academyName = academyName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setBtAddres(String btAddres) {
        this.btAddres = btAddres;
    }




    public void setCareer(String career) {
        this.career = career;
    }

    public String getCareer() {
        return career;
    }



    public void setName(String name) {
        this.name = name;
    }

    public String getName() {

        return name;
    }

    public String isSex() {
        return sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
