package com.lexia.CloudSign.config;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;
import com.lexia.CloudSign.R;


public class MyProgressDialog extends Dialog {

    public MyProgressDialog(Context context, int theme) {
        super(context, theme);
        this.setContentView(R.layout.progressbar);
    }

    public void setMessage(String message) {
        TextView loginstate = (TextView) findViewById(R.id.loginstate);
        loginstate.setText(message);
    }
}