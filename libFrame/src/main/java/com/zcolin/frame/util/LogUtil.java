/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */

package com.zcolin.frame.util;

import android.util.Log;

import com.zcolin.frame.app.FramePathConst;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 日志工具类
 */
public class LogUtil {

    public static char    LOG_PRINT_TYPE            = 'v';                            // 输出日志类型，v输出所有信息，i输出（i、d、w、e）信息，d输出（d、w、e信息），w输出（w、e信息），e写入e信息
    public static char    LOG_WRITE_TYPE            = 'w';                            // 写入日志类型，v写入所有信息，i写入（i、d、w、e）信息，d写入（d、w、e信息），w写入（w、e信息），e写入e信息
    public static boolean LOG_DEBUG                 = false;
    public static boolean LOG_WRITE                 = false;
    public static int     SDCARD_LOG_FILE_SAVE_DAYS = 30;                            // sd卡中日志文件的最多保存天数
    public static String  MYLOGFILEName             = "Log.txt";                    // 本类输出的日志文件名称
    public static String  PATH_LOG                  = FramePathConst.getInstance().getPathLog();

    /**
     * 设置文件写入路径
     *
     * @param path 文件的文件夹路径
     */
    public static void setFilePath(String path) {
        PATH_LOG = path;
    }

    /**
     * 警告信息
     */
    public static void w(String tag, Object msg) {
        log(tag, msg.toString(), 'w');
    }

    /**
     * 错误信息
     */
    public static void e(String tag, Object msg) {
        log(tag, msg.toString(), 'e');
    }

    /**
     * 调试信息
     */
    public static void d(String tag, Object msg) {
        log(tag, msg.toString(), 'd');
    }

    /**
     * 一般信息
     */
    public static void i(String tag, Object msg) {
        log(tag, msg.toString(), 'i');
    }

    /**
     * 所有信息
     */
    public static void v(String tag, Object msg) {
        log(tag, msg.toString(), 'v');
    }

    /**
     * 警告信息
     */
    public static void w(String tag, String text) {
        log(tag, text, 'w');
    }

    /**
     * 警告信息
     */
    public static void w(String tag, Throwable throwable) {
        log(tag, ExceptionToString(throwable), 'w');
    }

    /**
     * 错误信息
     */
    public static void e(String tag, String text) {
        log(tag, text, 'e');
    }

    /**
     * 错误信息
     */
    public static void e(String tag, Throwable throwable) {
        log(tag, ExceptionToString(throwable), 'e');
    }

    /**
     * 调试信息
     */
    public static void d(String tag, String text) {
        log(tag, text, 'd');
    }

    /**
     * 一般信息
     */
    public static void i(String tag, String text) {
        log(tag, text, 'i');
    }

    /**
     * 所有信息
     */
    public static void v(String tag, String text) {
        log(tag, text, 'v');
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param level 日志等级  e,w,d,i,v
     */
    private static void log(String tag, String msg, char level) {
        if (msg == null) {
            msg = "null";
        }

        if (LOG_DEBUG) {
            if ('e' == level) {
                logTruncation(tag, msg, level);
            } else if ('w' == level && ('w' == LOG_PRINT_TYPE || 'd' == LOG_PRINT_TYPE || 'i' == LOG_PRINT_TYPE || 'v' == LOG_PRINT_TYPE)) {
                logTruncation(tag, msg, level);
            } else if ('d' == level && ('d' == LOG_PRINT_TYPE || 'i' == LOG_PRINT_TYPE || 'v' == LOG_PRINT_TYPE)) {
                logTruncation(tag, msg, level);
            } else if ('i' == level && ('i' == LOG_PRINT_TYPE || 'v' == LOG_PRINT_TYPE)) {
                logTruncation(tag, msg, level);
            } else if ('v' == level && 'v' == LOG_PRINT_TYPE) {
                logTruncation(tag, msg, level);
            }
        }

        if (LOG_WRITE) {
            boolean flag = false;
            if ('e' == level) {
                flag = true;
            } else if ('w' == level && ('w' == LOG_WRITE_TYPE || 'd' == LOG_WRITE_TYPE || 'i' == LOG_WRITE_TYPE || 'v' == LOG_WRITE_TYPE)) {
                flag = true;
            } else if ('d' == level && ('d' == LOG_WRITE_TYPE || 'i' == LOG_WRITE_TYPE || 'v' == LOG_WRITE_TYPE)) {
                flag = true;
            } else if ('i' == level && ('i' == LOG_WRITE_TYPE || 'v' == LOG_WRITE_TYPE)) {
                flag = true;
            } else if ('v' == level && 'v' == LOG_WRITE_TYPE) {
                flag = true;
            }

            if (flag) {
                writeLogtoFile(String.valueOf(level), tag, msg);
            }
        }
    }

    /**
     * 超4K logcat不输出，认为截断
     * 截断输出日志
     */
    private static void logTruncation(String tag, String msg, char level) {
        if (tag == null || tag.length() == 0 || msg == null || msg.length() == 0) {
            return;
        }

        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            logDispense(tag, msg, level);
        } else {
            while (msg.length() > segmentSize) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                logDispense(tag, logContent, level);
                msg = msg.replace(logContent, "");
            }
            logDispense(tag, msg, level);// 打印剩余日志 
        }
    }

    private static void logDispense(String tag, String msg, char level) {
        switch (level) {
            case 'e':
                Log.e(tag, msg);
                break;
            case 'w':
                Log.w(tag, msg);
                break;
            case 'd':
                Log.d(tag, msg);
                break;
            case 'i':
                Log.i(tag, msg);
                break;
            case 'v':
                Log.v(tag, msg);
                break;
            default:
                Log.v(tag, msg);
        }
    }

    /**
     * 删除指定日期前的日志文件
     */
    public static void delFile() {
        long beforeTime = CalendarUtil.addDays(-SDCARD_LOG_FILE_SAVE_DAYS).getTime();
        File dirFile = new File(PATH_LOG);
        File files[] = dirFile.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.lastModified() < beforeTime) {
                    FileUtil.delete(file);
                }
            }
        }

    }

    /**
     * 将异常实例变为异常详细信息的字符串
     *
     * @param e 异常
     * @return 异常拼接成的字符串
     */
    public static String ExceptionToString(Throwable e) {
        String strError = null;
        if (e == null) {
            strError = "unknown error";
            return strError;
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        return writer.toString();
    }

    /**
     * 写入日志到文件
     *
     * @param mylogtype 日志等级类型
     * @param tag       日志标志
     * @param text      日志内容
     */
    private static void writeLogtoFile(String mylogtype, String tag, String text) {
        String preFileName = CalendarUtil.getDate();
        String needWriteMessage = CalendarUtil.getDateTime() + "    " + mylogtype + "    " + tag + "\n" + text;
        FileUtil.writeFileStr(PATH_LOG + "/" + preFileName + MYLOGFILEName, needWriteMessage);
    }

}
