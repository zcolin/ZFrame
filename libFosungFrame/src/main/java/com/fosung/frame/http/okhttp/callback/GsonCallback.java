/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.fosung.frame.http.okhttp.callback;


import com.fosung.frame.utils.GsonUtil;

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
        String string = response.body()
                                .string();
        if (cls == String.class) {
            return (T) string;
        }
        return GsonUtil.stringToBean(string, cls);
    }

}
