/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-21 下午1:54
 * *********************************************************
 */

package com.fosung.usedemo.http.response;

import com.fosung.frame.app.BaseApp;
import com.fosung.frame.app.BaseFrameActivity;
import com.fosung.frame.http.response.GsonResponse;
import com.fosung.frame.utils.LogUtil;
import com.fosung.frame.utils.NetworkUtil;
import com.fosung.usedemo.http.entity.HttpBaseReplyBean;

import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Response;


/**
 * 返回gson对象，ZResponse的代理，处理数据完成后调用ZResponse的成功失败函数
 */
public class ZResponseProxy<T extends HttpBaseReplyBean> extends GsonResponse<T> {
    private static final int STATUS_CODE_SUCCESS = 200;

    private ZResponse<T> zResponse;

    ZResponseProxy(Class<T> cls, ZResponse<T> zResponse) {
        super(cls);
        this.zResponse = zResponse;
    }

    ZResponseProxy(Class<T> cls, ZResponse<T> zResponse, BaseFrameActivity barActy) {
        super(cls, barActy);
        this.zResponse = zResponse;
    }

    ZResponseProxy(Class<T> cls, ZResponse<T> zResponse, BaseFrameActivity barActy, String barMsg) {
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
            zResponse.onError(0, str);
        } else {
            zResponse.onError(code, LogUtil.ExceptionToString(ex));
        }
    }

    @Override
    public void onSuccess(Response response, T reply) {
        if (reply != null && reply.code == STATUS_CODE_SUCCESS) {
            zResponse.onSuccess(response, reply);
        } else if (reply == null) {
            zResponse.onError(-1, "json对象解析失败！");
        } else {
            zResponse.onError(reply.code, reply.message);
        }
    }

    @Override
    public void onFinished() {
        super.onFinished();
        zResponse.onFinished();
    }
}
