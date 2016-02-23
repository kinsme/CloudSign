package com.lexia.CloudSign;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import cn.bmob.v3.listener.SaveListener;

public class RegisterAty extends Activity {

    private RegUser newuser=new RegUser();
    private EditText newusername,newuserpsword,newname,newemail,newage;
    private RadioGroup rcareer;
    private RadioButton careerchecked;
    private String career="";
    private int j=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regiseteraty);
        findViewById(R.id.returnbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        newusername= (EditText) findViewById(R.id.rusernamem);
        newuserpsword= (EditText) findViewById(R.id.ruserpassword);
        newname= (EditText) findViewById(R.id.rname);
        newemail= (EditText) findViewById(R.id.remail);
        newage= (EditText) findViewById(R.id.rage);
        rcareer= (RadioGroup) findViewById(R.id.careerid);

        rcareer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id=radioGroup.getCheckedRadioButtonId();
                careerchecked= (RadioButton) RegisterAty.this.findViewById(id);
                career=careerchecked.getText().toString();
            }
        });

        Spinner spner= (Spinner) findViewById(R.id.rsex);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        adapter.add("男");
        adapter.add("女");
        spner.setAdapter(adapter);

        spner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String sex = adapter.getItem(i);
                newuser.setSex(sex);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        findViewById(R.id.rbtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(newusername.getText().toString().equals("")||newuserpsword.getText().toString().equals("")){
                    Toast.makeText(RegisterAty.this,"用户名/密码不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(career==""){
                    Toast.makeText(RegisterAty.this,"职位不能为空",Toast.LENGTH_SHORT).show();
                }
                else {
                newuser.setUsername(newusername.getText().toString());
                newuser.setPassword(newuserpsword.getText().toString());
                newuser.setAge(new Integer(newage.getText().toString()));
                newuser.setEmail(newemail.getText().toString());
                newuser.setName(newname.getText().toString());
                Log.i("checked",careerchecked.getText().toString());
                newuser.setCareer(career);
                newuser.signUp(RegisterAty.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(RegisterAty.this,"注册成功",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterAty.this, LoginActivity.class));
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(RegisterAty.this,"注册失败"+s,Toast.LENGTH_SHORT).show();
                    }
                });
            }
            }
        });

    }
}
