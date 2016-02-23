package com.lexia.CloudSign.config;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MySXGestureDetector implements GestureDetector.OnGestureListener {
    private LinearLayout shuaxintitle;
    private ViewGroup.LayoutParams lparams;
    private TextView sxinfo;
    private boolean isUpdate=false;

    public MySXGestureDetector(LinearLayout shuaxintitle,ViewGroup.LayoutParams lparams,TextView sxinfo) {
      this.shuaxintitle=shuaxintitle;
        this.lparams=lparams;
        this.sxinfo=sxinfo;
    }

    @Override
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
            Log.i("logg","ssss");
            int length = (int) (e2.getRawY() - e1.getRawY());
            if (length < 150) {
                sxinfo.setText("下拉刷新");
                lparams.height = length;
                isUpdate = false;
                shuaxintitle.setLayoutParams(lparams);
            } else {//刷新控制
                sxinfo.setText("正在刷新...");
                isUpdate = true;
                lparams.height = 150;
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
}
