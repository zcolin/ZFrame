/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */
package com.zcolin.frame.util;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
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
 * SpannableStringBuilder字符串工具类
 */
public class SpannableStringBuilderUtil {

    private SpannableStringBuilder spannableString;

    public SpannableStringBuilderUtil() {
        spannableString = new SpannableStringBuilder();
    }

    public SpannableStringBuilderUtil(SpannableStringBuilder spannableStringBuilder) {
        this.spannableString = spannableStringBuilder;
    }

    /**
     * 给某段字符设置下划线
     */
    public SpannableStringBuilderUtil appendUnderLine(String str) {
        int start = spannableString.length();
        spannableString.append(str);
        spannableString.setSpan(new UnderlineSpan(), start, start + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 给某段字符设置下划线
     */
    public SpannableStringBuilderUtil appendStrikethroughLine(String str) {
        int start = spannableString.length();
        spannableString.append(str);
        spannableString.setSpan(new StrikethroughSpan(), start, start + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 给某段字符设置点击事件
     */
    public SpannableStringBuilderUtil appendClickable(String str, final View.OnClickListener clickListener) {
        int start = spannableString.length();
        spannableString.append(str);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                clickListener.onClick(widget);
            }
        }, start, start + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
    public SpannableStringBuilderUtil appendLink(String str, String url) {
        int start = spannableString.length();
        spannableString.append(str);
        spannableString.setSpan(new URLSpan(url), start, start + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 高亮整段字符串
     */
    public SpannableStringBuilderUtil appendHighLight(String str, int highlightColor) {
        int start = spannableString.length();
        spannableString.append(str);
        spannableString.setSpan(new ForegroundColorSpan(highlightColor), start, start + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }


    /**
     * 给一段文字设置背景色
     */
    public SpannableStringBuilderUtil appendBackgroundColor(String str, int basckgroundColor) {
        int start = spannableString.length();
        spannableString.append(str);
        spannableString.setSpan(new BackgroundColorSpan(basckgroundColor), start, start + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }


    /**
     * 给一段文字设置字号
     */
    public SpannableStringBuilderUtil appendTextSize(String str, int textSize) {
        int start = spannableString.length();
        spannableString.append(str);
        spannableString.setSpan(new AbsoluteSizeSpan(textSize), start, start + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }


    /**
     * 给一段文字设置下标
     */
    public SpannableStringBuilderUtil appendSubScript(String str) {
        int start = spannableString.length();
        spannableString.append(str);
        spannableString.setSpan(new SubscriptSpan(), start, start + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }


    /**
     * 给一段文字设置上标
     */
    public SpannableStringBuilderUtil appendSuperScript(String str) {
        int start = spannableString.length();
        spannableString.append(str);
        spannableString.setSpan(new SuperscriptSpan(), start, start + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 给一段文字设置加粗
     */
    public SpannableStringBuilderUtil appendBold(String str) {
        int start = spannableString.length();
        spannableString.append(str);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, start + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }


    /**
     * 给一段文字设置斜体
     */
    public SpannableStringBuilderUtil appendItalic(String str) {
        int start = spannableString.length();
        spannableString.append(str);
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC), start, start + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 给一段文字设置斜体加粗
     */
    public SpannableStringBuilderUtil appendItalicBold(String str) {
        int start = spannableString.length();
        spannableString.append(str);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, start + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }


    /**
     * 给一段文字设置Style
     */
    public SpannableStringBuilderUtil appendStyle(String str, int textApperence) {
        int start = spannableString.length();
        spannableString.append(str);
        spannableString.setSpan(new TextAppearanceSpan(BaseApp.APP_CONTEXT, textApperence), start, start + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 插入图片
     */
    public SpannableStringBuilderUtil appendImage(Drawable drawable) {
        int start = spannableString.length();
        spannableString.setSpan(new ImageSpan(drawable), start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public SpannableStringBuilder build() {
        return spannableString;
    }
}
