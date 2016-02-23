package com.lexia.CloudSign.config;

//新课程信息类

import java.io.Serializable;


public class NewCourse implements Serializable{
    private String name;

    public String getTeacherBt() {
        return teacherBt;
    }

    public void setTeacherBt(String teacherBt) {

        this.teacherBt = teacherBt;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {

        return color;
    }

    private int color=-16711936;
    private String teachername="";  //课程老师名
    private String objectID="";
    private String teacherBt="";      //老师蓝牙
    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }
    private int xlocation;
    private int ylocation;

    public int getYtime() {
        return ytime;
    }

    public int getXtime() {

        return xtime;
    }

    private int xtime;
    private int ytime;  //课程时间
    private int frameId=0;

    private int Id=0;

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getTeachername() {

        return teachername;
    }

    public int getFrameId() {
        return frameId;
    }

    public int getId() {
        return Id;
    }

    public void generateId() {
//        char[] temp=name.toCharArray();
//        if(name.length()!=0){
//            for(int i=0;i<temp.length;i++)
//            Id = Id+(int)temp[i];
//        }
         Id=xlocation*1000+ylocation;
        this.frameId = xlocation*10000+ylocation;
    }



    public NewCourse(String name) {
        this.name = name;
        //this.teachername=teachername;
    }
    public void generateTime(int day,int classes){
        this.xtime=day;
        this.ytime=classes;
    }

    public String getName() {
        return name;
    }

    public int getXlocation() {
        return xlocation;
    }

    public int getYlocation() {
        return ylocation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setXlocation(int xlocation) {
        this.xlocation = xlocation;
    }

    public void setYlocation(int ylocation) {
        this.ylocation = ylocation;
    }
}
