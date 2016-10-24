/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-21 下午1:53
 * *********************************************************
 */

package com.fosung.usedemo.http.entity;

/**
 * Http报文返回数据的基类，如果使用ZResponse直接获取实体，则实体需要继承此基类
 */
public class HttpBaseReplyBean {
    public int code;
    public String message;
}
