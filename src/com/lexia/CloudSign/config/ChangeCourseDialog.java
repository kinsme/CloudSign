package com.lexia.CloudSign.config;

import android.app.Dialog;
import android.content.Context;
import android.widget.EditText;
import com.lexia.CloudSign.R;


public class ChangeCourseDialog extends Dialog{
    private String courseName,teacherName;
    public ChangeCourseDialog(Context context, int theme) {
        super(context, theme);
        this.setContentView(R.layout.changecoursedlog);
    }

    public String getCourseName() {
        EditText newcoursename= (EditText) findViewById(R.id.ccoursename);
        courseName=newcoursename.getText().toString();
        return courseName;
    }

    public String getTeacherName() {
        EditText newteachername= (EditText) findViewById(R.id.cteachername);
        teacherName=newteachername.getText().toString();
        return teacherName;
    }
}
