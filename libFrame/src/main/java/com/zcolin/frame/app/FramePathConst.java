/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.zcolin.frame.app;

import android.os.Environment;

import java.util.UUID;

/**
 * 程序路径类
 * <p>
 * 如果需要更改路径，继承此类，重写get**（）函数
 */
public class FramePathConst {
    private String PATH_SDCARD;//SD卡路径
    private String PATH_SDCARD_APP;//程序的SD路径，访问需要用户确认
    private String PATH_IMG_CACHE;//缓存图片路径
    private String PATH_TEMP;//暂存路径
    private String PATH_FILE;//文件路径
    private String PATH_LOG; //日志路径

    private static class InstancesClass {
        private static FramePathConst instance = new FramePathConst();
    }

    public static FramePathConst getInstance() {
        return InstancesClass.instance;
    }

    public FramePathConst() {
        boolean sdCardExist = Environment.getExternalStorageState()
                                         .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在 
        if (sdCardExist) {
            PATH_SDCARD = Environment.getExternalStorageDirectory()
                                     .getPath();//获取根目录 
        } else {
            PATH_SDCARD = Environment.getRootDirectory()
                                     .getAbsolutePath();//获取根目录
        }

        PATH_SDCARD_APP = PATH_SDCARD + "/frame/";
        PATH_IMG_CACHE = BaseApp.APP_CONTEXT.getExternalCacheDir() + "/img_cache/";
        PATH_TEMP = BaseApp.APP_CONTEXT.getExternalCacheDir() + "/temp/";
        PATH_LOG = BaseApp.APP_CONTEXT.getExternalFilesDir(null) + "/log/";
        PATH_FILE = BaseApp.APP_CONTEXT.getExternalFilesDir(null) + "/file/";
    }

    public String getPathSdcard() {
        return PATH_SDCARD;
    }

    public String getPathSdcardApp() {
        return PATH_SDCARD_APP;
    }

    public String getPathImgCache() {
        return PATH_IMG_CACHE;
    }

    public String getPathTemp() {
        return PATH_TEMP;
    }

    public String getPathFile() {
        return PATH_FILE;
    }

    public String getPathLog() {
        return PATH_LOG;
    }

    public String getTempFilePath(String suffix) {
        return getPathTemp() + UUID.randomUUID()
                                   .toString() + "." + suffix;
    }
}
