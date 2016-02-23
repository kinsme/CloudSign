package com.lexia.CloudSign.config;

import android.net.Uri;

import java.io.Serializable;

//±¾µØ´æ´¢
public class UserInfoSF implements Serializable {
    String username="";
    String usertrname="";
    String schoolName="";
    String academyName="";
    String className="";
    String studentid="";
    String phoneNum="";
    String btAddres="";
    String email="";
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getUsertrname() {
        return usertrname;
    }

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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUsertrname(String usertrname) {
        this.usertrname = usertrname;
    }

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
}
