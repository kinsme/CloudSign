package com.lexia.CloudSign;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import com.lexia.CloudSign.config.MyProgressDialog;


public class LoginActivity extends Activity {
    final int HANDLER_SUCCSE = 1;
    final int HANDLER_FAIL = 0;
    int messagein;

    private Button loginbtn,registerbtn,forgetpw;
    private EditText nametext,passwordtext;
    private MyProgressDialog mylogindialog;
    private BmobUser mUser=new BmobUser();
    Handler myhandler=new Handler(){
        public void handleMessage(Message msg) {
            //Log.i("message what", aa + msg.what);
            switch (msg.what){
                case HANDLER_FAIL:
                    Toast.makeText(LoginActivity.this, "用户名/密码错误", Toast.LENGTH_SHORT).show();
                    mylogindialog.dismiss();
                    break;
                case HANDLER_SUCCSE:
                    mylogindialog.setMessage("登录中");
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    mylogindialog.dismiss();
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, UserPageAty.class));
                    break;
            }
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginaty);

        Bmob.initialize(this, "e2e8e2bac9e3260a99539260c89f9217");
        if(BmobUser.getCurrentUser(this)!=null){
            startActivity(new Intent(LoginActivity.this, UserPageAty.class));
        }
        loginbtn= (Button) findViewById(R.id.loginbtn);
        registerbtn= (Button) findViewById(R.id.registerbtn);
        forgetpw= (Button) findViewById(R.id.forgetpw);
        nametext= (EditText) findViewById(R.id.usernamem);
        passwordtext= (EditText) findViewById(R.id.userpassword);

        forgetpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,FindPasswordAty.class));
            }
        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterAty.class));
            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = nametext.getText().toString();
                String password = passwordtext.getText().toString();
                if(username.equals("")||password.equals("")){
                    Toast.makeText(LoginActivity.this,"用户名/密码不能为空",Toast.LENGTH_SHORT).show();
                }
                else{
                    //ProgressDialog prog=ProgressDialog.show(LoginActivity.this,"提示","正在登陆中");
                    mylogindialog = new MyProgressDialog(LoginActivity.this, R.style.ProgressDialogStyle);
                    mylogindialog.setMessage("正在验证...");
                    mylogindialog.show();
                    mUser.setUsername(username);
                    mUser.setPassword(password);

                    userLogin();
//                            Message msg0=myhandler.obtainMessage();
////                            msg0.what=messagein;
////                            myhandler.sendMessage(msg0);

                }
            }
        });

    }
    private void userLogin(){
        mUser.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                messagein=HANDLER_SUCCSE;
                //myhandler.sendMessage();
                myhandler.sendEmptyMessage(HANDLER_SUCCSE);
            }

            @Override
            public void onFailure(int i, String s) {
                messagein=HANDLER_FAIL;
                myhandler.sendEmptyMessage(HANDLER_FAIL);
            }
        });
    }
    public void onBackPressed() {
        //super.onBackPressed();
        this.onStop();
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }
}

