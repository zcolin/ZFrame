/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */

package com.zcolin.frame.util;

import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import static android.text.Html.FROM_HTML_MODE_LEGACY;
import static android.text.Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE;

/**
 *
 */
public class HtmlUtil {
    /**
     * 默认的fromHtml会增加两个空格，此函数会将默认增加的空格去掉
     */
    public static Spanned fromHtml(String strHtml) {
        if (strHtml == null) {
            return null;
        }

        Spanned spanned;
        if (Build.VERSION.SDK_INT >= 24) {
            spanned = Html.fromHtml(strHtml, FROM_HTML_MODE_LEGACY);
        } else {
            spanned = Html.fromHtml(strHtml);
        }

        SpannableStringBuilder builder;
        if (spanned instanceof SpannableStringBuilder) {
            builder = (SpannableStringBuilder) spanned;
        } else {
            builder = new SpannableStringBuilder(spanned);
        }

        if (builder.length() > 2 && "\n\n".equals(builder.subSequence(builder.length() - 2, builder.length()).toString())) {
            builder.delete(builder.length() - 2, builder.length());
        }

        return builder;
    }

    /**
     * 默认的toHtml会增加一个空格，此函数会将默认增加的空格去掉
     */
    public static String toHtml(Spanned spanned) {
        String strHtmlContent;
        if (Build.VERSION.SDK_INT >= 24) {
            strHtmlContent = Html.toHtml(spanned, TO_HTML_PARAGRAPH_LINES_CONSECUTIVE).replace(" dir=\"ltr\"", "");
        } else {
            strHtmlContent = Html.toHtml(spanned).replace(" dir=\"ltr\"", "");
        }

        if (strHtmlContent.endsWith("\n")) {
            strHtmlContent = strHtmlContent.substring(0, strHtmlContent.length() - 1);
        }
        return strHtmlContent;
    }

    /**
     * 去除所有html标签和换行空格等符号
     */
    public static String fromHtmlWithoutHtml(String str) {
        if (str == null) {
            return null;
        }

        if (Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(str, FROM_HTML_MODE_LEGACY).toString().replaceAll("\\s*", "");
        } else {
            return Html.fromHtml(str).toString().replaceAll("\\s*", "");
        }
    }
}
