/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */

package com.zcolin.frame.http.okhttp.callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public abstract class Callback<T> {
    public static Callback CALLBACK_DEFAULT = new Callback() {

        @Override
        public Object parseNetworkResponse(Response response) throws Exception {
            return null;
        }

        @Override
        public void onError(int code, Call call, Exception e) {

        }

        @Override
        public void onSuccess(Response response, Object resObj) {

        }
    };

    /**
     * UI Thread
     */
    public void onStart(Request request) {
    }

    /**
     * UI Thread
     */
    public void onFinished() {
    }


    /**
     * UI Thread
     */
    public void onCanceled() {
    }

    /**
     * UI Thread
     */
    public void onProgress(float progress, long total) {

    }

    /**
     * if you parse reponse code in parseNetworkResponse, you should make this method return true.
     */
    public boolean validateReponse(Response response) {
        return response.isSuccessful();
    }

    /**
     * Thread Pool Thread
     */
    public abstract T parseNetworkResponse(Response response) throws Exception;

    public abstract void onError(int code, Call call, Exception e);

    public abstract void onSuccess(Response response, T resObj);

}