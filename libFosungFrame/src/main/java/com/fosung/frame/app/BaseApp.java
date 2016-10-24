/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.fosung.frame.app;

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

        Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance());
    }
}
