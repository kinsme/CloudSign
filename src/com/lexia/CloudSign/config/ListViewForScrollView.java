package com.lexia.CloudSign.config;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewForScrollView extends ListView implements GestureDetector.OnGestureListener{
    private float length=0;
    private LinearLayout shuaxintitle;
    private ViewGroup.LayoutParams lparams;
    private TextView sxinfo,signnum;
    private GestureDetector gestureDetector; //手势
    private boolean isUpdate=false;
    private boolean update=false;


    //手势操作
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float v, float v1) {
        Log.i("logg","sss");
        Log.i("logg",e2.getY()+" ");
        Log.i("logg",""+e1.getY());
        if ((e2.getY() - e1.getY()) > 0) {
            Log.i("logg","ssssa");
            int length = (int) (e2.getRawY() - e1.getRawY());
            if (length < 150) {
                sxinfo.setText("下拉刷新");
                lparams.height = length;
                isUpdate = false;
                shuaxintitle.setLayoutParams(lparams);
            } else {//刷新控制
                sxinfo.setText("松开刷新...");
                isUpdate = true;
                lparams.height = length;
                shuaxintitle.setLayoutParams(lparams);
            }
            return true;
        }
        return false;
    }
    public void onLongPress(MotionEvent motionEvent) {

    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }


    public void setShuaxintitle(LinearLayout shuaxintitle,ViewGroup.LayoutParams lparams,TextView six,TextView signnum) {
        this.shuaxintitle = shuaxintitle;
        this.lparams=lparams;
        this.sxinfo=six;
        this.signnum=signnum;
    }

    public void setQuery(BmobQuery query) {
        this.query = query;
    }

    private BmobQuery query;

    public void setStudentItems(ArrayList<HashMap<String, String>> studentItems) {
        this.studentItems = studentItems;
    }

    private ArrayList<HashMap<String,String>> studentItems;
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
    private int maxLength=0; //最大向上滑动长度
    private int down,up;

    public ListViewForScrollView(Context context,AttributeSet attrs) {
        super(context,attrs);
        gestureDetector=new GestureDetector(getContext(),this);
        //setOnTouchListener(this);
    }


//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return true;
//    }

    public boolean onTouchEvent(MotionEvent ev) {
        if(length>=0){
            //手势操作
            gestureDetector.onTouchEvent(ev);
        }
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            down= (int) ev.getY();
        }else if(ev.getAction()==MotionEvent.ACTION_UP) {
            lparams.height=1;
            shuaxintitle.setLayoutParams(lparams);
            up = (int) ev.getY();
            if(up-down+length>0-maxLength){
                length=up-down+length;
            }else {
                length=0-maxLength;
            }
           // if(up-down+length>300)
               if(isUpdate){
                Log.i("shuaxin",length+"shuaxin");
                //刷新
                query.findObjects(getContext(), new FindCallback() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
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
                        ((SimpleAdapter)getAdapter()).notifyDataSetChanged();
                        signnum.setText(jsonArray.length()+"");
                        Toast.makeText(getContext(),"更新成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
                isUpdate=false;
                length=0;
            }
        }
        return super.onTouchEvent(ev);
    }

//    @Override
//    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//        Log.i("logg","dsf");
////        if(update==SCROLL_STATE_TOUCH_SCROLL){
////            Log.i("logg","dsf");
////        }
//    }
//
//    @Override
//    public void onScrollStateChanged(AbsListView absListView, int i) {
//        Log.i("logg","dsf");
//        if(i==SCROLL_STATE_TOUCH_SCROLL){
//            Log.i("logg","dsf");
//        }
//    }
    //        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//                MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, expandSpec);
//    }
}
