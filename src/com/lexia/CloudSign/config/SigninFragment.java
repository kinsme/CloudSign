package com.lexia.CloudSign.config;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.*;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import com.google.gson.Gson;
import com.lexia.CloudSign.R;
import com.lexia.CloudSign.RegUser;
import com.lexia.CloudSign.SigninList;

import java.util.ArrayList;


public class SigninFragment extends Fragment {
    private View view;
    private String courseFilename;
    private RegUser regUser;
    private int career;
    private boolean isabled=false;
    private Animation anim; //��ť����
    private ImageButton shuaxinisable,shuaxinnum;
    private BluetoothAdapter mAdapter;
    private ArrayList<NewCourse> courses=new ArrayList<NewCourse>();
    private NewCourse choosedCourse;  //����ǩ���Ŀγ�
   // private Spinner coursechoosed;
    private LinearLayout myspinner;
    private ImageView imageView;
    private TextView choosedCourseT;
    private TextView choosedCourseName,choosedCourseTeacher,choosedCourseTeacherbt,choosedCourseison,choosedsignNum,
    signinstate1,signinstate2,signinstate3,signinstate4;
    private Button signinNow;
    private MySpinnerAdapter mysAdapter;
    private boolean signIsok=false;
    private BroadcastReceiver findteacher=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            Log.i("btooth","ofd");
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //Toast.makeText(getContext(),device.getAddress(),Toast.LENGTH_SHORT).show();
                //Toast.makeText(getContext(),choosedCourse.getTeacherBt(),Toast.LENGTH_SHORT).show();
                Log.i("btooth", device.getAddress() + " " + choosedCourse.getTeacherBt());
                if(device.getAddress().equals(choosedCourse.getTeacherBt())){
                    signIsok=true;
                    //ǩ���ɹ�
                    StudentSigninfo me=new StudentSigninfo("s"+choosedCourse.getObjectID());
                    me.setBtAddress(regUser.getBtAddres());
                    me.setStudentname(regUser.getName());
                    me.setStudentid(regUser.getStudentid());
                    me.save(getActivity(), new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getActivity(),"ǩ���ɹ�",Toast.LENGTH_SHORT).show();
                            signinstate3.setText("ǩ���ɹ�");
                            signinNow.setText("��ǩ");
                            //signinNow.setClickable(false);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.i("course",s);
                        }
                    }); //&& signIsok==false
                }
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                Log.i("btooth","of");
                Toast.makeText(getActivity(),"ǩ��ʧ��",Toast.LENGTH_SHORT).show();
                signinstate3.setText("ǩ��ʧ��");
                //signinstate4.setText("δ����Ч��Χ��");
                signinNow.setText("ʧ��");
            }
        }
    };
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("textpager","signinfragmentcreate");
        view= inflater.inflate(R.layout.signinfragment,container,false);
        regUser= BmobUser.getCurrentUser(getActivity(), RegUser.class);
        signinNow= (Button) view.findViewById(R.id.signinnow);
        shuaxinisable= (ImageButton) view.findViewById(R.id.shuaxinisable);
        shuaxinnum= (ImageButton) view.findViewById(R.id.shuaxinnum);
        if(regUser.getCareer().equals("ѧ��")){
            career=1;
            signinNow.setText("ǩ��");
            signinNow.setClickable(true);

        }else{
            career=2;
            signinNow.setText("����");
            shuaxinisable.setVisibility(View.GONE);
        }
        courseFilename=regUser.getUsername()+"course";
        GetCourse();  //��ȡ�������пγ�
        //��ʼ���ؼ�
        //coursechoosed= (Spinner) view.findViewById(R.id.choosedcourse);
        myspinner= (LinearLayout) view.findViewById(R.id.mydropdown);
        imageView= (ImageView) view.findViewById(R.id.arrowImage);
        choosedCourseT= (TextView) view.findViewById(R.id.choosed_coursenamet);
        choosedCourseName= (TextView) view.findViewById(R.id.choosedcoursename);
        choosedCourseTeacher= (TextView) view.findViewById(R.id.choosedcourseteacher);
        choosedCourseTeacherbt= (TextView) view.findViewById(R.id.choosedcourseteacherbt);
        signinstate1= (TextView) view.findViewById(R.id.signinstate1);
        signinstate2= (TextView) view.findViewById(R.id.signinstate2);
        signinstate3= (TextView) view.findViewById(R.id.signinstate3);
        signinstate4= (TextView) view.findViewById(R.id.signinstate4);

        choosedsignNum= (TextView) view.findViewById(R.id.choosedsignnum);
        choosedCourseison= (TextView) view.findViewById(R.id.choosedcourseison);
        mAdapter=BluetoothAdapter.getDefaultAdapter();
        ArrayList<String> arrayList=new ArrayList<>();
        //ArrayAdapter<String> madapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);
        for(int i=0;i<courses.size();i++){
            //madapter.add(courses.get(i).getName());
            arrayList.add(courses.get(i).getName());
        }
        mysAdapter=new MySpinnerAdapter(getActivity(),arrayList);
        choosedCourse=courses.get(0);
        choosedCourseT.setText(courses.get(0).getName());
        choosedCourseName.setText(courses.get(0).getName());
        choosedCourseTeacher.setText(courses.get(0).getTeachername());
        choosedCourseTeacherbt.setText(courses.get(0).getTeacherBt());
        myspinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.arrowr);
                showWindow(myspinner,choosedCourseT);
            }
        });
        //madapter.setDropDownViewResource(android.R.layout.test_list_item);
        //coursechoosed.setAdapter(madapter);
        //ע��
        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(findteacher,mFilter);
        return view;
    }
    public void showWindow(View position,final TextView textView){
        LinearLayout layout= (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.myspinner,null);
        ListView listView= (ListView) layout.findViewById(R.id.mlist);
        listView.setAdapter(mysAdapter);
        PopupWindow popupWindow=new PopupWindow(position);
        popupWindow.setWidth(myspinner.getWidth());
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setContentView(layout);
        popupWindow.showAsDropDown(position, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                imageView.setImageResource(R.drawable.arrow);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                resetsignin();
                choosedCourse = courses.get(i);
                choosedCourseT.setText(choosedCourse.getName());
                choosedCourseName.setText(choosedCourse.getName());
                choosedCourseTeacher.setText(choosedCourse.getTeachername());
                choosedCourseTeacherbt.setText(choosedCourse.getTeacherBt());
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        anim= AnimationUtils.loadAnimation(getActivity(), R.anim.tip);
        anim.setInterpolator(new LinearInterpolator());
        choosedsignNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("courseid",choosedCourse.getObjectID());
                intent.setClass(getActivity(), SigninList.class);
                startActivity(intent);
            }
        });
//        coursechoosed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                resetsignin();
//                choosedCourse = courses.get(i);
//                choosedCourseName.setText(choosedCourse.getName());
//                choosedCourseTeacher.setText(choosedCourse.getTeachername());
//                choosedCourseTeacherbt.setText(choosedCourse.getTeacherBt());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        signinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(career==2){
                    //��ʦ����
                    if (signinNow.getText().equals("����")){
                        //������
                        if(!mAdapter.isEnabled()){
                    Intent enabler=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(enabler, 12);}

                }else{//��� ���
                        //coursechoosed.setClickable(true);
                        myspinner.setClickable(true);
                        mAdapter.disable();
                        signinNow.setText("����");
                        choosedCourseison.setText("��");
                        choosedCourseison.setTextColor(Color.RED);
                        signinstate3.setText("ǩ��������");

                        CourseNet newisable=new CourseNet();
                        newisable.setObjectId(choosedCourse.getObjectID());
                        newisable.setIsable(false);
                        newisable.update(getActivity(), new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getActivity(),"�Ѹ��½��",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });
                    }
                }
                else{//ѧ��ǩ��
                    if(signinNow.getText().equals("ǩ��")){
                        Log.i("qiandao","11");
                        if(isabled){
                            if(!mAdapter.isEnabled()){
                                Intent enabler=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enabler,13);
                            }else {
                                studentSignIn();
                            }
                        }else {
                           Toast.makeText(getActivity(),"δ�ϿΣ������",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        //ѧ��ˢ���Ƿ񿪿�
        if(career==1){
            shuaxinisable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shuaxinisable.startAnimation(anim);
                    quaryIsAble();
                }
            });
        }
        //ˢ��ǩ�������洢������
        shuaxinnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuaxinnum.startAnimation(anim);
                DownloadSignTask download=new DownloadSignTask(choosedsignNum,"s"+choosedCourse.getObjectID(),getActivity());
                download.execute("s"+choosedCourse.getObjectID());
            }
        });

    }

    private void quaryIsAble() {
        BmobQuery<CourseNet> quary=new BmobQuery<>();
        quary.getObject(getActivity(), choosedCourse.getObjectID(), new GetListener<CourseNet>() {
            @Override
            public void onSuccess(CourseNet courseNet) {
                isabled=courseNet.getIsable();
                if(isabled){
                    choosedCourseison.setText("��");
                    choosedCourseison.setTextColor(Color.GREEN);
                    signinNow.setClickable(isabled);
                }
                else {
                    choosedCourseison.setText("��");
                    choosedCourseison.setTextColor(Color.RED);
                    //signinNow.setClickable(isabled);
                }
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    private void resetsignin() {
        isabled=false;
        signinNow.setClickable(true);
        signinstate1.setText("");
        signinstate2.setText("");
        signinstate3.setText("");
        choosedCourseison.setText("��");
        choosedCourseison.setTextColor(Color.RED);
        choosedsignNum.setText("0");
        if(career==1){
            signinNow.setText("ǩ��");
            //signinNow.setClickable(isabled);
        }else{
            signinNow.setText("����");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //��ʦ����������
        if(requestCode==12){
            Log.i("results",""+resultCode);
        if(resultCode==120){
            signinstate1.setText("�Ѵ�����");
            signinstate2.setText("���ڵȴ�ǩ��...");
            choosedCourseison.setText("��");
            choosedCourseison.setTextColor(Color.GREEN);
            CourseNet newisable=new CourseNet();
            newisable.setObjectId(choosedCourse.getObjectID());
            newisable.setIsable(true);
            newisable.update(getActivity(), new UpdateListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getActivity(),"�Ѹ��¿���",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
            signinNow.setText("���");
            //coursechoosed.setClickable(false);
            myspinner.setClickable(false);
        }
        }
        //ѧ������������
        if(requestCode==13){
            if(resultCode==getActivity().RESULT_OK){
                studentSignIn();  //ǩ��
            }
        }
    }

    private void studentSignIn() {
        signinNow.setText("����ǩ��");
        signinNow.setClickable(false);
        signinstate1.setText("�Ѵ�����");
        signinstate2.setText("��ʼǩ��...");
        //������ʦ����
        mAdapter.startDiscovery();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    //��д����
    public void GetCourse(){
        SharedPreferences sp=getActivity().getSharedPreferences(courseFilename,getActivity().MODE_PRIVATE);
        courses.clear();
        int size=sp.getInt("Status_size",200);
        Gson gson=new Gson();
        if(size==200){
            //������˶�ȡ
            ArrayList<String> coursejson= (ArrayList<String>) regUser.getCourses();
            if(coursejson==null){
                coursejson=new ArrayList<>();
            }
            for(int i=0;i<coursejson.size();i++){
                NewCourse newCourse=gson.fromJson(coursejson.get(i),NewCourse.class);
                courses.add(newCourse);
            }
        }
        else{
            for(int i=0;i<size;i++){
            NewCourse newCourse= gson.fromJson(sp.getString("Status_" + i, null), NewCourse.class);
            courses.add(newCourse);
        }
    }
    }
    
}

