/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */

package com.zcolin.frame.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Calendar操作工具类
 */
public class CalendarUtil {

    /**
     * 缺省的日期显示格式
     */
    public static final String DEF_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 日期时间显示格式
     */
    public static final String DEF_DATETIME_FORMAT_SEC = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期时间显示格式,带微秒
     */
    public static final String DEF_DATETIME_FORMAT_MSEC = "yyyy-MM-dd HH:mm:ss.sss";

    /**
     * 只有时间的输出格式
     */
    public static final String DEF_ONLYTIME_FORMAT = "HHmmss";

    /**
     * 缺省的日期时间显示格式
     */
    public static final String DEF_DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

    /**
     * 简写日期时间显示格式
     */
    public static final String DEF_DATETIME_SIMPLD = "yy-MM-dd HH:mm";

    /**
     * UTC格式
     */
    public static final String DEF_UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * Iso格式
     */
    public static final String DEF_ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.sssZ";

    /**
     * 默认DATETIME格式
     */
    private static String DATETIME_FORMAT = DEF_DATETIME_FORMAT_SEC;

    /**
     * 默认DATE格式
     */
    private static String DATE_FORMAT = DEF_DATE_FORMAT;

    /**
     * 默认ONLYTIME格式
     */
    private static String ONLYTIME_FORMAT = DEF_ONLYTIME_FORMAT;

    /**
     * 私有构造方法，禁止对该类进行实例化
     */
    private CalendarUtil() {
    }

    /**
     * 初始化DateTime输出样式
     */
    public static void initDateTimeFormat(String pattern) {
        DATETIME_FORMAT = pattern;
    }

    /**
     * 初始化Date输出样式
     */
    public static void initDateFormat(String pattern) {
        DATE_FORMAT = pattern;
    }

    /**
     * 初始化OnlyTime输出样式
     */
    public static void initOnlyTimeFormat(String pattern) {
        ONLYTIME_FORMAT = pattern;
    }

    /**
     * 得到系统当前日期时间
     *
     * @return 当前日期时间
     */
    public static Date getNow() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 得到系统当前日期时间
     *
     * @return 当前日期时间
     */
    public static Calendar getCalendar(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return calendar;
    }

    /**
     * 得到用缺省方式格式化的当前日期
     *
     * @return 当前日期
     */
    public static String getDate() {
        return getDateTime(DATE_FORMAT);
    }

    /**
     * 得到用缺省方式格式化的当前日期及时间
     *
     * @return 当前日期及时间
     */
    public static String getDateTime() {
        return getDateTime(DATETIME_FORMAT);
    }

    /**
     * 得到用缺省方式格式化的当前时间
     *
     * @return 当前时间
     */
    public static String getOnlyTime() {
        return getDateTime(ONLYTIME_FORMAT);
    }

    /**
     * 得到系统当前日期及时间，并用指定的方式格式化
     *
     * @param pattern 显示格式
     * @return 当前日期及时间
     */
    public static String getDateTime(String pattern) {
        Date datetime = Calendar.getInstance().getTime();
        return getDateTime(datetime, pattern);
    }

    /**
     * 得到用缺省方式格式化的当前日期
     *
     * @return 传入date格式化后的字符串
     */
    public static String getDate(Date date) {
        return getDateTime(date, DATE_FORMAT);
    }

    /**
     * 得到用缺省方式格式化的当前日期及时间
     *
     * @return 传入date格式化后的字符串
     */
    public static String getDateTime(Date date) {
        return getDateTime(date, DATETIME_FORMAT);
    }

    /**
     * 得到用缺省方式格式化的当前时间
     *
     * @return 传入date格式化后的字符串
     */
    public static String getOnlyTime(Date date) {
        return getDateTime(date, ONLYTIME_FORMAT);
    }

    /**
     * 得到用指定方式格式化的日期
     *
     * @param date    Date对象
     * @param pattern 显示格式
     * @return 日期时间字符串
     */
    public static String getDateTime(Date date, String pattern) {
        if (date == null) {
            return "";
        }

        if (null == pattern || "".equals(pattern)) {
            pattern = DATETIME_FORMAT;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        return dateFormat.format(date);
    }

    /**
     * 得到用指定方式格式化的日期  UTC时间
     *
     * @param date    Date对象
     * @param pattern 显示格式
     * @return 日期时间字符串
     */
    public static String getDateTimeUTC(Date date, String pattern) {
        if (date == null) {
            return "";
        }

        if (null == pattern || "".equals(pattern)) {
            pattern = DATETIME_FORMAT;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(date);
    }

    /**
     * 得到用缺省方式格式化的当前日期
     *
     * @return 传入date格式化后的字符串
     */
    public static String getDate(long timeStamp) {
        return getDate(getCalendar(timeStamp).getTime());
    }

    /**
     * 得到用缺省方式格式化的当前日期及时间
     *
     * @return 传入date格式化后的字符串
     */
    public static String getDateTime(long timeStamp) {
        return getDateTime(getCalendar(timeStamp).getTime());
    }

    /**
     * 得到用缺省方式格式化的当前时间
     *
     * @return 传入date格式化后的字符串
     */
    public static String getOnlyTime(long timeStamp) {
        return getOnlyTime(getCalendar(timeStamp).getTime());
    }

    /**
     * 得到用指定方式格式化的日期
     *
     * @param timeStamp 时间戳
     * @param pattern   显示格式
     * @return 日期时间字符串
     */
    public static String getDateTime(long timeStamp, String pattern) {
        return getDateTime(getCalendar(timeStamp).getTime(), pattern);
    }


    /**
     * 得到当前年份
     *
     * @return 当前年份
     */
    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 得到当前月份
     *
     * @return 当前月份
     */
    public static int getCurrentMonth() {
        //用get得到的月份数比实际的小1，需要加上  
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 得到当前日
     *
     * @return 当前日
     */
    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    /**
     * 获取当前周天数
     *
     * @return 转换后的dayOfWeek
     */
    public static int getCurrentDayOfWeek() {
        final Calendar mCalendar = Calendar.getInstance();
        int dayofweek = mCalendar.get(Calendar.DAY_OF_WEEK);
        return getDayOfWeek(dayofweek);
    }

    /**
     * 获取当前中文周天数
     *
     * @return 转换后的dayOfWeek
     */
    public static String getCurrentChineseCharDayOfWeek() {
        final Calendar mCalendar = Calendar.getInstance();
        int dayofweek = mCalendar.get(Calendar.DAY_OF_WEEK);
        return getChineseCharDayOfWeek(dayofweek);
    }


    /**
     * 获取周天数    Calendar中1-星期天，2-星期一，3-星期二，4-星期三，5-星期四，6-星期五，7-星期六
     *
     * @param dayofweek 使用Calendar获取的dayOfWeek
     * @return 转换后的dayOfWeek
     */
    public static int getDayOfWeek(int dayofweek) {
        int day = 1;
        if (dayofweek > 0 && dayofweek < 8) {
            int[] wee = {0, 7, 1, 2, 3, 4, 5, 6};
            day = wee[dayofweek];
        }
        return day;
    }

    /**
     * 获取中文周天数    星期天
     *
     * @param dayofweek 使用Calendar获取的dayOfWeek
     * @return 转换后的dayOfWeek
     */
    public static String getChineseCharDayOfWeek(int dayofweek) {
        String day = "";
        if (dayofweek > 0 && dayofweek < 8) {
            String[] wee = {"", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
            day = wee[dayofweek];
        }
        return day;
    }

    /**
     * 取得当前日期以后若干天的日期。如果要得到以前的日期，参数用负数。
     * 例如要得到上星期同一天的日期，参数则为-7
     *
     * @param days 增加的日期数
     * @return 增加以后的日期
     */
    public static Date addDays(int days) {
        return add(getNow(), days, Calendar.DATE);
    }

    /**
     * 取得指定日期以后若干天的日期。如果要得到以前的日期，参数用负数。
     *
     * @param date 基准日期
     * @param days 增加的日期数
     * @return 增加以后的日期
     */
    public static Date addDays(Date date, int days) {
        return add(date, days, Calendar.DATE);
    }

    /**
     * 取得当前日期以后某月的日期。如果要得到以前月份的日期，参数用负数。
     *
     * @param months 增加的月份数
     * @return 增加以后的日期
     */
    public static Date addMonths(int months) {
        return add(getNow(), months, Calendar.MONTH);
    }

    /**
     * 取得指定日期以后某月的日期。如果要得到以前月份的日期，参数用负数。
     * 注意，可能不是同一日子，例如2003-1-31加上一个月是2003-2-28 、
     *
     * @param date   基准日期
     * @param months 增加的月份数
     * @return 增加以后的日期
     */
    public static Date addMonths(Date date, int months) {
        return add(date, months, Calendar.MONTH);
    }

    /**
     * 为指定日期增加相应的天数或月数
     *
     * @param date   基准日期
     * @param amount 增加的数量
     * @param field  增加的单位，年，月或者日
     * @return 增加以后的日期
     */
    public static Date add(Date date, int amount, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 获取日期为当前时间，将日期设定为制定的时分秒,不改变原calendar
     */
    public static Calendar getCalendarWithTime(int hour, int minute, int second) {
        return getCalendarWithTime(0, hour, minute, second);
    }

    /**
     * 将日期设定为制定的时分秒,不改变原calendar
     */
    public static Calendar getCalendarWithTime(Calendar calendar, int hour, int minute, int second) {
        return getCalendarWithTime(calendar.getTimeInMillis(), hour, minute, second);
    }

    /**
     * 将日期设定为制定的时分秒,不改变原calendar
     */
    public static Calendar getCalendarWithTime(Date date, int hour, int minute, int second) {
        return getCalendarWithTime(date.getTime(), hour, minute, second);
    }

    /**
     * 将日期设定为制定的时分秒,不改变原calendar
     */
    public static Calendar getCalendarWithTime(long timeMillion, int hour, int minute, int second) {
        Calendar calendarNew = Calendar.getInstance();
        if (timeMillion > 0) {
            calendarNew.setTimeInMillis(timeMillion);
        }
        calendarNew.set(Calendar.HOUR_OF_DAY, hour);
        calendarNew.set(Calendar.MINUTE, minute);
        calendarNew.set(Calendar.SECOND, second);
        return calendarNew;
    }

    /**
     * 计算日期和现在相差时间
     *
     * @param date 需要计算的Date对象
     * @return 相差的时间
     */
    public static String timeAgo(Date date) {
        return timeAgo(CalendarUtil.getNow(), date);
    }

    /**
     * 计算两个日期相差时间 。
     *
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 相差时间
     */
    public static String timeAgo(Date one, Date two) {
        long day = Math.abs(((int) (one.getTime() / 1000) - (int) (two.getTime() / 1000)) / (24 * 60 * 60));

        long diffTime = day / 365;
        if (Math.abs(diffTime) > 0) {
            return diffTime + "年前";
        }

        diffTime = day / 30;
        if (Math.abs(diffTime) > 0) {
            return diffTime + "月前";
        }

        diffTime = day;
        if (Math.abs(diffTime) > 0) {
            return diffTime + "天前";
        }

        diffTime = CalendarUtil.diffHours(one, two);
        if (Math.abs(diffTime) > 0) {
            return diffTime + "小时前";
        }

        diffTime = CalendarUtil.diffMinutes(one, two);
        if (Math.abs(diffTime) > 0) {
            return diffTime + "分钟前";
        }

        return "刚刚";
    }

    /**
     * 计算两个日期相差分钟数。
     * 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
     *
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差分钟数
     */
    public static long diffMinutes(Date one, Date two) {
        return (one.getTime() - two.getTime()) / (60 * 1000);
    }

    /**
     * 计算两个日期相差小时数。
     * 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
     *
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差小时数
     */
    public static long diffHours(Date one, Date two) {
        return (one.getTime() - two.getTime()) / (60 * 60 * 1000);
    }

    /**
     * 计算两个日期相差天数。
     * 如 2015.6.3 12:00 和 2015.6.4 11:00 结果为1
     * 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
     *
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差天数
     */
    public static long diffDays(Date one, Date two) {
        Calendar ca1 = getCalendarWithTime(one, 0, 0, 0);
        Calendar ca2 = getCalendarWithTime(two, 0, 0, 0);
        return ((int) (ca1.getTimeInMillis() / 1000) - (int) (ca2.getTimeInMillis() / 1000)) / (24 * 60 * 60);
    }

    /**
     * 计算两个日期相差天数。
     * 如 2015.6.3 12:00 和 2015.6.4 11:00 结果为1
     * 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
     *
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差天数
     */
    public static long diffDays(Calendar one, Calendar two) {
        Calendar ca1 = getCalendarWithTime(one, 0, 0, 0);
        Calendar ca2 = getCalendarWithTime(two, 0, 0, 0);
        return ((int) (ca1.getTimeInMillis() / 1000) - (int) (ca2.getTimeInMillis() / 1000)) / (24 * 60 * 60);
    }

    /**
     * 计算两个日期相差月份数。
     * 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
     */
    public static int diffMonth(Calendar one, Calendar two) {
        int diffYear = one.get(Calendar.YEAR) - two.get(Calendar.YEAR);
        int diffMonth = one.get(Calendar.MONTH) - two.get(Calendar.MONTH);
        return diffYear * 12 + diffMonth;
    }


    /**
     * 计算两个日期相差月份数。
     *
     * @return 两个日期相差月数
     */
    public static int diffMonth(Date dateOne, Date dateTwo) {
        Calendar one = Calendar.getInstance();
        Calendar two = Calendar.getInstance();
        one.setTime(dateOne);
        two.setTime(dateTwo);
        return diffMonth(one, two);
    }

    /**
     * 计算两个日期相差月年数。
     * 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
     */
    public static int diffYear(Calendar one, Calendar two) {
        return one.get(Calendar.YEAR) - two.get(Calendar.YEAR);
    }

    /**
     * 计算两个日期相差月年数。
     * 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
     */
    public static int diffYear(Date dateOne, Date dateTwo) {
        Calendar one = Calendar.getInstance();
        Calendar two = Calendar.getInstance();
        one.setTime(dateOne);
        two.setTime(dateTwo);
        return diffYear(dateOne, dateTwo);
    }

    /**
     * 计算两个日期是否为同一天
     * 如 2015.6.3 12:00 和 2015.6.4 11:00 结果为true
     */
    public static boolean isSameDay(Calendar one, Calendar two) {
        int diffYear = one.get(Calendar.YEAR) - two.get(Calendar.YEAR);
        int diffMonth = one.get(Calendar.MONTH) - two.get(Calendar.MONTH);
        int diffDay = one.get(Calendar.DATE) - two.get(Calendar.DATE);
        return diffYear == 0 && diffMonth == 0 && diffDay == 0;
    }

    /**
     * 计算两个日期是否为同一天
     * 如 2015.6.3 12:00 和 2015.6.4 11:00 结果为true
     */
    public static boolean isSameDay(Date dateOne, Date dateTwo) {
        Calendar one = Calendar.getInstance();
        Calendar two = Calendar.getInstance();
        one.setTime(dateOne);
        two.setTime(dateTwo);
        return isSameDay(one, two);
    }

    /**
     * 将一个字符串用给定的格式转换为日期类型。
     *
     * @param datestr 需要解析的日期字符串
     * @param pattern 日期字符串的格式，默认为“yyyy-MM-dd”的形式
     * @return 解析后的日期 ， 如果返回null，则表示解析失败
     */
    public static Date parse(String datestr, String pattern) {
        Date date = null;
        if (null == pattern || "".equals(pattern)) {
            pattern = DATE_FORMAT;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
            date = dateFormat.parse(datestr);
        } catch (ParseException e) {
        }
        return date;
    }

    /**
     * 将一个UTC时间字符串用给定的格式转换为日期类型。
     *
     * @param datestr 需要解析的日期字符串
     * @param pattern 日期字符串的格式，默认为“yyyy-MM-dd”的形式
     * @return 解析后的日期 ， 如果返回null，则表示解析失败
     */
    public static Date parseUTC(String datestr, String pattern) {
        Date date = null;
        if (null == pattern || "".equals(pattern)) {
            pattern = DATE_FORMAT;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            date = dateFormat.parse(datestr);
        } catch (ParseException e) {
        }
        return date;
    }

    /**
     * 返回本月的最后一天
     *
     * @return 本月最后一天的日期
     */
    public static Date getMonthLastDay() {
        return getMonthLastDay(getNow());
    }

    /**
     * 返回给定日期中的月份中的最后一天
     *
     * @return 该月最后一天的日期
     */
    public static Date getMonthLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getMonthLastDay(calendar).getTime();
    }

    /**
     * 返回给定日期中的月份中的最后一天
     *
     * @return 该月最后一天的日期
     */
    public static Calendar getMonthLastDay(Calendar calendar) {
        //将日期设置为下一月第一天  
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
        //减去1天，得到的即本月的最后一天  
        calendar.add(Calendar.DATE, -1);
        return calendar;
    }
}
