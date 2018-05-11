/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-5-11 上午8:50
 * ********************************************************
 */

package com.zcolin.frame.http.response;


import com.zcolin.frame.http.ZReply;

import okhttp3.Response;


/**
 * ZResponse拦截器
 */
public class ZResponseIntercept<T extends ZReply> {

    /**
     * 调用成功的拦截器
     */
    public boolean onSuccessIntercept(Response response, T resObj) {
        return false;
    }

    /**
     * 调用失败的拦截器
     */
    public boolean onErrorIntercept(int code, String error) {
        return false;
    }
}
