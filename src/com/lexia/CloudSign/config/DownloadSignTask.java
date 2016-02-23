package com.lexia.CloudSign.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import org.json.JSONArray;

public class DownloadSignTask extends AsyncTask<String,Void,Void>{
    private TextView tx;
    private String signlistname;
    private BmobQuery query;
    private Context context;

    protected Void doInBackground(String... pa) {
        //����signlistname�����
        if (pa[0].equals(signlistname)){
            query = new BmobQuery(signlistname);  //ǩ����
            query.findObjects(context, new FindCallback() {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    tx.setText(jsonArray.length()+""); //��ʾ����
                    tx.setTextColor(Color.GREEN);
                    saveInPath(jsonArray);
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });

        }
        return null;
    }
    protected void saveInPath(JSONArray jsonArray){
        SharedPreferences sharedPreferences=context.getSharedPreferences(signlistname+"sp",context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.putInt("number",jsonArray.length());
        editor.putString(signlistname,jsonArray.toString());
        editor.commit();
    }

    public DownloadSignTask(TextView textView,String s,Context text){
        this.signlistname=s;
        this.tx=textView;
        this.context=text;
    }
}
