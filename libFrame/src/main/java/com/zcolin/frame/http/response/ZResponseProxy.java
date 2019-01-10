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
                str = "当前无网络连接，请开启网络~";
            } else {
                str = "连接服务器失败, 请检查网络或稍后重试~";
            }
            error(0, str);
        } else if ((str = getErrorMsg(code)) != null) {
            error(code, str);
        } else if (ex instanceof JsonSyntaxException) {
            error(-1, "json conversion failed, code is : -1");
        } else {
            error(code, LogUtil.ExceptionToString(ex));
        }
    }

    private String getErrorMsg(int code) {
        switch (code) {
            case 400:
                return "请求出现错误";
            case 401:
                return "没有提供认证信息";
            case 403:
                return "禁止访问";
            case 404:
                return "你要找的地址不见啦~";
            case 406:
                return "请求的资源并不符合要求";
            case 408:
                return "客户端请求超时";
            case 413:
                return "请求体过大";
            case 415:
                return "类型不正确";
            case 416:
                return "请求的区间无效";
            case 500:
                return "服务器出错啦~";
            case 501:
                return "请求还没有被实现";
            case 502:
                return "网关错误";
            case 503:
                return "服务暂时不可用。服务器正好在更新代码重启";
            case 505:
                return "请求的 HTTP 版本不支持";
            default:
                break;
        }
        return null;
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
