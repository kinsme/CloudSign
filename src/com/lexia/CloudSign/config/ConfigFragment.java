package com.lexia.CloudSign.config;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import com.google.gson.Gson;
import com.lexia.CloudSign.*;

import java.io.*;
import java.util.concurrent.ExecutorService;


public class ConfigFragment extends Fragment {
    private final int PICK_PIC=123;
    private String userFilename,usericon="myicon";
    private RegUser regUser;  //学生用户
    private View view;
    private ImageButton sxbtn;
    private Animation anim;
    private ImageView myicon;
    private TextView mybtid,username,userrealname,schoolname,academyname,classname,studentid,phonenum;
    private UserInfoSF userInfoSF=new UserInfoSF();
    private Button configinfo;
    private  String ResPATH;
    private File iconfile;
    private float scale;
    private int iconheight;
    private ExecutorService singleUpdateThread;  //单线程池来自主线程
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.configfragment,container,false);
        username= (TextView) view.findViewById(R.id.usernamef);
        userrealname= (TextView) view.findViewById(R.id.userrealnamef);
        mybtid= (TextView) view.findViewById(R.id.mybtid);
        configinfo= (Button) view.findViewById(R.id.configinfo);
        schoolname= (TextView) view.findViewById(R.id.schoolname);
        academyname= (TextView) view.findViewById(R.id.acadmyname);
        classname= (TextView) view.findViewById(R.id.classname);
        studentid= (TextView) view.findViewById(R.id.studentid);
        phonenum= (TextView) view.findViewById(R.id.phonenum);
        myicon= (ImageView) view.findViewById(R.id.myicon);
        singleUpdateThread=((UserPageAty)getActivity()).getSingleUpdateThread(); //获取线程

        regUser= BmobUser.getCurrentUser(getActivity(),RegUser.class);
        userFilename=regUser.getUsername();
        LinearLayout linearic= (LinearLayout) view.findViewById(R.id.linearic);
        scale=getActivity().getResources().getDisplayMetrics().density;
        iconheight=(int)(scale*60+0.5f);
        setUserIcon();
        //从网络端下载
        if(!getUserInfo()){
            Log.i("infoss","test");
            downloadInServer();
        }
        initView();
        return view;
    }

    private void setUserIcon() {
        //文件夹
        ResPATH = getActivity().getFilesDir().getPath().toString() +"/"+userFilename+"icons/";
        File dirFile=new File(ResPATH);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        iconfile = new File(ResPATH+usericon + ".jpg");
        if (!iconfile.exists()) {
            try {
                iconfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.i("first", "first" + iconfile.getAbsolutePath());
//            try {
//                BufferedInputStream bis=new BufferedInputStream(new FileInputStream(iconfile));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
            //Bitmap缩略图
            loadBitmapSl();
        }

    }
    private void loadBitmapSl(){
        BitmapFactory.Options opts=new BitmapFactory.Options();
        opts.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(iconfile.getAbsolutePath(), opts);
        opts.inSampleSize=opts.outHeight/iconheight;
        opts.inJustDecodeBounds = false;
        Bitmap bmp=BitmapFactory.decodeFile(iconfile.getAbsolutePath(),opts);
        if(bmp!=null){
            myicon.setImageBitmap(bmp); }
    }

    private void initView() {
        username.setText("账号："+userInfoSF.getUsername());
        if(regUser.getCareer().equals("学生")){
          userrealname.setText(userInfoSF.getUsertrname()+"学生");
        }
        else{
          userrealname.setText(userInfoSF.getUsertrname()+"老师");
        }
        schoolname.setText(userInfoSF.getSchoolName());
        academyname.setText(userInfoSF.getAcademyName());
        classname.setText(userInfoSF.getClassName());
        studentid.setText(userInfoSF.getStudentid());
        mybtid.setText(userInfoSF.getBtAddres());
        phonenum.setText(userInfoSF.getPhoneNum());

        if(schoolname.getText()!=""&&academyname.getText()!=""&&classname.getText()!=""&&
                studentid.getText()!="")
        {
            configinfo.setText("");
        }
        else {
            configinfo.setText("点击完善资料");
        }
    }

    private boolean getUserInfo() {
        SharedPreferences usersp=getActivity().getSharedPreferences(userFilename+"info",getActivity().MODE_PRIVATE);
        Gson gson=new Gson();
        String usergson=usersp.getString("thisuser", null);
        if(usergson==null){
            return false;
        }
        else{
            userInfoSF=gson.fromJson(usersp.getString("thisuser", null), UserInfoSF.class);
            return true;
        }
    }

    @Override
    public void onStop() {
        Log.i("stoped","config");
        //本地存储
        SaveUserInfo();
        //网络存储 fragments只能同时用一个线程更新 可关闭course线程，此处使用线程池
        Thread configthread=new Thread(){
            @Override
            public void run() {
                super.run();
                updateServerData();
            }
        };
        singleUpdateThread.execute(configthread);
        super.onStop();
    }
    private void SaveUserInfo() {
        SharedPreferences usersp=getActivity().getSharedPreferences(userFilename+"info",getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor=usersp.edit();
        Gson gson=new Gson();
        String user= gson.toJson(userInfoSF);
        editor.remove("thisuser");
        editor.putString("thisuser",user);
        editor.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PICK_PIC){
            new Thread(){
                @Override
                public void run() {
                    try {
                        Uri uri= data.getData();
                        Bitmap bitmap;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(iconfile));
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                            bos.flush();
                            bos.close();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadBitmapSl();
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }
        if(resultCode==211){
            Bundle bundle=data.getBundleExtra("newinfol");
            userInfoSF= (UserInfoSF) bundle.getSerializable("newinfo");
            initView();

        }
//        if(resultCode==getActivity().RESULT_OK){
//            Log.i("kaike","na");
//        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //动画
        anim=AnimationUtils.loadAnimation(getActivity(),R.anim.tip);
        anim.setInterpolator(new LinearInterpolator());
        view.findViewById(R.id.quitbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser.logOut(getActivity());
                startActivity(new Intent(getActivity(),LoginActivity.class));
            }
        });
        sxbtn=(ImageButton) view.findViewById(R.id.shuaxinbtn);
        sxbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sxbtn.startAnimation(anim);
                BluetoothAdapter mAdapter=BluetoothAdapter.getDefaultAdapter();
                mybtid.setText(mAdapter.getAddress());
                userInfoSF.setBtAddres(mAdapter.getAddress());
//
//                if(!mAdapter.isEnabled()){
//                    Intent enabler=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(enabler, 22);
//                }
            }
        });
        //BmobUser.getCurrentUser(getActivity())!=null &&
        if(userInfoSF.getUsername()==""||userInfoSF.getEmail()==""){
            userInfoSF.setUsername(regUser.getUsername());
            userInfoSF.setUsertrname(regUser.getName());
            userInfoSF.setEmail(regUser.getEmail());
            username.setText("账号："+regUser.getUsername());
            userrealname.setText(regUser.getName());
        }
        configinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getActivity(), ConfigInformation.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("info",userInfoSF);
                intent.putExtra("userinfo",bundle);
                startActivityForResult(intent, getActivity().RESULT_FIRST_USER);
            }
        });

        myicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickIntent=new Intent(Intent.ACTION_PICK,null);
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(pickIntent,PICK_PIC);
            }
        });

    }
    void updateServerData(){
        RegUser updateUser=new RegUser();
        updateUser.setStudentid(userInfoSF.getStudentid());
        updateUser.setSchoolName(userInfoSF.getSchoolName());
        updateUser.setAcademyName(userInfoSF.getAcademyName());
        updateUser.setClassName(userInfoSF.getClassName());
        updateUser.setBtAddres(userInfoSF.getBtAddres());
        updateUser.setPhoneNum(userInfoSF.getPhoneNum());
        updateUser.update(getActivity(), regUser.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.i("updates","updateSuccess!");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i("updates","updatefailed!"+s);
            }
        });

    }
    //第一次登陆，从网络端读取
    void downloadInServer(){
//        if (!getActivity().getSharedPreferences(userFilename+"info",getActivity().MODE_PRIVATE).contains("thisuser")){
        userInfoSF.setStudentid(regUser.getStudentid());
        userInfoSF.setSchoolName(regUser.getSchoolName());
        userInfoSF.setBtAddres(regUser.getBtAddres());
        userInfoSF.setClassName(regUser.getClassName());
        userInfoSF.setAcademyName(regUser.getAcademyName());

    }
}
