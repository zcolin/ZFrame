/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */

package com.zcolin.frame.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.zcolin.frame.app.BaseApp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 适配Android7.0的Uri解析工具类
 */
public class NUriParseUtil {
    private static String applicationId;

    /**
     * 根据版本获取Uri, AndroidN之前直接返回，之后自动使用FileProvider转换提供的content uri
     */
    public static Uri get(Uri uri) {
        if (uri == null)
            return null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N || BaseApp.APP_CONTEXT.getApplicationInfo().targetSdkVersion <= 23) {
            return uri;
        } else if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            return getUriFromPath(uri.getPath());
        }
        return uri;
    }

    /**
     * 获取一个临时的Uri, 文件名随机生成
     */
    public static Uri getTempUri() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return getUriFromPath(timeStamp);
    }

    /**
     * 创建一个用于FileProvider输出路径的Uri (FileProvider)
     */
    public static Uri getUriFromPath(String path) {
        File file = new File(path);
        return getUriFromFile(file);
    }

    /**
     * 创建一个用于FileProvider输出路径的Uri (FileProvider)
     */
    public static Uri getUriFromFile(File file) {
        FileUtil.checkFilePath(file, false);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Uri.fromFile(file);
        } else {
            return FileProvider.getUriForFile(BaseApp.APP_CONTEXT, getProviderName(), file);
        }
    }

    /**
     * 将TakePhoto 提供的Uri 解析出文件绝对路径
     */
    public static String parseOwnUri(Uri uri) {
        if (uri == null)
            return null;

        String path;
        if (TextUtils.equals(uri.getAuthority(), getProviderName())) {
            path = new File(uri.getPath().replace("z-root-path/", "")).getAbsolutePath();
            path = new File(path.replace("z-file_path/", "")).getAbsolutePath();
            path = new File(path.replace("z-cache-path/", "")).getAbsolutePath();
            path = new File(path.replace("z-external-path/", "")).getAbsolutePath();
            path = new File(path.replace("z-external-files-path/", "")).getAbsolutePath();
            path = new File(path.replace("z-external-cache-path/", "")).getAbsolutePath();
        } else {
            path = uri.getPath();
        }
        return path;
    }

    /**
     * 通过URI获取文件
     */
    public static File getFileFromUri(Uri uri) {
        String filePath = getFilePathFromUri(uri);
        if (TextUtils.isEmpty(filePath))
            throw new IllegalArgumentException("uri parse fail");
        return TextUtils.isEmpty(filePath) ? null : new File(filePath);
    }

    /**
     * 通过URI获取文件路径
     */
    public static String getFilePathFromUri(Uri uri) {
        String picturePath = null;
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            String[] filePathColumn = {"_data"};
            Cursor cursor = BaseApp.APP_CONTEXT.getContentResolver().query(uri, filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
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

            if (!flag && TextUtils.equals(uri.getAuthority(), getProviderName())) {
                picturePath = parseOwnUri(uri);
            }
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            picturePath = uri.getPath();
        }
        return picturePath;
    }


    /**
     * 转换 content:// uri
     */
    public static Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = BaseApp.APP_CONTEXT.getContentResolver()
                                           .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, MediaStore.Images
                                                   .Media.DATA + "=? ", new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return BaseApp.APP_CONTEXT.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    private static String getProviderName() {
        return (applicationId == null ? BaseApp.APP_CONTEXT.getPackageName() : applicationId) + ".zframe_fileprovider";
    }

    /**
     * 如果自定义此设置，则默认的providerName不生效，此配置用户applicationId和packageName不一样时使用
     */
    public static void setApplicationId(String name) {
        applicationId = name;
    }
}