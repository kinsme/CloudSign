package com.lexia.CloudSign.config;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

/**
 * Created by pc123 on 2016/02/03.
 */
public class SpinnerPopWindow extends PopupWindow implements AdapterView.OnItemClickListener{
    private Context mContext;
    private ListView mList;

    public SpinnerPopWindow(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {

    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

}
