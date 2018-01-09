/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */

package com.zcolin.frame.demo.http.entity;

import com.zcolin.frame.http.ZReply;

/**
 * Http报文返回数据的基类，如果使用ZResponse直接获取实体，则实体需要继承此基类
 */
public class HttpBaseReplyBean implements ZReply {
    public int    code;
    public String message;

    @Override
    public boolean isSuccess() {
        return code == 200;
    }

    @Override
    public int getReplyCode() {
        return code;
    }

    @Override
    public String getErrorMessage() {
        return message;
    }
}
