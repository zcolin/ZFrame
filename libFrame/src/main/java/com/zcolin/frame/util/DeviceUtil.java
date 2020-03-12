/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */

package com.zcolin.frame.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.zcolin.frame.permission.PermissionsResultAction;

import java.util.UUID;

/**
 *
 */
public class DeviceUtil {

    /**
     * 调用次函数需要调用{@link com.zcolin.frame.permission.PermissionHelper#requestReadPhoneStatePermission(Object, PermissionsResultAction)}获取权限
     * pad可能会获取不到
     */
    public static String getDeviceId(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 如果用户拒绝了权限，即{@link #getDeviceId(Context)}没有获取到结果，则需要生成一个本次安装的唯一ID
     */
    public static String getUUID() {
        String str = SPUtil.getString("device_app_uuid", null);
        if (str == null) {
            str = UUID.randomUUID().toString();
            SPUtil.putString("device_app_uuid", str);
        }
        return str;
    }


    /**
     * 设置字体缩放级别
     *
     * @param scale 缩放倍数
     */
    public static void setSysFontScale(Context ctx, float scale) {
        try {
            Resources res = ctx.getResources();
            Configuration cfg = res.getConfiguration();
            cfg.fontScale = scale;
            res.updateConfiguration(cfg, null);
        } catch (Exception re) {
            re.printStackTrace();
        }
    }

    /**
     * 获取字体缩放级别
     */
    public static float getSysFontScale(Context ctx) {
        float fontScale = 1;
        try {
            Resources res = ctx.getResources();
            Configuration cfg = res.getConfiguration();
            fontScale = cfg.fontScale;
        } catch (Exception re) {
            re.printStackTrace();
        }
        return fontScale;
    }

    /**
     * 是否是平板设备
     */
    public static boolean isTablet(Context context) {
        return ScreenUtil.isTablet(context);
    }

    /**
     * 设置屏保时间
     *
     * @param time 屏保时间秒
     */
    public static void setScreenSaverTime(Context context, int time) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, time);
    }

    /**
     * 获取系统屏保时间
     */
    public static int getScreenSaverTime(Context context) {
        int result = 0;
        try {
            result = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 点亮屏幕
     */
    public static void acquireScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        mWakeLock.acquire();
        mWakeLock.release();
    }

    /**
     * 解锁屏幕、强制点亮屏幕
     * <p>
     * <pre>
     * 	标记值						CPU		屏幕				键盘
     * 	PARTIAL_WAKE_LOCK			开启		关闭				关闭
     * 	SCREEN_DIM_WAKE_LOCK		开启		调暗（Dim）		关闭
     * 	SCREEN_BRIGHT_WAKE_LOCK		开启		调亮（Bright）	关闭
     * 	FULL_WAKE_LOCK				开启		调亮（Bright）	调亮（Bright）
     *  </pre>
     */
    public static void acquireWakeLock(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,
                    "SimpleTimer");
            if (!wl.isHeld()) {
                wl.acquire();
                wl.release();
            }
        }
    }

    /**
     * 关掉屏幕
     */
    public static void acquireUNWakeLock(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm.isScreenOn()) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WhatEver");
            wl.acquire();
            wl.release();
        }
    }

    /**
     * 禁用键盘锁 、解锁键盘
     */
    public static void disableKeylock(Context context) {
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock mKeyguardLock = mKeyguardManager.newKeyguardLock("");
        mKeyguardLock.disableKeyguard();
    }
}
