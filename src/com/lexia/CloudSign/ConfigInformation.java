package com.lexia.CloudSign;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import com.lexia.CloudSign.config.UserInfoSF;

public class ConfigInformation extends Activity{
    private EditText editSchool,editClass,editAcademy,editStid,eidtphonenum;
    private UserInfoSF info;
    private Button changepwbtn;
    private TextView emailtext,pnumtext;
    private Context mContext;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configinfomation);
        mContext=this;
        Intent intent= getIntent();
        Bundle bundle=intent.getBundleExtra("userinfo");
        info= (UserInfoSF) bundle.getSerializable("info");
        //��ʼ��
        editSchool= (EditText) findViewById(R.id.editschool);
        editAcademy= (EditText) findViewById(R.id.editacademy);
        editStid= (EditText) findViewById(R.id.editstid);
        editClass= (EditText) findViewById(R.id.editclass);
        eidtphonenum= (EditText) findViewById(R.id.phonenumedit);
        emailtext= (TextView) findViewById(R.id.emailte);
        pnumtext= (TextView) findViewById(R.id.editpnum);
        changepwbtn= (Button) findViewById(R.id.changepwbtn);
        //�޸�����
        changepwbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog=new AlertDialog.Builder(mContext)
                        .setTitle("��ʾ")
                        .setMessage("ȷ���޸�����")
                        .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                RegUser regUser= BmobUser.getCurrentUser(mContext, RegUser.class);
                                regUser.resetPasswordByEmail(mContext, regUser.getEmail(), new ResetPasswordByEmailListener() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(mContext,"�����������޸�",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Toast.makeText(mContext,"�޸ĳ���",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("ȡ��", null).show();
            }
        });
        //���ֻ�
        Button change= (Button) findViewById(R.id.changepnum);
        change.setText("�޸�");
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(change.getText()=="�޸�"){
                    change.setText("����");
                    pnumtext.setVisibility(View.GONE);
                    eidtphonenum.setVisibility(View.VISIBLE);
                }else {
                    change.setText("�޸�");
                    info.setPhoneNum(eidtphonenum.getText().toString());
                    pnumtext.setVisibility(View.VISIBLE);
                    pnumtext.setText(eidtphonenum.getText().toString());
                    eidtphonenum.setVisibility(View.GONE);
                }
            }
        });
        //ȡ������
        findViewById(R.id.chcelbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //��������
        findViewById(R.id.savebtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInfo();
                Intent retintent=new Intent();
                Bundle retbundle=new Bundle();
                retbundle.putSerializable("newinfo",info);
                retintent.putExtra("newinfol",retbundle);
                setResult(211, retintent);
                finish();
            }
        });
        editSchool.setText(info.getSchoolName());
        editAcademy.setText(info.getAcademyName());
        editStid.setText(info.getStudentid());
        editClass.setText(info.getClassName());
        emailtext.setText(info.getEmail());
        pnumtext.setText(info.getPhoneNum());
    }

    private void setInfo() {
        info.setSchoolName(editSchool.getText().toString());
        info.setAcademyName(editAcademy.getText().toString());
        info.setClassName(editClass.getText().toString());
        info.setStudentid(editStid.getText().toString());
    }
}
