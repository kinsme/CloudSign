package com.lexia.CloudSign.config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lexia.CloudSign.R;

import java.util.ArrayList;

/**
 * Created by pc123 on 2016/02/04.
 */
public class MySpinnerAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> list;

    public MySpinnerAdapter(Context context, ArrayList<String> list) {
        super();
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
    }

    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if (view==null){
            view=inflater.inflate(R.layout.myspinneritem,null);
            viewHolder=new ViewHolder();
            viewHolder.layout= (RelativeLayout) view.findViewById(R.id.myspinner_dropdown_layout);
            viewHolder.textView= (TextView) view.findViewById(R.id.myspinner_dropdown_txt);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(list.get(i));
        return view;
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }
    public static class ViewHolder {
        public TextView textView;
        public RelativeLayout layout;
    }
}
