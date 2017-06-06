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

    public static String formatFloatPrecision(double fData, int precision) {
        return String.format("%." + precision + "f", fData);
    }

    /**
     * 获取float格式化的字符串
     *
     * @param fData 需要格式的数据
     */

    public static String formatStringInt(int fData) {
        return String.format("%d", fData);
    }

    /**
     * 格式化成只有一位的格式
     */
    public static String formatFloat(double data) {
        return formatFloatPrecision(data, 1);
    }

    /**
     * 格式化成金额形式
     */
    public static String formatToMoney(double data) {
        return String.format("￥%." + 1 + "f", data);
    }

    /**
     * 最小宽度前部补0
     *
     * @param precision 最小宽度
     */
    public static String formatFillZero(int data, int precision) {
        return String.format("%0" + precision + "d", data);
    }
}
