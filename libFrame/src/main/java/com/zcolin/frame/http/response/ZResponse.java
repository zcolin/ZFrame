/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */

package com.zcolin.frame.http.response;


import android.app.Activity;

import com.zcolin.frame.http.ZReply;
import com.zcolin.frame.util.LogUtil;
import com.zcolin.frame.util.ToastUtil;

import okhttp3.Response;


/**
 * 返回网络对象，使用gons解析好，并通过状态码做成功失败分发
 */
public abstract class ZResponse<T extends ZReply> {
    static ZResponseIntercept RESPONSEINTERCEPT;
    String   barMsg;        //进度条上的文字
    Activity barActy;       //进度条的Activity
    Class<T> cls;

    /**
     * 设置返回拦截器
     */
    public static void setResponseIntercept(ZResponseIntercept intercept) {
        RESPONSEINTERCEPT = intercept;
    }

    public ZResponse(Class<T> cls) {
        this(cls, null);
    }

    public ZResponseProxy generatedProxy() {
        return new ZResponseProxy<>(cls, this, barActy, barMsg);
    }

    /**
     * @param barActy 进度条Atvicity实体
     */
    public ZResponse(Class<T> cls, Activity barActy) {
        this(cls, barActy, null);
    }

    /**
     * @param barActy 进度条Atvicity实体
     * @param barMsg  进度条上 显示的信息
     */
    public ZResponse(Class<T> cls, Activity barActy, String barMsg) {
        this.cls = cls;
        this.barActy = barActy;
        this.barMsg = barMsg;
    }

    /**
     * 请求成功回调
     *
     * @param response response实例
     * @param resObj   返回的数据实体
     */
    public abstract void onSuccess(Response response, T resObj);

    public void onError(int code, String error) {
        ToastUtil.toastShort(error);
        LogUtil.w("HttpResponse:", error);
    }

    public void onFinished() {
    }
}
