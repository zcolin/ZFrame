/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.fosung.frame.app;

import android.os.Build;

import com.fosung.frame.utils.AppUtil;
import com.fosung.frame.utils.CalendarUtil;
import com.fosung.frame.utils.FileUtil;
import com.fosung.frame.utils.LogUtil;
import com.fosung.frame.utils.ToastUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 异常捕获
 */
final class CrashHandler implements UncaughtExceptionHandler {
    private static final Object LOCK               = new Object();
    private static final String mExceptionFileName = "log.txt";
    private static final String TAG                = "CrashHandler";

    private static volatile CrashHandler INSTANCE = null;
    private UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                INSTANCE = new CrashHandler();
            }
        }
        return INSTANCE;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogUtil.e("uncaughtException", LogUtil.ExceptionToString(ex));

        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //如果把这句话注释掉，有异常都不会退出
            AppUtil.quitSystem();
            System.exit(10);
        }
    }

    /**
     * 处理捕获到的异常
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        new Thread() {
            @Override
            public void run() {
                ToastUtil.toastShort("很抱歉，程序出现异常，即将退出");
            }
        }.start();


        return true;
    }

    /**
     * 将异常日志写入文件
     */
    private void writeExceptionToFile(String message) {
        File crashFile = new File(FramePathConst.getInstance()
                                                .getPathLog() + "/crash_" + CalendarUtil.getDate() + mExceptionFileName);
        if (!crashFile.exists()) {
            FileUtil.createFile(crashFile.getPath());
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CalendarUtil.getDateTime());
        stringBuilder.append(collectDeviceInfo());
        stringBuilder.append("\n\n");
        stringBuilder.append(message);
        stringBuilder.append("\n");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(crashFile, true));
            writer.append(stringBuilder);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 收集设备参数信息
     */
    private StringBuilder collectDeviceInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append("\nversionName:" + AppUtil.getVersionName(BaseApp.APP_CONTEXT));
        builder.append("\nversionCode:" + String.valueOf(AppUtil.getVersionCode(BaseApp.APP_CONTEXT)));
        builder.append("\nMODEL:" + String.valueOf(Build.MODEL));
        builder.append("\nSDK_INT:" + String.valueOf(Build.VERSION.SDK_INT));
        return builder;
    }
}
