/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.zcolin.frame.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;


/**
 * MD5加密 生成32位md5码
 */
public class MD5Util {

    /**
     * MD5 加密
     *
     * @param str 需要加密的字符串
     * @return 加密后的字符串，if null， return "", never return null
     */
    public static String getMD5Str(String str) {
        String strMd5 = null;
        if (str == null || str.length() == 0)
            return "";
        try {
            strMd5 = getMD5Str(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return strMd5 == null ? "" : strMd5;
    }

    /**
     * MD5 加密
     *
     * @param arrbyte 需要加密的字节数组
     * @return 加密后的字符串，if null， return "", never return null
     */
    public static String getMD5Str(byte[] arrbyte) {
        String strMd5 = null;
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            if (messageDigest == null)
                return "";

            messageDigest.reset();
            messageDigest.update(arrbyte);
            byte[] byteArray = messageDigest.digest();
            strMd5 = ByteUtil.toHexString(byteArray)
                             .toString();
        } catch (Exception e) {
            strMd5 = "";
        }
        return strMd5;
    }

    /**
     * 获取文件的MD5值
     *
     * @param file 文件
     * @return 获取的MD5值，if null， return "", never return null
     */
    public static String getFileMD5String(File file) {
        String strMd5 = null;
        MessageDigest messageDigest = null;
        FileInputStream in = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            if (messageDigest == null)
                return "";

            messageDigest.reset();
            in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            messageDigest.update(byteBuffer);
            byte[] byteArray = messageDigest.digest();
            strMd5 = ByteUtil.toHexString(byteArray)
                             .toString();
        } catch (Exception e) {
            strMd5 = "";
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return strMd5;
    }
}
