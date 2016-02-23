package com.lexia.CloudSign.config;

import android.content.Context;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.lexia.CloudSign.UserPageAty;

import java.util.ArrayList;

public class MyFragmentAdapter extends FragmentPagerAdapter{
    private ArrayList<Fragment> arrayList;
    private Context atyContext;
    private UserPageAty myUserPage;
    private ViewPager mviewpager;
    private FragmentManager fm;
    private boolean update=true;
    public MyFragmentAdapter(FragmentManager fm,Context atycontext) {
        super(fm);
        this.fm=fm;
        myUserPage= (UserPageAty) atycontext;
    }

    @Override
    public Fragment getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
    public void setFragments(ArrayList<Fragment> fragments) {
        if(this.arrayList != null){
            FragmentTransaction ft = fm.beginTransaction();
            for(Fragment f:this.arrayList){
                ft.remove(f);
            }
            ft.commit();
            ft=null;
            fm.executePendingTransactions();
        }
        this.arrayList = fragments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        //更新第二页
//        if(position==1&&update==true){
//            update=false;
//            notifyDataSetChanged();
//        }
//        if(position==0)
//        {
//            update=true;
//        }
        if(position==1&&myUserPage.isCoursesUpdate()==true){
            Log.i("updates","yes");
            myUserPage.setCoursesUpdate(false);
            notifyDataSetChanged();
        }
        super.setPrimaryItem(container, position, object);
    }


//    public void reLoad()
//    {
//        if(mListener != null)
//        {
//            mListener.onReload();
//        }
//        this.notifyDataSetChanged();
//    }
//    public void setOnReloadListener(OnReloadListener listener)
//    {
//        this.mListener = listener;
//    }
//    /**
//     *回调接口
//     */
//    public interface OnReloadListener
//    {
//        public void onReload();
//    }

////    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        View view=getItem(position).getView();
//        view.setTag(position);
//        container.addView(getItem(position).getView());
//        return view;
//    }
}
