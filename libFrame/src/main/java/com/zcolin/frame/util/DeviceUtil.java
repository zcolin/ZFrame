package com.zcolin.frame.util;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;
import java.util.UUID;

/**
 * 获取设备信息工具类
 */
public class DeviceUtil {

    /**
     * 获取设备唯一编码方案：
     * SDK_INT < 23: IMEI > MAC地址(直接通过WifiManager获取) > Android_Id > Serial > UUID
     * SDK_INT >= 23: IMEI > MAC地址(读取系统文件或通过NetWorkInterface获取) > Android_Id > Serial > UUID
     */
    public static String getDeviceUniqueId(Context context) {
        String str = "";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            str = getDeviceId(context);
            if (TextUtils.isEmpty(str)) {
                str = getMacAddressFromWifiManager(context);
                if (TextUtils.isEmpty(str)) {
                    str = getAndroidId(context);
                    if (TextUtils.isEmpty(str)) {
                        str = getSerial(context);
                        if (TextUtils.isEmpty(str)) {
                            str = getUUID();
                        }
                    }
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            str = getDeviceId(context);
            if (TextUtils.isEmpty(str)) {
                str = getMacAddressFromNetworkInterface();
                if (TextUtils.isEmpty(str)) {
                    str = getMacAddressFromFile();
                    // 或者 str = getMacAddressFromNetworkInterface();
                    if (TextUtils.isEmpty(str)) {
                        str = getAndroidId(context);
                        if (TextUtils.isEmpty(str)) {
                            str = getSerial(context);
                            if (TextUtils.isEmpty(str)) {
                                str = getUUID();
                            }
                        }
                    }
                }
            }
        }
        return str;
    }

    /**
     * 获取IMEI（android10及以上无效）
     * （android6.0及以上调用此函数需要申请权限：Manifest.permission.READ_PHONE_STATE）
     */
    public static String getDeviceId(Context context) {
        if (PackageManager.PERMISSION_GRANTED == context.getPackageManager()
                                                        .checkPermission("android.permission.READ_PHONE_STATE",
                                                                         context.getPackageName())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return ((TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE)).getImei();
            } else {
                return getDeviceId(context);
            }
        }
        return "";
    }

    /**
     * 通过WifiManager获取Mac地址,并且wifi需要是开启状态
     * （android6.0以下使用，调用此函数需要申请权限：Manifest.permission.ACCESS_WIFI_STATE；从Android 6.0开始，使用该方法android.net.wifi
     * .WifiManager#getConnectionInfo()
     * 获取到的mac地址都为02:00:00:00:00:00 无法使用）
     */
    public static String getMacAddressFromWifiManager(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            if (PackageManager.PERMISSION_GRANTED == context.getPackageManager()
                                                            .checkPermission("android.permission.ACCESS_WIFI_STATE",
                                                                             context.getPackageName())) {
                WifiInfo wifiInfo = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
                return wifiInfo.getMacAddress();
            }
        }
        return "";
    }

    /**
     * 通过文件获取Mac地址
     * （android10.0也可用）
     */
    public static String getMacAddressFromFile() {
        String[] arrayOfString = {"/sys/class/net/wlan0/address",
                                  "/sys/class/net/eth0/address",
                                  "/sys/devices/virtual/net/wlan0/address"};
        for (byte b = 0; b < arrayOfString.length; b++) {
            if (readFile(arrayOfString[b]) != null) {
                return readFile(arrayOfString[b]);
            }
        }
        return "";
    }

    /**
     * 通过NetWorkInterface获取Mac地址
     * （android10.0也可用）
     */
    public static String getMacAddressFromNetworkInterface() {
        try {
            Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) enumeration.nextElement();
                if ("wlan0".equals(networkInterface.getName()) || "eth0".equals(networkInterface.getName())) {
                    byte[] arrayOfByte = networkInterface.getHardwareAddress();
                    if (arrayOfByte == null || arrayOfByte.length == 0) {
                        return null;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    for (byte b1 : arrayOfByte) {
                        stringBuilder.append(String.format("%02X:", new Object[]{Byte.valueOf(b1)}));
                    }
                    if (stringBuilder.length() > 0) {
                        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    }
                    return stringBuilder.toString().toLowerCase(Locale.getDefault());
                }
            }
        } catch (Throwable throwable) {

        }
        return "";
    }

    /**
     * 获取设备Android_ID
     * （不需要申请权限）
     */
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取设备序列号（android10及以上无效）
     * （android6.0及以上调用此函数需要申请权限：Manifest.permission.READ_PHONE_STATE）
     */
    public static String getSerial(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return Build.SERIAL;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (PackageManager.PERMISSION_GRANTED == context.getPackageManager()
                                                            .checkPermission("android.permission.READ_PHONE_STATE",
                                                                             context.getPackageName())) {
                return Build.getSerial();
            }
        }
        return "";
    }

    /**
     * 如果用户拒绝了权限或以上所有获取id的方法都获取失败，则需要生成一个本次安装的唯一ID
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
        @SuppressLint("InvalidWakeLockTag")
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
            @SuppressLint("InvalidWakeLockTag")
            PowerManager.WakeLock wl =
                    pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,
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
            @SuppressLint("InvalidWakeLockTag")
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

    private static String readFile(String param) {
        String str = null;
        try {
            FileReader fileReader = new FileReader(param);
            BufferedReader bufferedReader = null;
            if (fileReader != null) {
                try {
                    bufferedReader = new BufferedReader(fileReader, 1024);
                    str = bufferedReader.readLine();
                } finally {
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (Throwable throwable) {
                        }
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (Throwable throwable) {
                        }
                    }
                }
            }
        } catch (Throwable throwable) {

        }
        return str;
    }

}
