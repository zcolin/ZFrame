/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */

package com.zcolin.frame.http.okhttp.callback;

import com.zcolin.frame.http.ZHttp;
import com.zcolin.frame.util.LogUtil;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class StringCallback extends Callback<String> {
    @Override
    public String parseNetworkResponse(Response response) throws IOException {
        String body = response.body().string();
        if (ZHttp.LOG) {
            LogUtil.i("***************接收数据*****************：", body + "\n");
        }
        return body;
    }
}
