/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */

package com.zcolin.frame.app;

import android.app.Application;
import android.content.Context;


/**
 * 程序入口的基类, 具体到应用的程序入口需要继承此类
 * <p/>
 * 1 公用ApplicationCntext的赋值
 * 2 异常处理
 */
public class BaseApp extends Application {

    public static Context APP_CONTEXT;

    @Override
    public void onCreate() {
        APP_CONTEXT = this;
        super.onCreate();

        if (isSetDefaultUncaughtExceptionHandler()) {
            Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance());
        }
    }

    protected boolean isSetDefaultUncaughtExceptionHandler() {
        return true;
    }
}
