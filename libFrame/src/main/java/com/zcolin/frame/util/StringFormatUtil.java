/*
 * *********************************************************
 *  author   colin
 *  company  fosung
 *  email    wanglin2046@126.com
 *  date     16-9-29 下午5:26
 * ********************************************************
 */

/*    
 * TODO
 *
 * @author		: WangLin  
 * @Company: 	：FCBN
 * @date		: 2015年8月27日 下午8:17:12  
 * @version 	: V1.0
 */
package com.zcolin.frame.util;


import java.util.Locale;

/**
 * 格式化工具类
 */
public class StringFormatUtil {

    /**
     * 获取float格式化的字符串
     *
     * @param fData     需要格式的数据
     * @param precision 保留的位数
     */
    public static String formatPrecision(double fData, int precision) {
        return String.format(Locale.CHINA, "%." + precision + "f", fData);
    }

    /**
     * 格式化成只有一位的格式
     */
    public static String formatPrecision(double data) {
        return formatPrecision(data, 1);
    }

    /**
     * 格式化数据，将参数为null的使用“”代替
     *
     * @param args 需要格式的数据
     */
    public static String format(String str, Object... args) {
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                args[i] = args[i] == null ? "" : args[i];
            }
            return String.format(Locale.CHINA, str, args);
        } else {
            return String.format(Locale.CHINA, str, "");
        }
    }

    /**
     * 将Int格式化为字符串
     *
     * @param fData 需要格式的数据
     */
    public static String format(int fData) {
        return String.format(Locale.CHINA, "%d", fData);
    }


    /**
     * 格式化成金额形式
     */
    public static String formatMoney(double data) {
        return String.format(Locale.CHINA, "￥%." + 1 + "f", data);
    }

    /**
     * 最小宽度前部补0
     *
     * @param precision 最小宽度
     */
    public static String formatFillZero(int data, int precision) {
        return String.format(Locale.CHINA, "%0" + precision + "d", data);
    }
}
