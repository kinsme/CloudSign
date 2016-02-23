package com.lexia.CloudSign;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import cn.bmob.v3.BmobUser;
import com.lexia.CloudSign.config.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UserPageAty extends FragmentActivity implements View.OnClickListener{
    private int screenwidth;
    private ViewPager mviewpager;
    private RegUser regUser;
    private boolean coursesUpdate=false;
    private MyFragmentAdapter madapter;
    private ArrayList<Fragment> arrayfragment=new ArrayList<Fragment>();
    private Button coursebtn,signinbtn,configbtn,quicksign;
    private CourseFragment coursepage;
    private ExecutorService singleUpdateThread= Executors.newSingleThreadExecutor();  //更新用户数据线程

    public ExecutorService getSingleUpdateThread() {
        return singleUpdateThread;
    }
    public boolean isCoursesUpdate() {
        return coursesUpdate;
    }
    public void setCoursesUpdate(boolean coursesUpdate) {
        this.coursesUpdate = coursesUpdate;
    }
    public ViewPager getMviewpager() {
        return mviewpager;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.museraty);
        regUser=BmobUser.getCurrentUser(this, RegUser.class);
        initView();
        DisplayMetrics dm = new DisplayMetrics();
        screenwidth=this.getWindowManager().getDefaultDisplay().getWidth();
        Bundle swidth=new Bundle();
        swidth.putInt("width", screenwidth);
        coursepage.setArguments(swidth);

        coursebtn.setOnClickListener(this);
        signinbtn.setOnClickListener(this);
        configbtn.setOnClickListener(this);
        quicksign.setOnClickListener(this);
        mviewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                resetTextView();
                switch (i){
                    case 0:
                        //(CourseFragment)getSupportFragmentManager().findFragmentById(0).
                        coursebtn.setTextColor(Color.BLACK);
                        coursebtn.setBackgroundColor(Color.WHITE);
                        break;
                    case 1:
                        signinbtn.setTextColor(Color.BLACK);
                        signinbtn.setBackgroundColor(Color.WHITE);
                        break;
                    case 2:
                        configbtn.setTextColor(Color.BLACK);
                        configbtn.setBackgroundColor(Color.WHITE);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        resetTextView();
        //madapter.notifyDataSetChanged();
        switch (view.getId()){
            case R.id.coursebtn:
                coursebtn.setTextColor(Color.BLACK);
                coursebtn.setBackgroundColor(Color.WHITE);
                mviewpager.setCurrentItem(0);
                break;
            case R.id.signinbtn:
                signinbtn.setTextColor(Color.BLACK);
                signinbtn.setBackgroundColor(Color.WHITE);
                mviewpager.setCurrentItem(1);
                break;
            case R.id.configbtn:
                configbtn.setTextColor(Color.BLACK);
                configbtn.setBackgroundColor(Color.WHITE);
                mviewpager.setCurrentItem(2);
                break;
            case R.id.quicksign:
                signinbtn.setTextColor(Color.BLACK);
                signinbtn.setBackgroundColor(Color.WHITE);
                mviewpager.setCurrentItem(1);
                break;
        }
    }

    private void resetTextView() {
        coursebtn.setTextColor(Color.WHITE);
        signinbtn.setTextColor(Color.WHITE);
        configbtn.setTextColor(Color.WHITE);
        coursebtn.setBackgroundColor(this.getResources().getColor(R.drawable.dgray));
        signinbtn.setBackgroundColor(this.getResources().getColor(R.drawable.dgray));
        configbtn.setBackgroundColor(this.getResources().getColor(R.drawable.dgray));
    }

    private void initView() {
        mviewpager= (ViewPager) findViewById(R.id.mviewpager);
        coursebtn= (Button) findViewById(R.id.coursebtn);
        coursebtn.setTextColor(Color.BLACK);
        coursebtn.setBackgroundColor(Color.WHITE);
        signinbtn= (Button) findViewById(R.id.signinbtn);
        configbtn= (Button) findViewById(R.id.configbtn);
        quicksign= (Button) findViewById(R.id.quicksign);
        if(regUser.getCareer().equals("教师")){
            quicksign.setVisibility(View.GONE);
        }
        coursepage=new CourseFragment();
        arrayfragment.add(coursepage);
        arrayfragment.add(new SigninFragment());
        arrayfragment.add(new ConfigFragment());
        madapter=new MyFragmentAdapter(getSupportFragmentManager(),this);
//        madapter.setOnReloadListener(new MyFragmentAdapter.OnReloadListener() {
//            @Override
//            public void onReload() {
//                arrayfragment=null;
//                ArrayList<Fragment> list=new ArrayList<Fragment>();
//                list.add(new CourseFragment());
//                list.add(new SigninFragment());
//                list.add(new ConfigFragment());
//                madapter.setPagerItems(list);
//            }
//        });
        madapter.setFragments(arrayfragment);
        mviewpager.setAdapter(madapter);
        mviewpager.setOffscreenPageLimit(3);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(1,1,0,"注销登陆");
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
         super.onMenuItemSelected(featureId, item);
         if(item.getItemId()==1){
             BmobUser.logOut(this);
             startActivity(new Intent(UserPageAty.this,LoginActivity.class));
         }
        return true;
    }

    @Override
    protected void onStop() {
        Log.i("stoped","activity");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("destory","daty");
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        this.onStop();
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }
    public MyFragmentAdapter getMadapter(){
        return madapter;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
//        switch (resultCode) {
//            case 211:
//                Log.i("result", "cbbbb");
//                Bundle bundle = data.getBundleExtra("newinfol");
//                UserInfoSF userInfoSF = (UserInfoSF) bundle.getSerializable("newinfo");
//                //initView();
//                break;
//            default:
//                break;
//        }
    }
}
