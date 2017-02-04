package com.zcolin.frame.utils;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.zcolin.frame.permission.PermissionsResultAction;

/**
 *
 */
public class DeviceUtil {

    /**
     * 综合多个获取的id，重复几率较小
     */
    public static String getUniqueId(Context context) {
        //deviceId 可能获取不到
        String str1 = getDeviceId(context);
        //设备信息
        String str2 = "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10;
        //androidId 可能为空
        String str3 = Settings.Secure.getString(context.getContentResolver(), "android_id");
        //mac地址不能使用，6.0之后户籍哦去的都是一样的
        String str4 = str1 + str2 + str3;
        str4 = MD5Util.getMD5Str(str4);
        return str4;
    }

    /**
     * 调用次函数需要调用{@link com.zcolin.frame.permission.PermissionHelper#requestReadPhoneStatePermission(Object, PermissionsResultAction)}获取权限
     * pad可能会获取不到
     */
    public static String getDeviceId(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
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
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "SimpleTimer");
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
