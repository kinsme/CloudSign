package com.lexia.CloudSign.config;

import android.app.Dialog;
import android.content.Context;
import android.widget.EditText;
import com.lexia.CloudSign.R;

//抽象类实现两种添加窗口
public abstract class AddCourseDialog extends Dialog{
    private String courseName;
    private int coursenameid; //控件id

    public void setCoursenameid(int coursenameid) {
        this.coursenameid = coursenameid;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    protected String teacherName;
    public AddCourseDialog(Context context, int theme,int layout) {
        super(context, theme);
        this.setContentView(layout);
    }

    public String getCourseName() {
        EditText newcoursename= (EditText) findViewById(coursenameid);
        courseName=newcoursename.getText().toString();
        return courseName;
    }

//    public String getTeacherName() {
//        EditText newteachername= (EditText) findViewById(R.id.newteachername);
//        teacherName=newteachername.getText().toString();
//        return teacherName;
//    }
    public abstract String getTeacherName();
}
