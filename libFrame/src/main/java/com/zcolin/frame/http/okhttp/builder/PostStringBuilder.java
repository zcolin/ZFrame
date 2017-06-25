/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.zcolin.frame.http.okhttp.builder;


import com.zcolin.frame.http.okhttp.request.PostStringRequest;
import com.zcolin.frame.http.okhttp.request.RequestCall;

import okhttp3.MediaType;

/**
 * Created by zhy on 15/12/14.
 */
public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder> {
    private String    content;

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
