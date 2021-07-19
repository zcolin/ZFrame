/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */

package com.zcolin.frame.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 网络连接工具类
 */
public class NetworkUtil {

    /**
     * 判断wifi是否连接
     */
    public static boolean isWifiConnect(Context context) {
        return getConnectedType(context) == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 判断移动数据是否连接
     */
    public static boolean isMobileConnect(Context context) {
        return getConnectedType(context) == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        return getConnectedType(context) != -1;
    }

    /**
     * 获取当前网络连接的类型信息, -1：没有网络, 0：移动网络, 1：WIFI网络, 2：wap网络, 3：net网络
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 获取本机Ip地址，可以获取Gprs和Wifi的
     */
    public List<InetAddress> getLocalIpList() {
        ArrayList<InetAddress> list = new ArrayList<>();
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        list.add(inetAddress);
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return list;
    }

    /**
     * 获取本机IpV4地址，可以获取Gprs和Wifi的
     */
    public String getLocalIpV4() {
        List<InetAddress> list = getLocalIpList();
        for (InetAddress inetAddress : list) {
            if (inetAddress instanceof Inet4Address) {
                return inetAddress.getHostAddress();
            }
        }
        return null;
    }
}
