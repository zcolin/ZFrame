/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */

package com.zcolin.frame.permission;

import android.Manifest;

import com.zcolin.frame.app.BaseFrameActivity;
import com.zcolin.frame.app.BaseFrameFrag;

/**
 * 申请权限辅助类，此类之包含一些常用的权限
 */
public class PermissionHelper {

    /**
     * 申请特殊权限或者权限集合
     *
     * @param context 只能是BaseFrameActivity或者BaseFrameFrag 的子类
     */
    public static void requestPermission(Object context, String[] arrayPermission, PermissionsResultAction action) {
        if (context instanceof BaseFrameActivity) {
            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult((BaseFrameActivity) context, arrayPermission, action);
        } else if (context instanceof BaseFrameFrag) {
            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult((BaseFrameFrag) context, arrayPermission, action);
        } else {
            throw new IllegalArgumentException("context 必须是BaseFrameFrag或者BaseFrameActivity的子类");
        }
    }

    /**
     * 申请定位权限
     *
     * @param context 只能是BaseFrameActivity或者BaseFrameFrag 的子类
     */
    public static void requestLocationPermission(Object context, PermissionsResultAction action) {
        requestPermission(context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, action);
    }

    /**
     * 申请写入SD卡权限
     *
     * @param context 只能是BaseFrameActivity或者BaseFrameFrag 的子类
     */
    public static void requestWriteSdCardPermission(Object context, PermissionsResultAction action) {
        requestPermission(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, action);
    }

    /**
     * 申请读取SD卡权限
     *
     * @param context 只能是BaseFrameActivity或者BaseFrameFrag 的子类
     */
    public static void requestReadSdCardPermission(Object context, PermissionsResultAction action) {
        requestPermission(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, action);
    }

    /**
     * 申请读写SD卡权限
     *
     * @param context 只能是BaseFrameActivity或者BaseFrameFrag 的子类
     */
    public static void requestReadWriteSdCardPermission(Object context, PermissionsResultAction action) {
        requestPermission(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, action);
    }

    /**
     * 申请调用摄像头权限
     *
     * @param context 只能是BaseFrameActivity或者BaseFrameFrag 的子类
     */
    public static void requestCameraPermission(Object context, PermissionsResultAction action) {
        requestPermission(context, new String[]{Manifest.permission.CAMERA}, action);
    }

    /**
     * 申请读取联系人权限
     *
     * @param context 只能是BaseFrameActivity或者BaseFrameFrag 的子类
     */
    public static void requestReadContactsPermission(Object context, PermissionsResultAction action) {
        requestPermission(context, new String[]{Manifest.permission.READ_CONTACTS}, action);
    }

    /**
     * 申请读取设备码权限
     *
     * @param context 只能是BaseFrameActivity或者BaseFrameFrag 的子类
     */
    public static void requestReadPhoneStatePermission(Object context, PermissionsResultAction action) {
        requestPermission(context, new String[]{Manifest.permission.READ_PHONE_STATE}, action);
    }

    /**
     * 申请拨打电话权限
     *
     * @param context 只能是BaseFrameActivity或者BaseFrameFrag 的子类
     */
    public static void requestCallPhonePermission(Object context, PermissionsResultAction action) {
        requestPermission(context, new String[]{Manifest.permission.CALL_PHONE}, action);
    }
}
