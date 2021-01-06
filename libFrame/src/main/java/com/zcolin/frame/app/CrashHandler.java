/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */

package com.zcolin.frame.app;

import android.os.Build;

import com.zcolin.frame.util.AppUtil;
import com.zcolin.frame.util.CalendarUtil;
import com.zcolin.frame.util.FileUtil;
import com.zcolin.frame.util.LogUtil;
import com.zcolin.frame.util.ToastUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 异常捕获
 */
final class CrashHandler implements UncaughtExceptionHandler {
    private static final Object LOCK                = new Object();
    private static final String EXCEPTION_FILE_NAME = "log.txt";
    private static final String TAG                 = "CrashHandler";

    private static volatile CrashHandler             INSTANCE = null;
    private                 UncaughtExceptionHandler mDefaultHandler;

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
            // 此处只执行写入到本地的操作，建议下一次打开应用程序时再执行相对比较耗时的上传log到服务器的操作，防止线程被杀死而无法完成上传。
            writeExceptionToFile(ex);
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

        // TODO: 2021/1/6  debug：主线程被杀死了，Toast无法正常执行，目前暂无法解决。
        new Thread(() -> ToastUtil.toastShort("很抱歉，程序出现异常，即将退出"));

        return true;
    }

    /**
     * 将异常日志写入文件
     */
    private void writeExceptionToFile(Throwable ex) {
        File crashFile = new File(FramePathConst.getInstance()
                                                .getPathLog() + "/crash_" + CalendarUtil.getDate() + EXCEPTION_FILE_NAME);
        if (!crashFile.exists()) {
            FileUtil.createFile(crashFile.getPath());
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CalendarUtil.getDateTime());
        stringBuilder.append(collectDeviceInfo());
        stringBuilder.append("\n\n");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(crashFile, true));
            writer.append(stringBuilder);
            ex.printStackTrace(writer);
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
