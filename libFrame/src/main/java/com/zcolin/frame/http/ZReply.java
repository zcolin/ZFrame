/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */

package com.zcolin.frame.http;

/**
 * Http报文返回数据的处理接口
 * 如果使用{@link com.zcolin.frame.http.response.ZResponse},则实体需要实现此接口
 */
public interface ZReply {
    /**
     * 是否是成功的信息，用来判定服务器处理了信息之后返回的是否为正确的数据
     *
     * @return 是否是约定的请求成功
     */
    boolean isSuccess();

    /**
     * 获取和服务器约定的返回码
     *
     * @return 服务器返回的状态码
     */
    int getReplyCode();

    /**
     * 获取 如果请求错误的错误信息
     *
     * @return 服务器返回的错误信息
     */
    String getErrorMessage();
}
