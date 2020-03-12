/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */

package com.zcolin.frame.http.okhttp.builder;


import com.zcolin.frame.http.okhttp.request.PostStringRequest;
import com.zcolin.frame.http.okhttp.request.RequestCall;

import okhttp3.MediaType;

/**
 * Created by zhy on 15/12/14.
 * <p>
 * update by zcolin
 */
public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder> {
    private String content;

    public PostStringBuilder content(String content) {
        this.content = content;
        return this;
    }


    @Override
    public RequestCall build() {
        MediaType mime = mimeType == null ? null : MediaType.parse(mimeType);
        return new PostStringRequest(url, tag, params, headers, content, mime, id).build();
    }
}
