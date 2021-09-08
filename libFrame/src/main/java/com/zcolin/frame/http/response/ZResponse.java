/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */

package com.zcolin.frame.http.response;


import android.app.Activity;
import android.widget.Toast;

import com.zcolin.frame.http.ZReply;
import com.zcolin.frame.util.LogUtil;

import java.lang.reflect.ParameterizedType;

import okhttp3.Response;


/**
 * 返回网络对象，使用gons解析好，并通过状态码做成功失败分发
 */
public abstract class ZResponse<T extends ZReply> {
    static    ZResponseIntercept RESPONSEINTERCEPT;
    protected Class<T>           dataClass;
    protected String             barMsg;        //进度条上的文字
    protected Activity           barActy;       //进度条的Activity

    /**
     * 设置返回拦截器
     */
    public static void setResponseIntercept(ZResponseIntercept intercept) {
        RESPONSEINTERCEPT = intercept;
    }

    public ZResponseProxy generatedProxy(String cancelTag) {
        if (dataClass == null) {
            dataClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        ZResponseProxy responseProxy = new ZResponseProxy<>(dataClass, this, barActy, barMsg);
        responseProxy.setCancelTag(cancelTag);
        return responseProxy;
    }

    public ZResponseProxy generatedProxy(String cancelTag, Class<T> cls) {
        if (dataClass == null) {
            dataClass = cls;
        }
        ZResponseProxy responseProxy = new ZResponseProxy<>(dataClass, this, barActy, barMsg);
        responseProxy.setCancelTag(cancelTag);
        return responseProxy;
    }

    public ZResponse() {
        this(null);
    }

    /**
     * @param barActy 进度条Atvicity实体
     */
    public ZResponse(Activity barActy) {
        this(barActy, null);
    }

    /**
     * @param barActy 进度条Atvicity实体
     * @param barMsg  进度条上 显示的信息
     */
    public ZResponse(Activity barActy, String barMsg) {
        this.barActy = barActy;
        this.barMsg = barMsg;
    }

    /**
     * @param barActy 进度条Atvicity实体
     * @param barMsg  进度条上 显示的信息
     */
    public ZResponse(Activity barActy, String barMsg, Class<T> dataClass) {
        this.barActy = barActy;
        this.barMsg = barMsg;
        this.dataClass = dataClass;
    }


    /**
     * 请求成功回调
     *
     * @param response response实例
     * @param resObj   返回的数据实体
     */
    public abstract void onSuccess(Response response, T resObj);

    public void onError(int code, String error) {
        Toast.makeText(barActy, error, Toast.LENGTH_SHORT).show();
        LogUtil.w("HttpResponse:", error);
    }

    public void onFinished() {
    }
}
