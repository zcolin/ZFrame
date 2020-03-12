/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */

package com.zcolin.frame.http.okhttp.callback;

import com.google.gson.JsonElement;
import com.zcolin.frame.http.ZHttp;
import com.zcolin.frame.util.GsonUtil;
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
            JsonElement element = GsonUtil.parse(body);
            String logstr = element == null ? body : element.toString();
            LogUtil.i("**返回数据**：", response.request().url() + "\n" + logstr + "\n");
        }
        return body;
    }
}
