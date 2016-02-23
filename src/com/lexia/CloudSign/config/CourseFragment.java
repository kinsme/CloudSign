package com.lexia.CloudSign.config;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.*;
import com.google.gson.Gson;
import com.lexia.CloudSign.CourseDetails;
import com.lexia.CloudSign.R;
import com.lexia.CloudSign.RegUser;
import com.lexia.CloudSign.UserPageAty;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CourseFragment extends Fragment {
    private int career; //1����ѧ����2������ʦ
    private String courseFilename;
    private boolean pressedCourse=false;  //�Ƿ�㵽�γ�
    private int coursewidth,courseheight,xtime,ytime;
    private RelativeLayout coursemain;
    private RegUser regUser;
    private boolean changePhone=false;
    private GestureDetector gestureDetector;
    private String newcoursename,newteachername;
    private AddCourseDialog cdialog,cdialogt;
    //private AddCourseDialogByTeacher cdialogt;
    private View view;
    private int changingNum=0;
    private ArrayList<NewCourse> courses=new ArrayList<NewCourse>();
    private ArrayList<String> coursejson=new ArrayList<String>();
    private File savefile;
    private float scale;
    private ExecutorService UpdateCourseThread= Executors.newSingleThreadExecutor();
    /**
     * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
     * (int) (dpValue * scale + 0.5f);
     */

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.coursefragment,container,false); //false��Ҫ�ֶ���ӵ�container
        Log.i("textpager","coursefragmentcreate");
        TextView weektext= (TextView) view.findViewById(R.id.weektext);
        coursemain= (RelativeLayout) view.findViewById(R.id.relativemain);
        regUser= BmobUser.getCurrentUser(getActivity(), RegUser.class);
        if(regUser.getCareer().equals("ѧ��")){
            career=1;
        }
        else {
            career=2;
        }
        courseFilename=regUser.getUsername()+"course";
//      filename=getActivity().getFilesDir().getPath().toString()+"/CourseSaved.s";
        scale =getActivity().getResources().getDisplayMetrics().density;
        int sceenWidth=getArguments().getInt("width");
        coursewidth=(sceenWidth-dip2px(40))/7;
        initCourses();  //��ȡ����˴洢��course����ʾ
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("textpager","coursecreatedd");
        gestureDetector=new GestureDetector(getActivity(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {

                        if(pressedCourse){
                        //�鿴�γ�����
                        //final int j=i;
                        pressedCourse=false;
                        Intent intent=new Intent();
                        intent.setClass(getActivity(), CourseDetails.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("course",courses.get(changingNum));
                        intent.putExtra("coursename",bundle);
                        getActivity().startActivityForResult(intent,22);
//                        changedlog.findViewById(R.id.changebtn).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                courses.get(j).setName(changedlog.Name()+""+changedlog.getTeacherName());
//                                TextView course= (TextView) coursemain.findViewById(courses.get(j).getId());
//                                course.setText(courses.get(j).getName());
//                                //SaveCourse();
//                                ChangeCourse(j,courses.get(j));  //�����޸ĺ�Ŀγ�
//                                changedlog.dismiss();
//
//                            }
//                        });
                        return true;
                        }
                else {
                if(regUser.getCareer().equals("ѧ��")){
                    //ѧ������
                    cdialog= new AddCourseDialog(getActivity(), R.style.CourseDialog, R.layout.addcoursedlog) {
                        @Override
                        public String getTeacherName() {
                            EditText newteachername= (EditText) findViewById(R.id.newteachername);
                            this.setTeacherName(newteachername.getText().toString());
                            return teacherName;
                        }
                    };
                    cdialog.setCoursenameid(R.id.newcoursename);
                    cdialog.show();
                    setNewCourse(cdialog,R.id.addbtn);
                    return true;
                }
                else{//��ʦ����
                    cdialogt=new AddCourseDialog(getActivity(), R.style.CourseDialog, R.layout.addcoursedlogbyt) {
                        @Override
                        public String getTeacherName() {
                            return regUser.getName();
                        }
                    };
                    ((TextView)cdialogt.findViewById(R.id.myteachername)).setText(cdialogt.getTeacherName());
                    cdialogt.setCoursenameid(R.id.mycoursename);
                    cdialogt.show();
                    setNewCourse(cdialogt,R.id.addbtnt);
                    return true;
                }
              }
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

                if(pressedCourse){
                        pressedCourse=false;
                        coursemain.showContextMenu();
                }

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }
        });
        coursemain.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.setHeaderTitle("�γ̲���");
                MenuItem changeItem=contextMenu.add("�޸�");
                MenuItem deleteItem=contextMenu.add("ɾ��");
                MenuItem signinItem=contextMenu.add("ǩ��");
                //�޸Ĳ���
                changeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        ChangeCourseDialog changedlog=new ChangeCourseDialog(getActivity(),R.style.CourseDialog);
                        changedlog.show();
                        changedlog.findViewById(R.id.changebtn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SetCoursesUpdate();//����״̬
                                courses.get(changingNum).setName(changedlog.getCourseName());
                                courses.get(changingNum).setTeachername(changedlog.getTeacherName());
                                TextView course= (TextView) coursemain.findViewById(courses.get(changingNum).getId());
                                course.setText(courses.get(changingNum).getName()+"\n"+courses.get(changingNum).getTeachername());
                                ChangeCourse(changingNum, courses.get(changingNum));  //�����޸ĺ�Ŀγ�
                                changedlog.dismiss();
                            }
                        });
                        return true;
                    }
                });
                //ǩ������
                signinItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        UserPageAty myUser= (UserPageAty) getActivity();
                        myUser.getMviewpager().setCurrentItem(1);
                        return true;
                    }
                });
                //ɾ������
                deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        FrameLayout delframe= (FrameLayout) coursemain.findViewById(courses.get(changingNum).getFrameId());
                        delframe.setVisibility(View.GONE);
                        DeleteCourse();
                        return true;
                    }
                });

            }
        });
        coursemain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                int x=(int)motionEvent.getX(),y=(int)motionEvent.getY();
                xtime=(x-dip2px(40))/coursewidth*coursewidth+dip2px(40);
                ytime=y/dip2px(50)*dip2px(50);
                //���Ч��
                if (motionEvent.getX() >= dip2px(40)) {
                    for(int i=0;i<courses.size();i++){
                        if(xtime==courses.get(i).getXlocation() && ytime==courses.get(i).getYlocation()){
                            pressedCourse=true;
                            changingNum=i;
                            FrameLayout frame= (FrameLayout) view.findViewById(courses.get(i).getFrameId());
                            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                                frame.getBackground().setAlpha(50);
                            }else if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                                pressedCourse=false;
                                frame.getBackground().setAlpha(255);
                            }
                        }
                    }
                }
                return true;
            }
        });
    }



    //��ʼ���α�
    private void initCourses() {
        GetCourse();
        for(int i=0;i<courses.size();i++){
            addCourse(courses.get(i));
        }

        //getActivity().getSupportFragmentManager().findFragmentById(0).re
//        savefile=new File(filename);
//        ObjectOutputStream oos=null;
//        if(!savefile.exists()){
//            Log.i("first","if");
//            try{
//                savefile.createNewFile();
//                Log.i("first","first"+savefile.getAbsolutePath());
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.i("first","first"+e);
//            }
//            return false;   //��һ�δ���
//        }
//        else{
//            GetCourse();
//            Log.i("second","second"+savefile.getAbsolutePath());
//            Log.i("second","length"+courses.size());
////            for(int i=0;i<courses.size();i++)
////            {
////                addCourse(courses.get(0));
////            }
//            return true; //�ѻ�ȡ��ԭ������Ŀα�
//        }
    }

    private void setNewCourse(AddCourseDialog dialog,int btnid) {
        Button addbtn= (Button) dialog.findViewById(btnid);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("stoped","courses"+dialog.getCourseName());
                SetCoursesUpdate();//����״̬,�ı�fragment����
                newcoursename =dialog.getCourseName();
                //newteachername=dialog.getTeacherName();
                NewCourse newCourse=new NewCourse(newcoursename);
                newCourse.setXlocation(xtime);
                newCourse.setYlocation(ytime);
                newCourse.generateTime((xtime-dip2px(40))/coursewidth, ytime / dip2px(50));
                //����˴����¿γ�
                if(career==1){//ѧ�������γ�
                    BmobQuery<CourseNet> quary=new BmobQuery<>();
                    quary.addWhereEqualTo("courseName",newcoursename);
                    quary.include("objectId");
                    quary.include("teacher");
                    quary.findObjects(getActivity(), new FindListener<CourseNet>() {
                        @Override
                        public void onSuccess(List<CourseNet> list) {
                            if (list.size() == 0) {
                                newCourse.setTeachername("δ����");
                                newCourse.setColor(Color.RED);
                                addCourse(newCourse);//��ӵ�fragment��
                                courses.add(newCourse);
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "û�п���˿γ�,����ǩ��", Toast.LENGTH_SHORT).show();
                            } else {
                                BmobRelation relation=new BmobRelation();
                                relation.add(regUser);
                                //���ÿγ���ʦ�Լ���ʦ����
                                newCourse.setTeachername(list.get(0).getTeacher().getName());
                                newCourse.setTeacherBt(list.get(0).getTeacher().getBtAddres());
                                newCourse.setObjectID(list.get(0).getObjectId());
                                list.get(0).setStudents(relation);
                                list.get(0).update(getActivity(),new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        newCourse.setColor(Color.GREEN);
                                        addCourse(newCourse);//��ӵ�fragment��
                                        courses.add(newCourse);
                                        dialog.dismiss();
                                        Toast.makeText(getActivity(), "����γ�", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        dialog.dismiss();
                                        Toast.makeText(getActivity(),s, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(int i, String s) {

                        }
                    });

                    Gson gson=new Gson();
                    coursejson.add(gson.toJson(newCourse));
                }
                if(career==2){
                    SaveTeacherCourse(newcoursename,newCourse);
                    courses.add(newCourse);
                    dialog.dismiss();
                }
//                addCourse(newCourse);//��ӵ�fragment��
//                courses.add(newCourse);
//                dialog.dismiss();
            }
        });
    }
    //����˱�����ʦ�γ�
    private void SaveTeacherCourse(String newcname,NewCourse newCourse) {
        CourseNet course=new CourseNet();
        course.setTeacher(regUser);
        course.setCourseName(newcname);
        course.setTeacherBtAddress(regUser.getBtAddres());
        course.save(getActivity(), new SaveListener() {
            @Override
            public void onSuccess() {
                newCourse.setTeachername(regUser.getName());
                newCourse.setTeacherBt(course.getTeacherBtAddress());
                newCourse.setObjectID(course.getObjectId());
                newCourse.setColor(Color.GREEN);
                addCourse(newCourse);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getActivity(),"��������"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==222){
            UserPageAty myUser= (UserPageAty) getActivity();
            myUser.getMviewpager().setCurrentItem(1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("destory","dcourse");
    }

    @Override
    public void onStop() {
        SaveCourse();
        Thread courseupdate= new Thread(){
            @Override
            public void run() {
                super.run();
                RegUser newcourses=new RegUser();
                newcourses.setCourses(coursejson);
                newcourses.update(getActivity(), regUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Log.i("updates","courseupdateSuccess!");
                    }
                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
                if(career==2){
                    //���γ��Ƿ�ȫ���ϴ�
                    Log.i("querys","start");
                    BmobQuery<CourseNet> query=new BmobQuery<>();
                    query.addWhereEqualTo("teacher",regUser);
                    query.include("courseName");
                    query.findObjects(getActivity(), new FindListener<CourseNet>() {
                        @Override
                        public void onSuccess(List<CourseNet> list) {
                            Log.i("querys","add"+list.size());
                            //���ж��ϴ�
                            if(list.size()==0){
                                for(int i=0;i<courses.size();i++)
                                {
                                    SaveTeacherCourse(courses.get(i).getName(),courses.get(i));
                                    Log.i("querys","add"+courses.get(i).getName());
                                }
                            }
                            if(list.size()==coursejson.size()){
                                Log.i("querys","success");
                            }
                            else{//ȱ��
                                for(int i=0;i<courses.size();i++)
                                {
                                    for(int j=0;j<list.size();j++){
                                        if(courses.get(i).getName().equals(list.get(j).getCourseName())){
                                            break;
                                        }else if(j==list.size()-1){
                                            SaveTeacherCourse(courses.get(i).getName(),courses.get(i));
                                            Log.i("querys","add"+courses.get(i).getName());
                                        }
                                    }
                                }
                            }
                        }
                        @Override
                        public void onError(int i, String s) {

                        }
                    });
                }
            }
        };
        ((UserPageAty)getActivity()).getSingleUpdateThread().execute(courseupdate);
        super.onStop();
    }

    private void addCourse(NewCourse newCourse) {//��������¿γ�
        TextView newcoursetext=new TextView(getActivity());
        newcoursetext.setText(newCourse.getName()+"\n"+newCourse.getTeachername());

        newcoursetext.setGravity(Gravity.CENTER);
        if(newCourse.getId()==0){newCourse.generateId();} //ʱ�䣨λ�ã�����id

        newcoursetext.setId(newCourse.getId());
        Log.i("id",newCourse.getId()+"");
        FrameLayout courseframe=new FrameLayout(getActivity());  //�γ�ģ��

        courseframe.setBackgroundColor(newCourse.getColor());
        courseframe.setId(newCourse.getFrameId());
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(coursewidth,dip2px(50));
//        if(changePhone){
//            layoutParams.setMargins(newCourse.getXtime()*coursewidth+dip2px(40),
//                    newCourse.getYtime()*dip2px(50),0,0);
//        }else{
            layoutParams.setMargins(newCourse.getXlocation(),
                    newCourse.getYlocation(),0,0);
//        }
        courseframe.setLayoutParams(layoutParams);
        courseframe.addView(newcoursetext);
        coursemain.addView(courseframe);
    }


    private int dip2px(int dp){
        return (int)(dp*scale+0.5f);
    }

    //�γ�д�뱾���ļ�
    public void SaveCourse(){
        Log.i("savecourse","starth2");
//        ObjectOutputStream oos=null;
//
//        try {
//            //FileOutputStream fos=getActivity().openFileOutput(filename, getActivity().MODE_PRIVATE);
//            savefile.delete();
//            savefile.createNewFile();
//            FileOutputStream fos=new FileOutputStream(filename);
//            oos=new ObjectOutputStream(fos);
//            oos.writeObject(courses);
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        SharedPreferences sp=getActivity().getSharedPreferences(courseFilename,getActivity().MODE_PRIVATE);
        SharedPreferences.Editor mEdit1= sp.edit();
        mEdit1.clear();
        Gson gson=new Gson();
        coursejson.clear();
        String courseo="";
        for(int i=0;i<courses.size();i++) {
            // os.writeObject(courses.get(i));
            courseo=gson.toJson(courses.get(i));
            coursejson.add(courseo);
            mEdit1.putString("Status_" + i,courseo);
        }
        mEdit1.putInt("Status_size",courses.size()); /*sKey is an array*/
        mEdit1.commit();
    }
    //

//    private String bytesTohString(byte[] bArray) {
//        if(bArray == null){
//            return null;
//        }
//        if(bArray.length == 0){
//            return "";
//        }
//        StringBuffer sb = new StringBuffer(bArray.length);
//        String sTemp;
//        for (int i = 0; i < bArray.length; i++) {
//            sTemp = Integer.toHexString(0xFF & bArray[i]);
//            if (sTemp.length() < 2)
//                sb.append(0);
//            sb.append(sTemp.toUpperCase());
//        }
//        return sb.toString();
//
//    }

    //��ȡ�γ��ļ�
    public void GetCourse() {
//        FileInputStream fis = null;
//        ObjectInputStream ois = null;
//        try {
//            fis=new FileInputStream(filename);
//            ois=new ObjectInputStream(fis);
//            courses=(ArrayList<NewCourse>)ois.readObject();
//            Log.i("getcourse","starth22");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (StreamCorruptedException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        SharedPreferences sp = getActivity().getSharedPreferences(courseFilename, getActivity().MODE_PRIVATE);
        courses.clear();
        coursejson.clear();
        int size = sp.getInt("Status_size", 200);
        Gson gson = new Gson();

        if (size == 200) {
            //������˶�ȡ
            //changePhone=true;
            coursejson = (ArrayList<String>) regUser.getCourses();
            if(coursejson==null){
                coursejson=new ArrayList<>();
            }
            for (int i = 0; i < coursejson.size(); i++) {
                NewCourse newCourse = gson.fromJson(coursejson.get(i), NewCourse.class);
//                newCourse.getXtime()*coursewidth+dip2px(40),
////                    newCourse.getYtime()*dip2px(50)
                newCourse.setXlocation(newCourse.getXtime()*coursewidth+dip2px(40));
                newCourse.setYlocation(newCourse.getYtime()*dip2px(50));
                courses.add(newCourse);
            }
        } else {
            for (int i = 0; i < size; i++) {
                NewCourse newCourse = gson.fromJson(sp.getString("Status_" + i, null), NewCourse.class);
                courses.add(newCourse);
                coursejson.add(sp.getString("Status_" + i, null)); //json��ʽ�γ̶�ȡ
            }

        }
    }
    //�޸Ŀγ��ļ�
    private void ChangeCourse(int j, NewCourse changedcourse) {
        SharedPreferences sp=getActivity().getSharedPreferences(courseFilename,getActivity().MODE_PRIVATE);
        SharedPreferences.Editor mEdit1= sp.edit();
        Gson gson=new Gson();
        String courseo="";
        courseo=gson.toJson(changedcourse);
        //coursejson.get(j)=courseo;
        coursejson.remove(j);
        coursejson.add(j,courseo);
        mEdit1.remove("Status_" + j);
        mEdit1.putString("Status_" + j,courseo);
        mEdit1.commit();
    }
    //ɾ���γ�
    private void DeleteCourse(){
        SetCoursesUpdate();
        SharedPreferences sp=getActivity().getSharedPreferences(courseFilename,getActivity().MODE_PRIVATE);
        SharedPreferences.Editor mEdit1=sp.edit();
        if(career==1){
            //ɾ��������ϵ
            if(!courses.get(changingNum).getTeachername().equals("δ����")){
                CourseNet delcourse=new CourseNet();
                delcourse.setObjectId(courses.get(changingNum).getObjectID());
                BmobRelation relation=new BmobRelation();
                relation.remove(regUser);
                delcourse.setStudents(relation);
                delcourse.update(getActivity(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getActivity(),"�˳��γ�",Toast.LENGTH_SHORT).show();
                        courses.remove(changingNum);
                        coursejson.remove(changingNum);
                        SaveCourse();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        courses.remove(changingNum);
                        coursejson.remove(changingNum);
                    }
                });
            }
            else{
                courses.remove(changingNum);
                coursejson.remove(changingNum);
                SaveCourse();
            }
        }
        else{
            //��ʦɾ���γ�
            CourseNet delcourse=new CourseNet();
            delcourse.setObjectId(courses.get(changingNum).getObjectID());
            delcourse.delete(getActivity(), new DeleteListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
            courses.remove(changingNum);
            coursejson.remove(changingNum);
            SaveCourse();
        }
    }
    //���ÿγ̸���״̬
    private void SetCoursesUpdate(){
        UserPageAty myuserpage= (UserPageAty) getActivity();
        myuserpage.setCoursesUpdate(true);
    }
}
