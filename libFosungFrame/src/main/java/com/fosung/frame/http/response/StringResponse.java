/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-21 下午12:01
 * *********************************************************
 */

package com.fosung.frame.http.response;

import android.app.ProgressDialog;

import com.fosung.frame.app.BaseFrameActivity;
import com.fosung.frame.http.okhttp.callback.StringCallback;

import okhttp3.Request;


/**
 * 返回String对象
 */
public abstract class StringResponse extends StringCallback {

    private ProgressDialog proBar;        //请求过程中的进度条
    private String         barMsg;        //进度条上的文字

    public StringResponse() {
    }

    /**
     * @param barActy 进度条Atvicity实体
     */
    public StringResponse(BaseFrameActivity barActy) {
        this(barActy, null);
    }

    /**
     * @param barActy 进度条Atvicity实体
     * @param barMsg  进度条上 显示的信息
     */
    public StringResponse(BaseFrameActivity barActy, String barMsg) {
        if (barActy != null) {
            if (barActy.getProgressDialog() != null) {
                proBar = new ProgressDialog(barActy);
            } else {
                proBar = new ProgressDialog(barActy);
            }
            this.barMsg = barMsg;
        }
    }

    @Override
    public void onStart(Request request) {
        if (proBar != null) {
            proBar.show();
            proBar.setMessage(barMsg);
        }
    }

    @Override
    public void onFinished() {
        if (proBar != null) {
            proBar.dismiss();
            barMsg = null;
        }
    }
}
