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
import com.zcolin.frame.http.okhttp.callback.FileCallBack;

import okhttp3.Request;


/**
 * 返回File对象
 */
public abstract class ZFileResponse extends FileCallBack {

    private Dialog proBar;        //请求过程中的进度条
    private String barMsg;        //进度条上的文字
    private String cancelTag;     //取消的标志位

    public ZFileResponse(String destPath) {
        this(destPath, null);
    }

    /**
     * @param barActy 进度条Atvicity实体
     */
    public ZFileResponse(String destPath, Activity barActy) {
        this(destPath, barActy, null);
    }

    /**
     * @param barActy 进度条Atvicity实体
     * @param barMsg  进度条上 显示的信息
     */
    public ZFileResponse(String destPath, Activity barActy, String barMsg) {
        this(destPath, barActy, barMsg, false);
    }

    /**
     * @param barActivity 进度条Atvicity实体
     * @param barMsg      进度条上 显示的信息
     * @param cancelAble  是否可以取消
     */
    public ZFileResponse(String destPath, Activity barActivity, String barMsg, boolean cancelAble) {
        super(destPath);
        if (barActivity != null) {
            if (barActivity instanceof BaseFrameActivity && ((BaseFrameActivity) barActivity).getProgressDialog() != null) {
                proBar = ((BaseFrameActivity) barActivity).getProgressDialog();
            } else {
                proBar = new ProgressDialog(barActivity);
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
    public ZFileResponse setCancelTag(String cancelTag) {
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

    public void setBarMsg(String barMsg) {
        if (proBar != null) {
            if (proBar.isShowing()) {
                if (proBar instanceof ProgressDialog) {
                    ((ProgressDialog) proBar).setMessage(barMsg);
                }
            } else {
                this.barMsg = barMsg;
            }
        }
    }
}
