/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.fosung.frame.http.okhttp.builder;


import com.fosung.frame.http.okhttp.request.RequestCall;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class OkHttpRequestBuilder<T extends OkHttpRequestBuilder> {
    protected String                        url;
    protected Object                        tag;
    protected LinkedHashMap<String, String> headers;
    protected Map<String, String>           params;
    protected int                           id;
    protected String                        mimeType;

    public T id(int id) {
        this.id = id;
        return (T) this;
    }

    public T url(String url) {
        this.url = url;
        return (T) this;
    }


    public T tag(Object tag) {
        this.tag = tag;
        return (T) this;
    }

    public T headers(LinkedHashMap<String, String> headers) {
        this.headers = headers;
        return (T) this;
    }

    public T mimeType(String mimeType){
        this.mimeType = mimeType;
        return (T)this;
    }

    public T addHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return (T) this;
    }

    public abstract RequestCall build();
}
