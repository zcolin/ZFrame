/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-21 上午11:38
 * *********************************************************
 */

package com.fosung.usedemo.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.TextView;

import com.fosung.usedemo.R;


/**
 * 进度条封装类
 */
public class ZDlgProgress extends ProgressDialog {

    private TextView     tvMessage;
    private CharSequence strMessage;

    public ZDlgProgress(Context context) {
        super(context);
        setCancelable(false);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    public void show() {
        super.show();
        setContentView(R.layout.view_progress);
        tvMessage = (TextView) findViewById(R.id.progressBar_tv);
        tvMessage.setText(strMessage);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void setMessage(CharSequence message) {
        if (isShowing()) {
            if (tvMessage != null)
                tvMessage.setText(message);
        }
        strMessage = message;
    }
}
