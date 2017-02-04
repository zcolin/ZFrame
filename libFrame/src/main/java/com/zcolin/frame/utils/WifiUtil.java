/*    
 * 
 * @author		: WangLin  
 * @Company: 	：FCBN
 * @date		: 2015年5月14日 
 * @version 	: V1.0
 */
package com.zcolin.frame.utils;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.SystemClock;

import java.util.List;

/**
 * Wifi管理工具类
 */
public class WifiUtil {

    private static WifiLock wifiLock;

    /**
     * 获取WifiManager实例
     */
    public static WifiManager getWifiMgr(Context context) {
        return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 检查Wifi是否开启
     */
    public static boolean isWifiEnable(Context context) {
        boolean flag = false;
        WifiManager wifiMgr = getWifiMgr(context);
        if (wifiMgr != null && wifiMgr.isWifiEnabled()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 关闭WIFI
     */
    public static boolean closeWifi(Context context) {
        boolean bRet = true;
        WifiManager wifiManager = getWifiMgr(context);
        if (wifiManager.isWifiEnabled()) {
            bRet = wifiManager.setWifiEnabled(false);
        }
        return bRet;
    }

    /**
     * 关闭wifi,并且等待，直到wifi关闭成功
     */
    public static void closeWifiWithWait(Context context) {
        closeWifi(context);
        SystemClock.sleep(1000);

        // 关闭wifi功能需要一段时间(一般需要1-3秒左右)，所以要等到WIFI状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
        WifiManager wifiManager = getWifiMgr(context);
        while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
            SystemClock.sleep(500);
        }
    }

    /**
     * 打开WIFI
     */
    public static boolean openWifi(Context context) {
        boolean bRet = true;
        WifiManager wifiManager = getWifiMgr(context);
        if (!wifiManager.isWifiEnabled()) {
            bRet = wifiManager.setWifiEnabled(true);
        }
        return bRet;
    }

    /**
     * 打开wifi,并且等待，直到wifi打开成功
     */
    public static void openWifiWithWait(Context context) {
        openWifi(context);
        SystemClock.sleep(1000);

        WifiManager wifiManager = getWifiMgr(context);
        while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            SystemClock.sleep(500);
        }
    }

    /**
     * 刷新WIFI连接，如果没有连接或者连接的不是指定的SSID，则连接指定的SSID
     *
     * @param SSID     SSID
     * @param Password 密码
     */
    public static void refreshWifi(Context context, String SSID, String Password) {
        WifiManager wifiManager = getWifiMgr(context);
        if (wifiManager != null) {
            if (!wifiManager.isWifiEnabled()) {
                openWifiWithWait(context);
            }
            connect(context, SSID, Password);
        }
    }

    /**
     * 重启WIFI连接
     */
    public static void reStartWifi(Context context) {
        closeWifiWithWait(context);
        openWifiWithWait(context);
    }

    /**
     * 连接的WIFI网络
     *
     * @param SSID     SSID
     * @param Password 密码
     */
    public static boolean connect(Context context, String SSID, String Password) {
        boolean bRet = true;
        if (!isConnectSSID(context, SSID)) {
            WifiConfiguration wifiConfig = new WifiConfiguration();    // 连接信息
            wifiConfig.SSID = "\"" + SSID + "\"";
            wifiConfig.preSharedKey = "\"" + Password + "\"";
            removeConfiguredNetworks(context);
            bRet = connect(context, wifiConfig);
        }
        return bRet;
    }

    /**
     * 连接的WIFI网络
     */
    public static boolean connect(Context context, WifiConfiguration wifiConfig) {
        WifiManager wifiManager = getWifiMgr(context);
        int netID = wifiManager.addNetwork(wifiConfig);
        return wifiManager.enableNetwork(netID, true);
    }

    /**
     * 判断是否连接上指定的网络
     *
     * @param SSID SSID
     */
    public static boolean isConnectSSID(Context context, String SSID) {
        WifiManager wifiManager = getWifiMgr(context);
        boolean isConnect = NetworkUtil.isWifiConnect(context);
        if (isConnect) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null && ("\"" + SSID + "\"").equals(wifiInfo.getSSID()) && getConnectState(context) == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取连接状态
     */
    public static int getConnectState(Context context) {
        WifiManager wifiManager = getWifiMgr(context);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int strRet = 0;
        if (wifiInfo != null) {
            SupplicantState suppState = wifiInfo.getSupplicantState();
            NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(suppState);
            switch (state) {
                //连接失败 
                case IDLE:
                case DISCONNECTED:
                case FAILED:
                case BLOCKED:
                    strRet = 0;
                    break;
                //正在连接 
                case DISCONNECTING:
                case SCANNING:
                case AUTHENTICATING:
                case CONNECTING:
                    strRet = 2;
                    break;
                //连接成功 
                case OBTAINING_IPADDR:
                case CONNECTED:
                    strRet = 1;
                    break;
                default:
                    strRet = 0;
            }
        }
        return strRet;
    }

    /**
     * 根据SSID获取已配置过的信息
     */
    public static WifiConfiguration getWifiConfigurationByssid(Context context, String strSSID) {
        WifiManager wifiManager = getWifiMgr(context);
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        WifiConfiguration wifiConfig = null;
        if (existingConfigs != null) {
            int size = existingConfigs.size();
            for (int i = 0; i < size; i++) {
                WifiConfiguration wifiConfigTemp = existingConfigs.get(i);
                String ssid = StringUtil.strip(wifiConfigTemp.SSID, "\"");
                if (ssid != null && ssid.equals(strSSID)) {
                    wifiConfig = wifiConfigTemp;
                    break;
                }
            }
        }
        return wifiConfig;
    }

    /**
     * 将配置过的信息全部移除
     */
    public static void removeConfiguredNetworks(Context context) {
        WifiManager wifiManager = getWifiMgr(context);
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        if (existingConfigs == null || existingConfigs.size() < 1)
            return;

        for (WifiConfiguration existingConfig : existingConfigs) {
            wifiManager.removeNetwork(existingConfig.networkId);
        }
    }

    /**
     * 移除现在连接的WIFI配置信息
     */
    public static boolean removeCurNetwork(Context context) {
        boolean flag = false;
        WifiInfo wifiInfo = getWifiConnectInfo(context);
        WifiManager wifiManager = getWifiMgr(context);
        if (wifiInfo != null) {
            wifiManager.removeNetwork(wifiInfo.getNetworkId());
            flag = true;
        }
        return flag;
    }

    /**
     * 获取已经配置过的wifi信息
     */
    public static List<WifiConfiguration> getConfiguredNetworks(Context context, List<WifiConfiguration> existingConfigs) {
        WifiManager wifiManager = getWifiMgr(context);
        return wifiManager.getConfiguredNetworks();
    }

    /**
     * 检查wifi信号是否在附近
     */
    public static boolean isExistInWifiList(Context context, String SSID) {
        if (SSID.length() == 0)
            return false;
        WifiManager wifiManager = getWifiMgr(context);
        List<ScanResult> scanResult = wifiManager.getScanResults();
        if (scanResult == null)
            return false;
        for (int i = 0; i < scanResult.size(); i++) {
            if (scanResult.get(i).SSID.equals(SSID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取扫描的Wifi列表
     */
    public static List<ScanResult> getScanWifiList(Context context) {
        WifiManager wifiManager = getWifiMgr(context);
        return wifiManager.getScanResults();
    }

    /**
     * 开始搜寻附近的wifi列表，该方法不会立即返回结果
     */
    public static void scanWifiList(Context context) {
        WifiManager wifiManager = getWifiMgr(context);
        wifiManager.startScan();
    }

    /**
     * 锁住wifi防止休眠
     */
    public static void lockWifi(Context context) {
        try {
            if (wifiLock == null) {
                WifiManager wifiManager = getWifiMgr(context);
                wifiLock = wifiManager.createWifiLock("wifiycb"); // 创建一个锁的标志
                wifiLock.setReferenceCounted(true);
            }
            wifiLock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放wifi锁
     */
    public static void unLockWifi() {
        try {
            if (wifiLock != null && wifiLock.isHeld()) {
                wifiLock.release(); // 释放资源
                wifiLock = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取WIFI启用状态
     */
    public static int getWifiState(Context context) {
        WifiManager wifiManager = getWifiMgr(context);
        int state = wifiManager.getWifiState();
        return state;
    }

    /**
     * 获取WIFI连接到的SSID
     */
    public static String getWifiConnectInfoSSID(Context context) {
        WifiInfo wifiInfo = getWifiConnectInfo(context);
        if (wifiInfo == null)
            return "";
        return wifiInfo.getSSID();
    }

    /**
     * 获取WIFI连接信息
     */
    public static WifiInfo getWifiConnectInfo(Context context) {
        WifiManager wifiManager = getWifiMgr(context);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (!NetworkUtil.isWifiConnect(context) || wifiInfo == null || ("0x").equalsIgnoreCase(wifiInfo.getSSID()) || wifiInfo.getSSID()
                                                                                                                              .equals("<unknown ssid>"))
            return null;
        return wifiInfo;
    }

    /**
     * 计算信号等级，WifiManager中的有bug
     */
    public static int calculateSignalLevel(int rssi, int totalLevels) {
        int MIN_RSSI = -100;
        int MAX_RSSI = -55;
        if (rssi <= MIN_RSSI) {
            return 0;
        } else if (rssi >= MAX_RSSI) {
            return totalLevels - 1;
        } else {
            float inputRange = (MAX_RSSI - MIN_RSSI);
            float outputRange = (totalLevels - 1);
            if (inputRange != 0)
                return (int) ((float) (rssi - MIN_RSSI) * outputRange / inputRange);
        }
        return 0;
    }

    /**
     * 获取Ip地址，只有Wifi连接时获取
     */
    public static String getLocalIP(Context context) {
        String ip = "";
        WifiManager wifiManager = getWifiMgr(context);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            int i = wifiInfo.getIpAddress();
            if (i != 0) {
                ip = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
            }
        }
        return ip;
    }
}
