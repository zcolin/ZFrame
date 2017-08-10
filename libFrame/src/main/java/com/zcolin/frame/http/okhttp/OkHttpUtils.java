/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.zcolin.frame.http.okhttp;

import android.os.Handler;
import android.os.Looper;

import com.zcolin.frame.http.okhttp.builder.GetBuilder;
import com.zcolin.frame.http.okhttp.builder.HeadBuilder;
import com.zcolin.frame.http.okhttp.builder.OtherRequestBuilder;
import com.zcolin.frame.http.okhttp.builder.PostFileBuilder;
import com.zcolin.frame.http.okhttp.builder.PostFormBuilder;
import com.zcolin.frame.http.okhttp.builder.PostStringBuilder;
import com.zcolin.frame.http.okhttp.callback.Callback;
import com.zcolin.frame.http.okhttp.request.RequestCall;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by zhy on 15/8/17.
 */
public class OkHttpUtils {
    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private volatile static OkHttpUtils  mInstance;
    private                 OkHttpClient mOkHttpClient;
    private                 Handler      handler;

    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }

        handler = new Handler(Looper.getMainLooper());
    }


    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        return initClient(null);
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder(METHOD.PUT);
    }

    public static HeadBuilder head() {
        return new HeadBuilder();
    }

    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder(METHOD.DELETE);
    }

    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder(METHOD.PATCH);
    }

    public Handler getHandler() {
        return handler;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public void execute(final RequestCall requestCall, Callback callback) {
        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;
        // final int id = requestCall.getOkHttpRequest().getId();

        requestCall.getCall()
                   .enqueue(new okhttp3.Callback() {
                       @Override
                       public void onFailure(Call call, final IOException e) {
                           if (call.isCanceled()) {
                               sendCancelResultCallback(finalCallback);
                               return;
                           }
                           sendFailResultCallback(0, call, e, finalCallback);
                       }

                       @Override
                       public void onResponse(final Call call, final Response response) {
                           try {
                               if (call.isCanceled()) {
                                   sendCancelResultCallback(finalCallback);
                                   return;
                               }

                               if (!finalCallback.validateReponse(response)) {
                                   sendFailResultCallback(response.code(), call, new IOException("request failed , reponse's code is : " + response.code()), 
                                           finalCallback);
                                   return;
                               }

                               Object o = finalCallback.parseNetworkResponse(response);
                               sendSuccessResultCallback(response, o, finalCallback);
                           } catch (IOException e) {
                               if ("Canceled".equals(e.getMessage())) {
                                   sendCancelResultCallback(finalCallback);
                               } else {
                                   sendFailResultCallback(response.code(), call, e, finalCallback);
                               }
                           } catch (Exception e) {
                               sendFailResultCallback(response.code(), call, e, finalCallback);
                           } finally {
                               if (response.body() != null)
                                   response.body()
                                           .close();
                           }

                       }
                   });
    }

    public void sendCancelResultCallback(final Callback callback) {
        if (callback == null)
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onCanceled();
                callback.onFinished();
            }
        });
    }

    public void sendFailResultCallback(final int code, final Call call, final Exception e, final Callback callback) {
        if (callback == null)
            return;

        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(code, call, e);
                callback.onFinished();
            }
        });
    }

    public void sendSuccessResultCallback(final Response response, final Object object, final Callback callback) {
        if (callback == null)
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, object);
                callback.onFinished();
            }
        });
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher()
                                      .queuedCalls()) {
            if (tag.equals(call.request()
                               .tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher()
                                      .runningCalls()) {
            if (tag.equals(call.request()
                               .tag())) {
                call.cancel();
            }
        }
    }

    public static class METHOD {
        public static final String HEAD   = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT    = "PUT";
        public static final String PATCH  = "PATCH";
    }
}

