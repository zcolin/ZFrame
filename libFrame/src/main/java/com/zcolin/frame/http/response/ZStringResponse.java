/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */

package com.zcolin.frame.http.response;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;

import com.zcolin.frame.app.BaseFrameActivity;
import com.zcolin.frame.http.ZHttp;
import com.zcolin.frame.http.okhttp.callback.StringCallback;

import okhttp3.Request;


/**
 * 返回String对象
 */
public abstract class ZStringResponse extends StringCallback {

    private Dialog proBar;        //请求过程中的进度条
    private String barMsg;        //进度条上的文字
    private String cancelTag;     //取消的标志位

    public ZStringResponse() {
    }

    /**
     * @param barActy 进度条Atvicity实体
     */
    public ZStringResponse(Activity barActy) {
        this(barActy, null);
    }

    /**
     * @param barActy 进度条Atvicity实体
     * @param barMsg  进度条上 显示的信息
     */
    public ZStringResponse(Activity barActy, String barMsg) {
        this(barActy, barMsg, false);
    }

    /**
     * @param barActy    进度条Atvicity实体
     * @param barMsg     进度条上 显示的信息
     * @param cancelAble 是否可以取消
     */
    public ZStringResponse(Activity barActy, String barMsg, boolean cancelAble) {
        if (barActy != null) {
            if (barActy instanceof BaseFrameActivity && ((BaseFrameActivity) barActy).getProgressDialog() != null) {
                proBar = ((BaseFrameActivity) barActy).getProgressDialog();
            } else {
                proBar = new ProgressDialog(barActy);
                proBar.setCancelable(cancelAble);
                if (cancelAble) {
                    proBar.setOnCancelListener(dialogInterface -> {
                        ZHttp.cancelRequest(cancelTag);
                    });
                }
            }
            this.barMsg = barMsg;
        }
    }

    /**
     * 设置取消标志位
     */
    public ZStringResponse setCancelTag(String cancelTag) {
        this.cancelTag = cancelTag;
        return this;
    }

    @Override
    public void onStart(Request request) {
        if (proBar != null) {
            proBar.show();
            if (proBar instanceof ProgressDialog) {
                ((ProgressDialog) proBar).setMessage(barMsg);
            }
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
