/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-3-29 上午11:01
 * ********************************************************
 */

package com.zcolin.frame.util;

import android.app.Activity;
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

    /**
     * 根据版本获取Uri, AndroidN之前直接返回，之后自动使用FileProvider转换提供的content uri
     */
    public static Uri get(Uri uri) {
        if (uri == null)
            return null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
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
            path = new File(uri.getPath()
                               .replace("file_path/", "")).getAbsolutePath();
        } else {
            path = uri.getPath();
        }
        return path;
    }

    public static File getImageFileFromUri(Uri uri) throws IllegalArgumentException {
        return getFileFromUri(MediaStore.Images.Media.DATA, uri);
    }

    public static File getVideoFileFromUri(Uri uri, Activity activity) throws IllegalArgumentException {
        return getFileFromUri(MediaStore.Video.Media.DATA, uri);
    }

    /**
     * 通过URI获取文件的路径
     */
    public static String getFilePathFromUri(String dataType, Uri uri) throws IllegalArgumentException {
        if (uri == null) {
            throw new IllegalArgumentException("uri is null,activity may have been recovered?");
        }

        File file = getFileFromUri(dataType, uri);
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
    public static File getFileFromUri(String dataType, Uri uri) {
        String picturePath = null;
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            String[] filePathColumn = {dataType};
            Cursor cursor = BaseApp.APP_CONTEXT.getContentResolver()
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

            if (!flag && TextUtils.equals(uri.getAuthority(), getProviderName())) {
                picturePath = parseOwnUri(uri);
            }
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            picturePath = uri.getPath();
        }
        return TextUtils.isEmpty(picturePath) ? null : new File(picturePath);
    }
    

    /**
     * 转换 content:// uri
     */
    public static Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = BaseApp.APP_CONTEXT.getContentResolver()
                                           .query(
                                                   MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                   new String[]{MediaStore.Images.Media._ID},
                                                   MediaStore.Images.Media.DATA + "=? ",
                                                   new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return BaseApp.APP_CONTEXT.getContentResolver()
                                          .insert(
                                                  MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    private static String getProviderName() {
        return BaseApp.APP_CONTEXT.getPackageName() + ".fileprovider";
    }
}