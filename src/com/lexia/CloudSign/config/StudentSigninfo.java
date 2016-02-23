package com.lexia.CloudSign.config;

import cn.bmob.v3.BmobObject;

public class StudentSigninfo extends BmobObject{
    private String studentname;
    private String btAddress;
    private String studentid;
    public StudentSigninfo(String tablename){
        this.setTableName(tablename);
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public void setBtAddress(String btAddress) {
        this.btAddress = btAddress;
    }

    public String getStudentname() {
        return studentname;
    }

    public String getBtAddress() {
        return btAddress;
    }
}
