/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */
package com.zcolin.frame.util;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

import com.zcolin.frame.app.BaseApp;


/**
 * SpannableString字符串工具类
 */
public class SpannableStringUtil {

    /**
     * 给某段字符设置下划线
     *
     * @param str       整段字符串
     * @param effectStr 需要设置下划线的字符
     */
    public static SpannableString underLine(String str, String effectStr) {
        SpannableStringUtil.Builder builder = new SpannableStringUtil.Builder(str);
        int index = 0;
        while (index < str.length()) {
            int start = str.indexOf(effectStr, index);
            if (start < 0) {
                break;
            }
            int end = start + effectStr.length();
            builder.underLine(start, end);
            index = end;
        }
        return builder.build();
    }

    /**
     * 给某段字符设置下划线
     */
    public static SpannableString underLine(SpannableString sp, int start, int end) {
        return new SpannableStringUtil.Builder(sp).underLine(start, end).build();
    }

    /**
     * 给某段字符设置下划线
     *
     * @param str       整段字符串
     * @param effectStr 需要设置中划线的字符
     */
    public static SpannableString strikethroughLine(String str, String effectStr) {
        SpannableStringUtil.Builder builder = new SpannableStringUtil.Builder(str);
        int index = 0;
        while (index < str.length()) {
            int start = str.indexOf(effectStr, index);
            if (start < 0) {
                break;
            }
            int end = start + effectStr.length();
            builder.strikethroughLine(start, end);
            index = end;
        }
        return builder.build();
    }

    /**
     * 给某段字符设置下划线
     */
    public static SpannableString strikethroughLine(SpannableString sp, int start, int end) {
        return new SpannableStringUtil.Builder(sp).strikethroughLine(start, end).build();
    }

    /**
     * 给某段字符设置点击事件
     *
     * @param str           整段字符串
     * @param effectStr     需要设置点击事件的字符
     * @param clickListener 点击事件回调
     */
    public static SpannableString clickable(String str, String effectStr, final View.OnClickListener clickListener) {
        SpannableStringUtil.Builder builder = new SpannableStringUtil.Builder(str);
        int index = 0;
        while (index < str.length()) {
            int start = str.indexOf(effectStr, index);
            if (start < 0) {
                break;
            }
            int end = start + effectStr.length();
            builder.clickable(start, end, clickListener);
            index = end;
        }
        return builder.build();
    }


    /**
     * 给某段字符设置点击事件
     */
    public static SpannableString clickable(SpannableString sp, int start, int end, final View.OnClickListener clickListener) {
        return new SpannableStringUtil.Builder(sp).clickable(start, end, clickListener).build();
    }

    /**
     * 创建一个含有超链接的字符串
     *
     * @param str       整段字符串
     * @param effectStr 含有超链接的字符
     * @param url       超链接地址 文本对应的链接地址，需要注意格式：
     *                  （1）电话以"tel:"打头，比如"tel:02355692427"
     *                  （2）邮件以"mailto:"打头，比如"mailto:zmywly8866@gmail.com"
     *                  （3）短信以"sms:"打头，比如"sms:02355692427"
     *                  （4）彩信以"mms:"打头，比如"mms:02355692427"
     *                  （5）地图以"geo:"打头，比如"geo:68.426537,68.123456"
     *                  （6）网络以"http://"打头，比如"http://www.google.com"
     */
    public static SpannableString link(String str, String effectStr, String url) {
        SpannableStringUtil.Builder builder = new SpannableStringUtil.Builder(str);
        int index = 0;
        while (index < str.length()) {
            int start = str.indexOf(effectStr, index);
            if (start < 0) {
                break;
            }
            int end = start + effectStr.length();
            builder.link(start, end, url);
            index = end;
        }
        return builder.build();
    }

    /**
     * 创建一个含有超链接的字符串
     *
     * @param url 超链接地址 文本对应的链接地址，需要注意格式：
     *            （1）电话以"tel:"打头，比如"tel:02355692427"
     *            （2）邮件以"mailto:"打头，比如"mailto:zmywly8866@gmail.com"
     *            （3）短信以"sms:"打头，比如"sms:02355692427"
     *            （4）彩信以"mms:"打头，比如"mms:02355692427"
     *            （5）地图以"geo:"打头，比如"geo:68.426537,68.123456"
     *            （6）网络以"http://"打头，比如"http://www.google.com"
     */
    public static SpannableString link(SpannableString sp, int start, int end, String url) {
        return new SpannableStringUtil.Builder(sp).link(start, end, url).build();
    }

    /**
     * 高亮关键字
     *
     * @param str            整段字符串
     * @param effectStr      关键字
     * @param highlightColor 高亮颜色
     */
    public static SpannableString highLight(String str, String effectStr, int highlightColor) {
        SpannableStringUtil.Builder builder = new SpannableStringUtil.Builder(str);
        int index = 0;
        while (index < str.length()) {
            int start = str.indexOf(effectStr, index);
            if (start < 0) {
                break;
            }
            int end = start + effectStr.length();
            builder.highLight(start, end, highlightColor);
            index = end;
        }
        return builder.build();
    }

    /**
     * 高亮整段字符串
     *
     * @param str            整段字符串
     * @param highlightColor 高亮颜色
     */
    public static SpannableString highLight(String str, int highlightColor) {
        return new SpannableStringUtil.Builder(str).highLight(0, str.length(), highlightColor).build();
    }

    /**
     * 高亮整段字符串
     */
    public static SpannableString highLight(SpannableString sp, int start, int end, int highlightColor) {
        return new SpannableStringUtil.Builder(sp).highLight(start, end, highlightColor).build();
    }


    /**
     * 给一段文字设置背景色
     *
     * @param str              整段字符串
     * @param effectStr        要设置背景色的代码段
     * @param basckgroundColor 背景颜色
     */
    public static SpannableString backgroundColor(String str, String effectStr, int basckgroundColor) {
        SpannableStringUtil.Builder builder = new SpannableStringUtil.Builder(str);
        int index = 0;
        while (index < str.length()) {
            int start = str.indexOf(effectStr, index);
            if (start < 0) {
                break;
            }
            int end = start + effectStr.length();
            builder.backgroundColor(start, end, basckgroundColor);
            index = end;
        }
        return builder.build();
    }

    /**
     * 给一段文字设置背景色
     */
    public static SpannableString backgroundColor(SpannableString sp, int start, int end, int basckgroundColor) {
        return new SpannableStringUtil.Builder(sp).backgroundColor(start, end, basckgroundColor).build();
    }


    /**
     * 给一段文字设置字号
     *
     * @param str       整段字符串
     * @param effectStr 要设置字体大小的代码段
     * @param textSize  字体大小  sp
     */
    public static SpannableString textSize(String str, String effectStr, int textSize) {
        textSize = DisplayUtil.sp2px(BaseApp.APP_CONTEXT, textSize);
        SpannableStringUtil.Builder builder = new SpannableStringUtil.Builder(str);
        int index = 0;
        while (index < str.length()) {
            int start = str.indexOf(effectStr, index);
            if (start < 0) {
                break;
            }
            int end = start + effectStr.length();
            builder.textSize(start, end, textSize);
            index = end;
        }
        return builder.build();
    }


    /**
     * 给一段文字设置字号
     */
    public static SpannableString textSize(SpannableString sp, int start, int end, int textSize) {
        return new SpannableStringUtil.Builder(sp).textSize(start, end, textSize).build();
    }

    /**
     * 给一段文字设置下标
     *
     * @param str       整段字符串
     * @param effectStr 要设置下标字段
     */
    public static SpannableString subScript(String str, String effectStr) {
        SpannableStringUtil.Builder builder = new SpannableStringUtil.Builder(str);
        int index = 0;
        while (index < str.length()) {
            int start = str.indexOf(effectStr, index);
            if (start < 0) {
                break;
            }
            int end = start + effectStr.length();
            builder.subScript(start, end);
            index = end;
        }
        return builder.build();
    }

    /**
     * 给一段文字设置下标
     */
    public static SpannableString subScript(SpannableString sp, int start, int end) {
        return new SpannableStringUtil.Builder(sp).subScript(start, end).build();
    }


    /**
     * 给一段文字设置上标
     *
     * @param str            整段字符串
     * @param superScriptStr 要设置上标字段
     */
    public static SpannableString superScript(String str, String superScriptStr) {
        int start = str.indexOf(superScriptStr);
        return new SpannableStringUtil.Builder(str).superScript(start, start + superScriptStr.length()).build();
    }

    /**
     * 给一段文字设置上标
     */
    public static SpannableString superScript(SpannableString sp, int start, int end) {
        return new SpannableStringUtil.Builder(sp).superScript(start, end).build();
    }

    /**
     * 给一段文字设置加粗
     *
     * @param str       整段字符串
     * @param effectStr 要设置加粗字段
     */
    public static SpannableString bold(String str, String effectStr) {
        SpannableStringUtil.Builder builder = new SpannableStringUtil.Builder(str);
        int index = 0;
        while (index < str.length()) {
            int start = str.indexOf(effectStr, index);
            if (start < 0) {
                break;
            }
            int end = start + effectStr.length();
            builder.bold(start, end);
            index = end;
        }
        return builder.build();
    }

    /**
     * 给一段文字设置加粗
     */
    public static SpannableString bold(SpannableString sp, int start, int end) {
        return new SpannableStringUtil.Builder(sp).bold(start, end).build();
    }

    /**
     * 给一段文字设置斜体
     *
     * @param str       整段字符串
     * @param effectStr 要设置斜体字段
     */
    public static SpannableString italic(String str, String effectStr) {
        SpannableStringUtil.Builder builder = new SpannableStringUtil.Builder(str);
        int index = 0;
        while (index < str.length()) {
            int start = str.indexOf(effectStr, index);
            if (start < 0) {
                break;
            }
            int end = start + effectStr.length();
            builder.italic(start, end);
            index = end;
        }
        return builder.build();
    }

    /**
     * 给一段文字设置斜体
     */
    public static SpannableString italic(SpannableString sp, int start, int end) {
        return new SpannableStringUtil.Builder(sp).italic(start, end).build();
    }

    /**
     * 给一段文字设置斜体加粗
     *
     * @param str       整段字符串
     * @param effectStr 要设置斜体加粗字段
     */
    public static SpannableString italicBold(String str, String effectStr) {
        SpannableStringUtil.Builder builder = new SpannableStringUtil.Builder(str);
        int index = 0;
        while (index < str.length()) {
            int start = str.indexOf(effectStr, index);
            if (start < 0) {
                break;
            }
            int end = start + effectStr.length();
            builder.italicBold(start, end);
            index = end;
        }
        return builder.build();
    }

    /**
     * 给一段文字设置斜体加粗
     */
    public static SpannableString italicBold(SpannableString sp, int start, int end) {
        return new SpannableStringUtil.Builder(sp).italicBold(start, end).build();
    }

    /**
     * 给一段文字设置Style
     *
     * @param str            整段字符串
     * @param effectStr      要设置样式的字符串
     * @param tetxtApperence 要设置样式
     */
    public static SpannableString style(String str, String effectStr, int tetxtApperence) {
        SpannableStringUtil.Builder builder = new SpannableStringUtil.Builder(str);
        int index = 0;
        while (index < str.length()) {
            int start = str.indexOf(effectStr, index);
            if (start < 0) {
                break;
            }
            int end = start + effectStr.length();
            builder.style(start, end, tetxtApperence);
            index = end;
        }
        return builder.build();
    }

    /**
     * 给一段文字设置Style
     */
    public static SpannableString style(SpannableString sp, int start, int end, int textApperence) {
        return new SpannableStringUtil.Builder(sp).style(start, end, textApperence).build();
    }

    /**
     * 插入图片
     *
     * @param str       整段字符串
     * @param effectStr 要设置斜体加粗字段
     * @param drawable  图片
     */
    public static SpannableString image(String str, String effectStr, Drawable drawable) {
        SpannableStringUtil.Builder builder = new SpannableStringUtil.Builder(str);
        int index = 0;
        while (index < str.length()) {
            int start = str.indexOf(effectStr, index);
            if (start < 0) {
                break;
            }
            int end = start + effectStr.length();
            builder.image(start, end, drawable);
            index = end;
        }
        return builder.build();
    }

    /**
     * 插入图片
     */
    public static SpannableString image(SpannableString sp, int start, int end, Drawable drawable) {
        return new SpannableStringUtil.Builder(sp).image(start, end, drawable).build();
    }


    /**
     * 构造者，上边静态函数满足不了需求或者一段文字有多种状态需要设置，可以直接使用构造者
     */
    public static class Builder {
        SpannableString spannableString;

        public Builder(String str) {
            spannableString = new SpannableString(str);
        }

        public Builder(SpannableString sp) {
            spannableString = sp;
        }

        /**
         * 给某段字符设置下划线
         */
        public Builder underLine(int start, int end) {
            spannableString.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return this;
        }

        /**
         * 给某段字符设置下划线
         */
        public Builder strikethroughLine(int start, int end) {
            spannableString.setSpan(new StrikethroughSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return this;
        }

        /**
         * 给某段字符设置点击事件
         */
        public Builder clickable(int start, int end, final View.OnClickListener clickListener) {
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    clickListener.onClick(widget);
                }
            }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return this;
        }

        /**
         * 创建一个含有超链接的字符串
         *
         * @param url 超链接地址 文本对应的链接地址，需要注意格式：
         *            （1）电话以"tel:"打头，比如"tel:02355692427"
         *            （2）邮件以"mailto:"打头，比如"mailto:zmywly8866@gmail.com"
         *            （3）短信以"sms:"打头，比如"sms:02355692427"
         *            （4）彩信以"mms:"打头，比如"mms:02355692427"
         *            （5）地图以"geo:"打头，比如"geo:68.426537,68.123456"
         *            （6）网络以"http://"打头，比如"http://www.google.com"
         */
        public Builder link(int start, int end, String url) {
            spannableString.setSpan(new URLSpan(url), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return this;
        }

        /**
         * 高亮整段字符串
         */
        public Builder highLight(int start, int end, int highlightColor) {
            spannableString.setSpan(new ForegroundColorSpan(highlightColor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return this;
        }


        /**
         * 给一段文字设置背景色
         */
        public Builder backgroundColor(int start, int end, int basckgroundColor) {
            spannableString.setSpan(new BackgroundColorSpan(basckgroundColor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return this;
        }


        /**
         * 给一段文字设置字号
         */
        public Builder textSize(int start, int end, int textSize) {
            spannableString.setSpan(new AbsoluteSizeSpan(textSize), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return this;
        }


        /**
         * 给一段文字设置下标
         */
        public Builder subScript(int start, int end) {
            spannableString.setSpan(new SubscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return this;
        }


        /**
         * 给一段文字设置上标
         */
        public Builder superScript(int start, int end) {
            spannableString.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return this;
        }

        /**
         * 给一段文字设置加粗
         */
        public Builder bold(int start, int end) {
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return this;
        }


        /**
         * 给一段文字设置斜体
         */
        public Builder italic(int start, int end) {
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return this;
        }

        /**
         * 给一段文字设置斜体加粗
         */
        public Builder italicBold(int start, int end) {
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return this;
        }


        /**
         * 给一段文字设置Style
         */
        public Builder style(int start, int end, int tetxtApperence) {
            spannableString.setSpan(new TextAppearanceSpan(BaseApp.APP_CONTEXT, tetxtApperence), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return this;
        }

        /**
         * 插入图片
         */
        public Builder image(int start, int end, Drawable drawable) {
            spannableString.setSpan(new ImageSpan(drawable), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return this;
        }

        public SpannableString build() {
            return spannableString;
        }
    }
}
