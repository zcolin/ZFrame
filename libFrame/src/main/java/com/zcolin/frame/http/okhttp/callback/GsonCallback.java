/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
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
 * Created by JimGong on 2016/6/23.
 */
public abstract class GsonCallback<T> extends Callback<T> {

    public Class<T> cls;

    public GsonCallback(Class<T> cls) {
        this.cls = cls;
    }

    @Override
    public T parseNetworkResponse(Response response) throws IOException {
        String string = response.body().string();
        if (ZHttp.LOG) {
            JsonElement element = GsonUtil.parse(string);
            String logstr = element == null ? string : element.toString();
            LogUtil.i("**返回数据**：", "url：" + response.request().url() + "\n数据：" + logstr + "\n");
        }

        if (cls == String.class) {
            return (T) string;
        }
        return GsonUtil.stringToBean(string, cls);
    }

}
