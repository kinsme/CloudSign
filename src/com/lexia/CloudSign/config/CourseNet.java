package com.lexia.CloudSign.config;
//����˴洢������ʦ����Ŀγ�
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;
import com.lexia.CloudSign.RegUser;

public class CourseNet extends BmobObject{
    private String courseName;  //��ʦ����Ŀγ���
    private String teacherBtAddress; //��ʦ������
    private RegUser teacher;     //�γ���ʦ(һ��һ��ϵ)
    private BmobRelation students;  //ѡ�ε�����ѧ������Զ��ϵ��
    private boolean isable=false;   //�γ��Ƿ������ϣ���ǩ����
    public boolean getIsable() {
        return isable;
    }

    public void setIsable(boolean isable) {
        this.isable = isable;
    }
    public BmobRelation getStudents() {
        return students;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTeacherBtAddress() {
        return teacherBtAddress;
    }

    public RegUser getTeacher() {
        return teacher;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setTeacherBtAddress(String teacherBtAddress) {
        this.teacherBtAddress = teacherBtAddress;
    }

    public void setTeacher(RegUser teacher) {
        this.teacher = teacher;
    }

    public void setStudents(BmobRelation students) {
        this.students = students;
    }
}
