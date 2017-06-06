/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.zcolin.frame.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.zcolin.frame.app.BaseApp;

import java.util.Set;


/**
 * SharedPreferences配置文件读写封装
 */
public class SPUtil {
    public static final String DEFAULT_NAME = "shared_data";

    public static void putString(String key, String value) {
        putString(DEFAULT_NAME, key, value);
    }

    public static String getString(String key, String value) {
        return getString(DEFAULT_NAME, key, value);
    }

    public static void removeString(String key) {
        removeString(DEFAULT_NAME, key);
    }

    public static void putInt(String key, int value) {
        putInt(DEFAULT_NAME, key, value);
    }

    public static int getInt(String key, int value) {
        return getInt(DEFAULT_NAME, key, value);
    }

    public static void putBoolean(String key, boolean value) {
        putBoolean(DEFAULT_NAME, key, value);
    }

    public static boolean getBoolean(String key, Boolean value) {
        return getBoolean(DEFAULT_NAME, key, value);
    }

    public static void putLong(String key, long value) {
        putLong(DEFAULT_NAME, key, value);
    }

    public static long getLong(String key) {
        return getLong(DEFAULT_NAME, key);
    }

    public static void putFloat(String key, float value) {
        putFloat(DEFAULT_NAME, key, value);
    }

    public static float getFloat(String key) {
        return getFloat(DEFAULT_NAME, key);
    }

    public static void putStringSet(String key, Set<String> value) {
        putStringSet(DEFAULT_NAME, key, value);
    }

    public static Set<String> getStringSet(String key) {
        return getStringSet(DEFAULT_NAME, key);
    }

    public static void remove(String key) {
        remove(DEFAULT_NAME, key);
    }

    public static boolean contains(String key) {
        return contains(DEFAULT_NAME, key);
    }

    public static void putString(String fileName, String key, String value) {
        SharedPreferences sp = BaseApp.APP_CONTEXT.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String fileName, String key, String value) {
        SharedPreferences sp = BaseApp.APP_CONTEXT.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getString(key, value);
    }

    public static void removeString(String fileName, String key) {
        SharedPreferences sp = BaseApp.APP_CONTEXT.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void putInt(String fileName, String key, int value) {
        SharedPreferences sp = BaseApp.APP_CONTEXT.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String fileName, String key, int value) {
        SharedPreferences sp = BaseApp.APP_CONTEXT.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getInt(key, value);
    }

    public static void putBoolean(String fileName, String key, boolean value) {
        SharedPreferences sp = BaseApp.APP_CONTEXT.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String fileName, String key, Boolean value) {
        SharedPreferences sp = BaseApp.APP_CONTEXT.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getBoolean(key, value);
    }

    public static void putLong(String fileName, String key, long value) {
        SharedPreferences sp = BaseApp.APP_CONTEXT.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(String fileName, String key) {
        SharedPreferences sp = BaseApp.APP_CONTEXT.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getLong(key, 0L);
    }

    public static void putFloat(String fileName, String key, float value) {
        SharedPreferences sp = BaseApp.APP_CONTEXT.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static float getFloat(String fileName, String key) {
        SharedPreferences sp = BaseApp.APP_CONTEXT.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getFloat(key, 0f);
    }

    public static void putStringSet(String fileName, String key, Set<String> value) {
        SharedPreferences sp = BaseApp.APP_CONTEXT.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    public static Set<String> getStringSet(String fileName, String key) {
        SharedPreferences sp = BaseApp.APP_CONTEXT.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getStringSet(key, null);
    }


    public static void remove(String fileName, String key) {
        SharedPreferences sp = BaseApp.APP_CONTEXT.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public static boolean contains(String fileName, String key) {
        SharedPreferences sp = BaseApp.APP_CONTEXT.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.contains(key);
    }
}
