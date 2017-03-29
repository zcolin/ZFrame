/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-3-29 上午11:01
 * ********************************************************
 */

package com.zcolin.frame.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 适配Android7.0的Uri解析工具类
 */
public class NUriParse {

    /**
     * 根据版本获取Uri, AndroidN之前直接返回，之后自动使用FileProvider转换提供的content uri
     */
    public static Uri get(Context context, Uri uri) {
        if (uri == null)
            return null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return uri;
        } else if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            return getUriFromPath(context, uri.getPath());
        }
        return uri;
    }

    /**
     * 获取一个临时的Uri, 文件名随机生成
     */
    public static Uri getTempUri(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return getUriFromPath(context, timeStamp);
    }

    /**
     * 创建一个用于FileProvider输出路径的Uri (FileProvider)
     */
    public static Uri getUriFromPath(Context context, String path) {
        File file = new File(path);
        return getUriFromFile(context, file);
    }

    /**
     * 创建一个用于FileProvider输出路径的Uri (FileProvider)
     */
    public static Uri getUriFromFile(Context context, File file) {
        FileUtil.checkFilePath(file, false);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Uri.fromFile(file);
        } else {
            return FileProvider.getUriForFile(context, getProviderName(context), file);
        }
    }

    /**
     * 将TakePhoto 提供的Uri 解析出文件绝对路径
     */
    public static String parseOwnUri(Context context, Uri uri) {
        if (uri == null)
            return null;

        String path;
        if (TextUtils.equals(uri.getAuthority(), getProviderName(context))) {
            path = new File(uri.getPath()
                               .replace("file_path/", "")).getAbsolutePath();
        } else {
            path = uri.getPath();
        }
        return path;
    }

    public static String getImagePathFromUri(Uri uri, Activity activity) throws IllegalArgumentException {
        return getFilePathFromUri(MediaStore.Images.Media.DATA, uri, activity);
    }

    public static String getVideoPathFromUri(Uri uri, Activity activity) throws IllegalArgumentException {
        return getFilePathFromUri(MediaStore.Video.Media.DATA, uri, activity);
    }

    /**
     * 通过URI获取文件的路径
     */
    public static String getFilePathFromUri(String dataType, Uri uri, Activity activity) throws IllegalArgumentException {
        if (uri == null) {
            throw new IllegalArgumentException("uri is null,activity may have been recovered?");
        }

        File file = getFileFromUri(dataType, uri, activity);
        String filePath = file == null ? null : file.getPath();
        if (TextUtils.isEmpty(filePath))
            throw new IllegalArgumentException("uri parse fail");
        return filePath;
    }

    /**
     * 通过URI获取文件
     *
     * @param dataType ex:MediaStore.Images.Media.DATA/MediaStore.Video.Media.DATA
     */
    public static File getFileFromUri(String dataType, Uri uri, Activity activity) {
        String picturePath = null;
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            String[] filePathColumn = {dataType};
            Cursor cursor = activity.getContentResolver()
                                    .query(uri, filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
            boolean flag = false;
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                if (columnIndex >= 0) {
                    picturePath = cursor.getString(columnIndex);  //获取文件路径
                    flag = true;
                }
                cursor.close();
            }

            if (!flag && TextUtils.equals(uri.getAuthority(), getProviderName(activity))) {
                picturePath = parseOwnUri(activity, uri);
            }
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            picturePath = uri.getPath();
        }
        return TextUtils.isEmpty(picturePath) ? null : new File(picturePath);
    }

    private static String getProviderName(Context context) {
        return context.getPackageName() + ".fileprovider";
    }
}