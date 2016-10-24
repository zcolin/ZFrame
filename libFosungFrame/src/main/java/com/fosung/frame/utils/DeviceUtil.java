package com.fosung.frame.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;

import com.fosung.frame.permission.PermissionsResultAction;

/**
 *
 */
public class DeviceUtil {

    /**
     * 调用次函数需要调用{@link com.fosung.frame.permission.PermissionHelper#requestReadPhoneStatePermission(Fragment, PermissionsResultAction)}获取权限
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

}
