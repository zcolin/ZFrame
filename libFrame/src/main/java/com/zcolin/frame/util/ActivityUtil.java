/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */

package com.zcolin.frame.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.zcolin.frame.app.BaseFrameActivity;
import com.zcolin.frame.app.ResultActivityHelper;

import java.util.ArrayList;

/**
 * Activity 相关操作 工具类
 */
public class ActivityUtil {

    /**
     * 存储打开的Activity的列表
     */
    private static ArrayList<Activity> ACTIVITY_LIST = new ArrayList<>();

    /**
     * 添加Activity到集合
     */
    public static void addActivityToList(Activity acty) {
        ACTIVITY_LIST.add(acty);
    }

    /**
     * 将Activity从集合移除
     */
    public static void removeActivityFromList(Activity acty) {
        ACTIVITY_LIST.remove(acty);
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        for (Activity acty : ACTIVITY_LIST) {
            if (acty != null && !acty.isFinishing()) {
                acty.finish();
            }
        }

        ACTIVITY_LIST.clear();
    }

    /**
     * 结束某个Activity
     *
     * @param classObj Activity的class对象
     */
    public static void finishActivity(Class<?>... classObj) {
        for (Class<?> obj : classObj) {
            for (Activity acty : ACTIVITY_LIST) {
                if (acty != null && acty.getClass() == obj && !acty.isFinishing()) {
                    acty.finish();
                    break;
                }
            }
        }
    }

    /**
     * 开启新的Activity
     */
    public static void startActivity(Activity context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    /**
     * 开启新的Activity
     */
    public static void startActivity(Activity context, Class<?> cls, String paramName, boolean value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, value);
        context.startActivity(intent);
    }

    /**
     * 开启新的Activity
     */
    public static void startActivity(Activity context, Class<?> cls, String paramName, String value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, value);
        context.startActivity(intent);
    }

    /**
     * 开启新的Activity
     */
    public static void startActivity(Activity context, Class<?> cls, String paramName, int value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, value);
        context.startActivity(intent);
    }

    /**
     * 开启新的Activity
     */
    public static void startActivity(Activity context, Class<?> cls, String paramName, Parcelable value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, value);
        context.startActivity(intent);
    }

    /**
     * 开启新的Activity
     */
    public static void startActivity(Activity context, Class<?> cls, String paramName, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, bundle);
        context.startActivity(intent);
    }

    /**
     * 开启新的Activity
     *
     * @deprecated use {@link BaseFrameActivity#startActivityWithCallback(Intent, ResultActivityHelper.ResultActivityListener)}
     */
    public static void startActivityForResult(Activity context, Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 开启新的Activity
     *
     * @deprecated use {@link BaseFrameActivity#startActivityWithCallback(Intent, ResultActivityHelper.ResultActivityListener)}
     */
    public static void startActivityForResult(Activity context, Class<?> cls, int requestCode, String paramName, Parcelable value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, value);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 开启新的Activity
     *
     * @deprecated use {@link BaseFrameActivity#startActivityWithCallback(Intent, ResultActivityHelper.ResultActivityListener)}
     */
    public static void startActivityForResult(Activity context, Class<?> cls, int requestCode, String paramName, boolean value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, value);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 开启新的Activity
     *
     * @deprecated use {@link BaseFrameActivity#startActivityWithCallback(Intent, ResultActivityHelper.ResultActivityListener)}
     */
    public static void startActivityForResult(Activity context, Class<?> cls, int requestCode, String paramName, int value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, value);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 开启新的Activity
     *
     * @deprecated use {@link BaseFrameActivity#startActivityWithCallback(Intent, ResultActivityHelper.ResultActivityListener)}
     */
    public static void startActivityForResult(Activity context, Class<?> cls, int requestCode, String paramName, String value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, value);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 开启新的Activity
     *
     * @deprecated use {@link BaseFrameActivity#startActivityWithCallback(Intent, ResultActivityHelper.ResultActivityListener)}
     */
    public static void startActivityForResult(Activity context, Class<?> cls, int requestCode, String paramName, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(paramName, bundle);
        context.startActivityForResult(intent, requestCode);
    }


}
