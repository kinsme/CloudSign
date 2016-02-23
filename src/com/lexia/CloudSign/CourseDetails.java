package com.lexia.CloudSign;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import com.lexia.CloudSign.config.CourseNet;
import com.lexia.CloudSign.config.NewCourse;

import java.util.List;

public class CourseDetails extends Activity{
    private TextView coursename,courseteacher,coursetime,title,teacherbt,classmate;
    private Button signinbtn;
    private boolean isOpen=false;
    private NewCourse Course;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursedetails);
        Intent intent= getIntent();
        Bundle bundle=intent.getBundleExtra("coursename");
        Course= (NewCourse) bundle.getSerializable("course");
        title= (TextView) findViewById(R.id.coursetitle);
        coursename= (TextView) findViewById(R.id.coursedetailname);
        courseteacher= (TextView) findViewById(R.id.coursedetailteacher);
        coursetime= (TextView) findViewById(R.id.coursedetailtime);
        teacherbt= (TextView) findViewById(R.id.bluetooth);
        classmate= (TextView) findViewById(R.id.classmates);
        signinbtn= (Button) findViewById(R.id.signinthis);
        title.setText(Course.getName());
        coursename.setText(Course.getName());
        courseteacher.setText(Course.getTeachername());
        coursetime.setText(String.format("星期%d 第%d节",Course.getXtime()+1,Course.getYtime()+1));
        findViewById(R.id.retbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        new Thread(){
            @Override
            public void run() {
                super.run();
                BmobQuery<CourseNet> quary=new BmobQuery<>();
                quary.getObject(getApplicationContext(), Course.getObjectID(), new GetListener<CourseNet>() {
                    @Override
                    public void onSuccess(CourseNet courseNet) {
                        //Log.i("qiandao",courseNet.getStudents().getObjects().size()+Course.getObjectID());
                        //classmate.setText("共"+courseNet.getStudents().getObjects().size()+"位同学");
                        if(courseNet.getIsable()){
                            isOpen=true;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    teacherbt.setText("已打开");
                                }
                            });
                        }

                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }
        }.start();
        BmobQuery<RegUser> query1 = new BmobQuery<RegUser>();
        CourseNet course=new CourseNet();
        course.setObjectId(Course.getObjectID());
//likes是Post表中的字段，用来存储所有喜欢该帖子的用户
        query1.addWhereRelatedTo("students", new BmobPointer(course));
        query1.findObjects(this, new FindListener<RegUser>() {

            public void onSuccess(List<RegUser> object) {
                // TODO Auto-generated method stub
                Log.i("life", "查询个数："+object.size());
                classmate.setText("共"+object.size()+"位同学");
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Log.i("life", "查询失败："+code+"-"+msg);
            }
        });
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen){
                    setResult(222);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"未在上课",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
