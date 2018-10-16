/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */

package com.zcolin.frame.http.okhttp.request;


import com.zcolin.frame.http.okhttp.OkHttpUtils;
import com.zcolin.frame.http.okhttp.builder.PostFormBuilder;
import com.zcolin.frame.http.okhttp.callback.Callback;

import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zhy on 15/12/14.
 * <p>
 * 表单post请求
 */
public class PostFormRequest extends OkHttpRequest {
    private List<PostFormBuilder.FileInput> files;
    private String                          mimeType;

    public PostFormRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, List<PostFormBuilder.FileInput> files, int id, String mimeType) {
        super(url, tag, params, headers, id);
        this.files = files;
        this.mimeType = mimeType;
    }

    @Override
    protected RequestBody buildRequestBody() {
        if (files == null || files.isEmpty()) {
            StringBuilder stringBuffer = new StringBuilder();
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    stringBuffer.append(key).append("=").append(params.get(key)).append("&");
                }
                if (stringBuffer.length() > 0) {
                    stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
                }
            }

            MediaType mime = mimeType == null ? MediaType.parse("application/x-www-form-urlencoded; charset=utf-8") : MediaType.parse(mimeType);
            return RequestBody.create(mime, stringBuffer.toString());
            //            此方法没有默认设置charset=utf-8，会导致中文乱码问题
            //            FormBody.Builder builder = new FormBody.Builder();
            //            addParams(builder);
            //            FormBody formBody = builder.build();
            //            return formBody;
        } else {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            addParams(builder);

            for (int i = 0; i < files.size(); i++) {
                PostFormBuilder.FileInput fileInput = files.get(i);
                MediaType mime = mimeType == null ? MediaType.parse(guessMimeType(fileInput.filename)) : MediaType.parse(mimeType);
                RequestBody fileBody = RequestBody.create(mime, fileInput.file);
                builder.addFormDataPart(fileInput.key, fileInput.filename, fileBody);
            }
            return builder.build();
        }
    }

    @Override
    protected RequestBody wrapRequestBody(RequestBody requestBody, final Callback callback) {
        if (callback == null) {
            return requestBody;
        }
        return new CountingRequestBody(requestBody, (bytesWritten, contentLength) -> OkHttpUtils.getInstance()
                                                                                                .getHandler()
                                                                                                .post(() -> callback.onProgress(bytesWritten * 1.0f / contentLength, contentLength)));
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private void addParams(MultipartBody.Builder builder) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""), RequestBody.create(null, params.get(key)));
            }
        }
    }

    private void addParams(FormBody.Builder builder) {
        if (params != null) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
    }

}
