/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
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
