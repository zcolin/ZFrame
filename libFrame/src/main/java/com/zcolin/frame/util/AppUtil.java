/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/
package com.zcolin.frame.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.zcolin.frame.app.BaseApp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * APP管理工具类，如应用退出 重启 安装 卸载.判断程序运行状况等函数的定义
 */
public class AppUtil {

    /**
     * 退出程序
     */
    public static void quitSystem() {
        ActivityUtil.finishAllActivity();
    }

    /**
     * 退出程序
     */
    public static void quitSystemWithService(String... serviceName) {
        ActivityUtil.finishAllActivity();
        if (serviceName != null && serviceName.length > 0) {
            for (String s : serviceName) {
                Intent intent = new Intent(s);
                intent.setPackage(BaseApp.APP_CONTEXT.getPackageName());
                BaseApp.APP_CONTEXT.stopService(intent);
            }
        }
    }

    /**
     * 重启程序
     * <p/>
     * cls 需要重启的页面
     */
    public static void restartSystem(final Activity acty, final Class<?> cls) {
        quitSystem();
        if (cls != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(acty, cls);
                    intent.putExtra("notPlaySplash", true);
                    acty.startActivity(intent);
                }
            }, 500);
        }
    }

    /**
     * 启动另外一个App
     */
    public static void startOtherApp(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent launcherIntent = pm.getLaunchIntentForPackage(packageName);
        context.startActivity(launcherIntent);
    }

    /**
     * 杀进程， 在有的平台会失效
     */
    public static void shutDownPck(Context context, String pckName) {
        if (context != null) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            am.killBackgroundProcesses(pckName);
        }
    }

    /**
     * 获取应用标签
     *
     * @param info ApplicationInfo
     */
    public static String getAppLabel(Context context, ApplicationInfo info) {
        PackageManager pManager = context.getPackageManager();
        return pManager.getApplicationLabel(info)
                       .toString();
    }

    /**
     * 从指定包中 获取自定义标签值标签
     *
     * @param key 字段key
     * @return key对应的值
     */
    public static String getApplicationMetaData(Context context, String key) {
        String myApiKey = null;
        try {
            Bundle bundle = getApplicationMetaData(context);
            if (bundle != null) {
                myApiKey = bundle.getString(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myApiKey;
    }

    /**
     * 从指定的包中 获取自定义标签值标签
     *
     * @return 自定义Bundle对象，没有找到返回null
     */
    public static Bundle getApplicationMetaData(Context context) {
        Bundle bundle = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                                             .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            bundle = appInfo.metaData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bundle;
    }

    /**
     * 读取Activity节点的meta-data
     */
    public static String getActivityMetaData(Activity activity, String key) {
        try {
            return activity.getPackageManager()
                           .getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA)
                    .metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取Service节点的meta-data
     *
     * @param serviceClazz 服务的class
     */
    public static String getServiceMetaData(Context context, Class<? extends Service> serviceClazz, String key) {
        try {
            return context.getPackageManager()
                          .getServiceInfo(new ComponentName(context, serviceClazz), PackageManager.GET_META_DATA)
                    .metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取BroadCast节点meta-data数据
     *
     * @param receiverClazz 广播接收器的class
     */
    public static String getBroadCasetMetaData(Context context, Class<? extends BroadcastReceiver> receiverClazz, String key) {
        try {
            return context.getPackageManager()
                          .getReceiverInfo(new ComponentName(context, receiverClazz), PackageManager.GET_META_DATA)
                    .metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本信息
     */
    public static PackageInfo getPackageInfo(Context context) {
        return getPackageInfo(context, context.getPackageName());
    }

    /**
     * 获取指定包的包信息
     *
     * @param pckName 包名称
     * @return 包信息
     */
    public static PackageInfo getPackageInfo(Context context, String pckName) {
        PackageInfo packInfo = null;
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // 0代表是获取版本信息
            packInfo = packageManager.getPackageInfo(pckName, 0);
        } catch (Exception e) {
            LogUtil.d("FCUtil--getPackageInfo", LogUtil.ExceptionToString(e));
        }
        return packInfo;
    }

    /**
     * 获取本程序版本名称
     *
     * @return 版本名称
     */
    public static String getVersionName(Context context) {
        return getVersionName(context, context.getPackageName());
    }

    /**
     * 获取本程序版本号
     *
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        return getVersionCode(context, context.getPackageName());
    }

    /**
     * 获取指定程序的版本名称
     *
     * @param pckName 指定程序的包名
     * @return 版本名称
     */
    public static String getVersionName(Context context, String pckName) {
        String versionName = null;
        PackageInfo packInfo = getPackageInfo(context, pckName);
        if (packInfo != null)
            versionName = packInfo.versionName;
        return versionName;
    }

    /**
     * 获取指定程序的版本号
     *
     * @param pckName 指定程序的报名
     * @return 版本号
     */
    public static int getVersionCode(Context context, String pckName) {
        int versionCode = 0;
        PackageInfo packInfo = getPackageInfo(context, pckName);
        if (packInfo != null)
            versionCode = packInfo.versionCode;
        return versionCode;
    }

    /**
     * 根据包名获取已安装的PackageInfo
     *
     * @return PackageInfo
     */
    public static List<PackageInfo> getInstallPckInfo(Context context) {
        PackageManager pckMgr = context.getPackageManager();
        return pckMgr.getInstalledPackages(0);
    }

    /**
     * 根据包名获取已安装的PackageInfo
     *
     * @return PackageInfo
     */
    public static PackageInfo getInstallPckInfoByPckName(Context context, String pckName) {
        PackageInfo packageInfo = null;
        PackageManager pckMgr = context.getPackageManager();
        List<PackageInfo> lstPackageInfo = pckMgr.getInstalledPackages(0);
        for (PackageInfo pckInfo : lstPackageInfo) {
            if (pckInfo.packageName.equals(pckName)) {
                packageInfo = pckInfo;
            }
        }
        return packageInfo;
    }

    /**
     * 判断指定程序是否有Service运行
     *
     * @param pckName 程序包名
     * @return 是否有Service运行
     */
    public static boolean isServiceRun(Context context, String pckName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        boolean flag = false;
        List<ActivityManager.RunningServiceInfo> runningServiceInfo = manager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo serviceInfo : runningServiceInfo) {
            String strInfo = serviceInfo.service.getPackageName();
            if (strInfo.startsWith(pckName)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 判断服务是否后台运行
     *
     * @param context Context
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRun(Context context, Class<?> clazz) {
        boolean isRun = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        int size = serviceList.size();
        for (int i = 0; i < size; i++) {
            if (serviceList.get(i).service.getClassName()
                                          .equals(clazz.getName())) {
                isRun = true;
                break;
            }
        }
        return isRun;
    }

    /**
     * 判断指定包中的Activity是否正在运行
     *
     * @param pckName 指定包的包名
     * @return 是否有Activity运行
     */
    public static boolean isActivityRun(Context context, String pckName) {
        boolean isActivityRun = false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> lstInfo = manager.getRunningTasks(100);
        for (RunningTaskInfo info : lstInfo) {
            if (info.topActivity.getPackageName()
                                .startsWith(pckName) || info.baseActivity.getPackageName()
                                                                         .startsWith(pckName)) {
                isActivityRun = true;
                break;
            }
        }
        return isActivityRun;
    }

    /**
     * 判断当前栈顶的Activity是否属于传入的包名
     *
     * @param pckName 指定包的包名
     * @return 栈顶的Activity是否属于传入的包名
     */
    public static boolean isTopActivityFromPck(Context context, String pckName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        RunningTaskInfo info = manager.getRunningTasks(1)
                                      .get(0);
        // String shortClassName = info.topActivity.getShortClassName(); // 类名
        // String className = info.topActivity.getClassName(); // 完整类名
        String activityPckName = info.topActivity.getPackageName(); // 包名
        return activityPckName.startsWith(pckName);
    }

    /**
     * 调用系统安装
     *
     * @param f APK文件对象
     */
    public static void installBySys(Context context, File f) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 调用系统卸载
     *
     * @param pckName 卸载Apk的包名
     */
    public static String unInstallBySys(Context context, String pckName) {
        Intent intent = new Intent();
        Uri uri = Uri.parse("package:" + pckName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(uri);
        context.startActivity(intent);
        return null;
    }

    /**
     * 获取应用程序下所有Activity
     */
    public static ArrayList<ActivityInfo> getAppAllActivities(Context ctx) {
        ArrayList<ActivityInfo> result = new ArrayList<ActivityInfo>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setPackage(ctx.getPackageName());
        for (ResolveInfo info : ctx.getPackageManager()
                                   .queryIntentActivities(intent, 0)) {
            result.add(info.activityInfo);
        }
        return result;
    }

    /**
     * 检查有没有应用程序来接受处理发出的intent
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 应用是否被安装
     */
    public static boolean isInstall(Context context, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager()
                                 .getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            //            e.printStackTrace();
        }
        return packageInfo != null;
    }

    /**
     * 查询手机内非系统应用
     */
    public static List<PackageInfo> getAllNoSystemApps(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                apps.add(pak);
            }
        }
        return apps;
    }

    /**
     * 获取应用名
     */
    public static String getAppName(Context context) {
        try {
            return context.getString(context.getApplicationInfo().labelRes);
        } catch (Resources.NotFoundException e) {
            return "";
        }
    }


    /**
     * 获取设备型号(Nexus5)
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * 获取版本号
     */
    public static String getSystemVersion() {
        // 获取android版本号
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取apk文件的icon
     *
     * @param path apk文件路径
     */
    public static Drawable getApkIcon(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            //android有bug，需要下面这两句话来修复才能获取apk图片
            appInfo.sourceDir = path;
            appInfo.publicSourceDir = path;
            //			    String packageName = appInfo.packageName;  //得到安装包名称
            //	            String version=info.versionName;       //得到版本信息
            return pm.getApplicationIcon(appInfo);
        }
        return null;
    }

    /**
     * 获取包名
     */
    public static String getPackageName(Context context) {
        return getPackageInfo(context).packageName;
    }

    /**
     * 获取目录
     */
    public static String getSourcePath(Context context, String packageName) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager()
                             .getApplicationInfo(packageName, 0);
            return appInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
