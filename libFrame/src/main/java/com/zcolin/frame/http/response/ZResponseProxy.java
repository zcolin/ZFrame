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

import com.google.gson.JsonSyntaxException;
import com.zcolin.frame.app.BaseApp;
import com.zcolin.frame.http.ZReply;
import com.zcolin.frame.util.LogUtil;
import com.zcolin.frame.util.NetworkUtil;

import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Response;


/**
 * 返回gson对象，ZResponse的代理，处理数据完成后调用ZResponse的成功失败函数
 */
public class ZResponseProxy<T extends ZReply> extends ZGsonResponse<T> {
    public static final int STATUS_CODE_SUCCESS = 200;

    public ZResponse<T> zResponse;

    ZResponseProxy(Class<T> cls, ZResponse<T> zResponse) {
        super(cls);
        this.zResponse = zResponse;
    }

    ZResponseProxy(Class<T> cls, ZResponse<T> zResponse, Activity barActy) {
        super(cls, barActy);
        this.zResponse = zResponse;
    }

    ZResponseProxy(Class<T> cls, ZResponse<T> zResponse, Activity barActy, String barMsg) {
        super(cls, barActy, barMsg);
        this.zResponse = zResponse;
    }

    @Override
    public void onError(int code, Call call, Exception ex) {
        String str;
        if (ex instanceof SocketTimeoutException || code == 0) {
            if (!NetworkUtil.isNetworkAvailable(BaseApp.APP_CONTEXT)) {
                str = "当前无网络连接，请开启网络！";
            } else {
                str = "连接服务器失败, 请检查网络或稍后重试";
            }
            error(0, str);
        } else if (ex instanceof JsonSyntaxException) {
            error(-1, "json conversion failed, code is : -1");
        } else {
            error(code, LogUtil.ExceptionToString(ex));
        }
    }

    @Override
    public void onSuccess(Response response, T reply) {
        if (reply == null) {
            error(204, "response message is null, code is : 204");
        } else if (reply.isSuccess()) {
            success(response, reply);
        } else {
            error(reply.getReplyCode(), reply.getErrorMessage());
        }
    }

    private void error(int code, String message) {
        if (ZResponse.RESPONSEINTERCEPT == null || !ZResponse.RESPONSEINTERCEPT.onErrorIntercept(code, message)) {
            zResponse.onError(code, message);
        }
    }

    private void success(Response response, T reply) {
        if (ZResponse.RESPONSEINTERCEPT == null || !ZResponse.RESPONSEINTERCEPT.onSuccessIntercept(response, reply)) {
            zResponse.onSuccess(response, reply);
        }
    }

    @Override
    public void onFinished() {
        super.onFinished();
        zResponse.onFinished();
    }
}
