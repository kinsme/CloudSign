package com.lexia.CloudSign;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;

import java.util.List;

/**
 * Created by pc123 on 2016/02/15.
 */
public class FindPasswordAty extends Activity {
    private EditText musername,memail;
    private Button submit,returnbtn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpassword);
        musername= (EditText) findViewById(R.id.unamee);
        memail= (EditText) findViewById(R.id.uemaile);
        submit= (Button) findViewById(R.id.submitbtn);
        returnbtn= (Button) findViewById(R.id.rtbtnfp);
        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musername.getText().equals("")||memail.getText().equals("")){
                    Toast.makeText(getApplicationContext(),"不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
                    query.addWhereEqualTo("username", musername.getText().toString());
                    query.findObjects(getApplicationContext(), new FindListener<BmobUser>() {
                        @Override
                        public void onSuccess(List<BmobUser> object) {
                            // TODO Auto-generated method stub
                            //toast("查询用户成功："+object.size());
                            object.get(0).resetPasswordByEmail(getApplicationContext(), memail.getText().toString(), new ResetPasswordByEmailListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(getApplicationContext(),"请到邮箱中找回密码",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(getApplicationContext(),"找回密码失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void onError(int code, String msg) {
                            Toast.makeText(getApplicationContext(),"找回密码失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}
