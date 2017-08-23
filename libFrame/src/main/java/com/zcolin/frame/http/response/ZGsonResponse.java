/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-21 下午12:01
 * *********************************************************
 */

package com.zcolin.frame.http.response;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;

import com.zcolin.frame.app.BaseFrameActivity;
import com.zcolin.frame.http.okhttp.callback.GsonCallback;

import okhttp3.Request;


/**
 * 返回gson对象
 */
public abstract class ZGsonResponse<T> extends GsonCallback<T> {

    private ProgressDialog proBar;        //请求过程中的进度条
    private String         barMsg;        //进度条上的文字

    public ZGsonResponse(Class<T> cls) {
        super(cls);
    }

    /**
     * @param barActy 进度条Atvicity实体
     */
    public ZGsonResponse(Class<T> cls, Activity barActy) {
        this(cls, barActy, null);
    }

    /**
     * @param barActy 进度条Atvicity实体
     * @param barMsg  进度条上 显示的信息
     */
    public ZGsonResponse(Class<T> cls, Activity barActy, String barMsg) {
        super(cls);
        if (barActy != null) {
            if (barActy instanceof BaseFrameActivity && ((BaseFrameActivity) barActy).getProgressDialog() != null) {
                proBar = ((BaseFrameActivity) barActy).getProgressDialog();
            } else {
                proBar = new ProgressDialog(barActy);
                proBar.setCancelable(false);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (proBar != null && proBar.getWindow() != null && proBar.getWindow()
                                                                      .getDecorView() != null && proBar.getWindow()
                                                                                                       .getDecorView()
                                                                                                       .isAttachedToWindow()) {
                proBar.dismiss();
                barMsg = null;
            }
        } else {
            if (proBar != null) {
                proBar.dismiss();
                barMsg = null;
            }
        }
    }
}
