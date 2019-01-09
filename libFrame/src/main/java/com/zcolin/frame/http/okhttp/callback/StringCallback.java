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
            body = body == null ? null : new String(body.getBytes("utf-8"), "utf-8");
            LogUtil.i("**返回数据**：", "url：" + response.request().url() + "\n数据：" + body + "\n");
        }
        return body;
    }
}
