package com.lexia.CloudSign;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.*;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import com.lexia.CloudSign.config.ListViewForScrollView;
import com.lexia.CloudSign.config.MySXGestureDetector;
import com.lexia.CloudSign.config.StudentSigninfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
//显示签到

public class SigninList extends Activity {
    private float scale;
    private ArrayList<HashMap<String,String>> studentItems=new ArrayList<>();
    private SimpleAdapter studentAdapter;
    private String courseId;
    private ListViewForScrollView listsign;
    private LinearLayout mainlayout,shuaxintitle;
    private GestureDetector gestureDetector; //手势
    private ViewGroup.LayoutParams lparams;
    private TextView sxinfo,signnum;
    private boolean isUpdate=false;
    private  BmobQuery query; //签到表
    private int screenHeight;
    private float length=0;
    private int maxLength=0; //最大向上滑动长度
    private int down,up;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signinlist);
        scale =getResources().getDisplayMetrics().density;
        mainlayout= (LinearLayout) findViewById(R.id.mainlayout);
        signnum= (TextView) findViewById(R.id.signnum);
        shuaxintitle= (LinearLayout) findViewById(R.id.shuaxintitle); //刷新条
        lparams=shuaxintitle.getLayoutParams();
        sxinfo= (TextView) findViewById(R.id.sxinfo);
        Intent intent=getIntent();
        courseId=intent.getStringExtra("courseid");
        query = new BmobQuery("s"+courseId);  //签到表
        listsign= (ListViewForScrollView) findViewById(R.id.listsign);
        //传参
        listsign.setQuery(query);
        listsign.setShuaxintitle(shuaxintitle,lparams,sxinfo,signnum);

        initView();
        new Thread(){
            @Override
            public void run() {
                super.run();
                //本地读取
                getItemFromPath();
            }
        }.start();

        gestureDetector=new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {

                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float v, float v1) {
                if ((e2.getRawY() - e1.getRawY()) > 0) {
                    int length= (int) (e2.getRawY() - e1.getRawY());
                    if(length<150){
                        sxinfo.setText("下拉刷新");
                        lparams.height=length;
                        isUpdate=false;
                        shuaxintitle.setLayoutParams(lparams);
                    }
                    else{//刷新控制
                        sxinfo.setText("松开刷新...");
                        isUpdate=true;
                        lparams.height=length;
                        shuaxintitle.setLayoutParams(lparams);
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {

                return false;
            }
        });
//        gestureDetector=new GestureDetector(this,new MySXGestureDetector(shuaxintitle,lparams,sxinfo,isUpdate));
        mainlayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                //抬起开始刷新
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    if(!isUpdate){
                    lparams.height=1;
                    shuaxintitle.setLayoutParams(lparams);
                    }
                    else {//刷新
                        query.findObjects(getApplicationContext(), new FindCallback() {
                            @Override
                            public void onSuccess(JSONArray jsonArray) {
                                getItemFromServer();
                                lparams.height=1;
                                shuaxintitle.setLayoutParams(lparams);
                                Toast.makeText(getApplicationContext(),"更新成功",Toast.LENGTH_SHORT).show();
                                isUpdate=false;
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(getApplicationContext()
                                        ,s,Toast.LENGTH_SHORT).show();
                                lparams.height=1;
                                isUpdate=false;
                                shuaxintitle.setLayoutParams(lparams);
                            }
                        });
                    }
                }
                return true;
            }
        });
    }
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            Rect outRect = new Rect();
            getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
            screenHeight=outRect.height();
        }

    }
    //本地读取
    private void getItemFromPath(){
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("s" + courseId + "sp", getApplicationContext().MODE_PRIVATE);
        try {
            JSONArray jsonArray=new JSONArray(sharedPreferences.getString("s" + courseId, "[]"));
            int length=sharedPreferences.getInt("number",0);
            signnum.setText(length+"");
            readInfoFromJson(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //网络读取
    private void getItemFromServer() {
        query.findObjects(this, new FindCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                readInfoFromJson(jsonArray);
                signnum.setText(jsonArray.length()+"");
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
    private void readInfoFromJson(JSONArray jsonArray){
        studentItems.clear();
        for(int i=0;i<jsonArray.length();i++){
            try {
                JSONObject students=jsonArray.getJSONObject(i);
                HashMap<String,String> info=new HashMap<String, String>();
                info.put("studentid",students.getString("studentid"));
                info.put("name", students.getString("studentname"));
                info.put("studentbt",students.getString("btAddress"));
                studentItems.add(info);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        studentAdapter.notifyDataSetChanged();
        listsign.setMaxLength(calculateMaxLength(jsonArray.length()));
        //maxLength=calculateMaxLength(jsonArray.length());
    }
    private void initView() {
        HashMap<String,String> info=new HashMap<String, String>();
        info.put("studentid","");
        info.put("name", "");
        info.put("studentbt","");
        studentItems.add(info);
        studentAdapter=new SimpleAdapter(this,studentItems,R.layout.liststudentitem,new String[]{"studentid","name","studentbt"},
                new int[]{R.id.studentinfo,R.id.studentname,R.id.studentbt});
        listsign.setAdapter(studentAdapter);
        listsign.setStudentItems(studentItems);
    }
    private int calculateMaxLength(int size){
        //最大滑动距离
        int everyitem=(int) (50 * scale + 0.5f);  //每个列表长度
        int listheight=everyitem*size;
        int maxLength=listheight-(screenHeight-everyitem*2);
        return maxLength;
    }

}

