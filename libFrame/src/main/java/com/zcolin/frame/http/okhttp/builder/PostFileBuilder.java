/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */

package com.zcolin.frame.http.okhttp.builder;


import com.zcolin.frame.http.okhttp.request.PostFileRequest;
import com.zcolin.frame.http.okhttp.request.RequestCall;

import java.io.File;

import okhttp3.MediaType;

/**
 * Created by zhy on 15/12/14.
 * <p>
 * update by zcolin
 */
public class PostFileBuilder extends OkHttpRequestBuilder<PostFileBuilder> {
    private File      file;
    private MediaType mediaType;


    public OkHttpRequestBuilder file(File file) {
        this.file = file;
        return this;
    }

    public OkHttpRequestBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build() {
        return new PostFileRequest(url, tag, params, headers, file, mediaType, id).build();
    }


}
