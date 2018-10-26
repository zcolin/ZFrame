/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-10-26 上午11:13
 * ********************************************************
 */

package com.zcolin.frame.http;


import java.util.LinkedHashMap;

/**
 * 请求拦截器
 */
public class ZRequestIntercept<T extends ZReply> {


    /**
     * 请求的拦截器
     */
    public boolean onRequest(String url, LinkedHashMap<String, String> headerParams, Object contentParams, Object response) {
        return false;
    }
}
