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
//��ʾǩ��

public class SigninList extends Activity {
    private float scale;
    private ArrayList<HashMap<String,String>> studentItems=new ArrayList<>();
    private SimpleAdapter studentAdapter;
    private String courseId;
    private ListViewForScrollView listsign;
    private LinearLayout mainlayout,shuaxintitle;
    private GestureDetector gestureDetector; //����
    private ViewGroup.LayoutParams lparams;
    private TextView sxinfo,signnum;
    private boolean isUpdate=false;
    private  BmobQuery query; //ǩ����
    private int screenHeight;
    private float length=0;
    private int maxLength=0; //������ϻ�������
    private int down,up;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signinlist);
        scale =getResources().getDisplayMetrics().density;
        mainlayout= (LinearLayout) findViewById(R.id.mainlayout);
        signnum= (TextView) findViewById(R.id.signnum);
        shuaxintitle= (LinearLayout) findViewById(R.id.shuaxintitle); //ˢ����
        lparams=shuaxintitle.getLayoutParams();
        sxinfo= (TextView) findViewById(R.id.sxinfo);
        Intent intent=getIntent();
        courseId=intent.getStringExtra("courseid");
        query = new BmobQuery("s"+courseId);  //ǩ����
        listsign= (ListViewForScrollView) findViewById(R.id.listsign);
        //����
        listsign.setQuery(query);
        listsign.setShuaxintitle(shuaxintitle,lparams,sxinfo,signnum);

        initView();
        new Thread(){
            @Override
            public void run() {
                super.run();
                //���ض�ȡ
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
                        sxinfo.setText("����ˢ��");
                        lparams.height=length;
                        isUpdate=false;
                        shuaxintitle.setLayoutParams(lparams);
                    }
                    else{//ˢ�¿���
                        sxinfo.setText("�ɿ�ˢ��...");
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
                //̧��ʼˢ��
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    if(!isUpdate){
                    lparams.height=1;
                    shuaxintitle.setLayoutParams(lparams);
                    }
                    else {//ˢ��
                        query.findObjects(getApplicationContext(), new FindCallback() {
                            @Override
                            public void onSuccess(JSONArray jsonArray) {
                                getItemFromServer();
                                lparams.height=1;
                                shuaxintitle.setLayoutParams(lparams);
                                Toast.makeText(getApplicationContext(),"���³ɹ�",Toast.LENGTH_SHORT).show();
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
    //���ض�ȡ
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
    //�����ȡ
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
        //��󻬶�����
        int everyitem=(int) (50 * scale + 0.5f);  //ÿ���б���
        int listheight=everyitem*size;
        int maxLength=listheight-(screenHeight-everyitem*2);
        return maxLength;
    }

}

