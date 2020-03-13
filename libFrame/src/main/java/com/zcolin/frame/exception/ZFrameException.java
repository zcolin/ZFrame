/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午6:31
 * ********************************************************
 */

package com.zcolin.frame.exception;

import com.zcolin.frame.util.ExceptionUtil;
import com.zcolin.frame.util.StringUtil;

/**
 * 工具类异常
 */
public class ZFrameException extends RuntimeException {

    public ZFrameException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public ZFrameException(String message) {
        super(message);
    }

    public ZFrameException(String messageTemplate, Object... params) {
        super(StringUtil.format(messageTemplate, params));
    }

    public ZFrameException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ZFrameException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringUtil.format(messageTemplate, params), throwable);
    }
}
