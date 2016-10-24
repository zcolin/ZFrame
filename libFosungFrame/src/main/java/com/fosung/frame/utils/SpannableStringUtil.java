/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/
package com.fosung.frame.utils;

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

import com.fosung.frame.app.BaseApp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * SpannableString字符串工具类
 */
public class SpannableStringUtil {

    /**
     * 给某段字符设置下划线
     *
     * @param str          整段字符串
     * @param underLineStr 需要设置下划线的字符
     */
    public static SpannableString underLine(String str, String underLineStr) {
        int index = str.indexOf(underLineStr);
        return new SpannableStringUtil.Builder(str).underLine(index, index + underLineStr.length())
                                                   .build();
    }

    /**
     * 给某段字符设置下划线
     */
    public static SpannableString underLine(SpannableString sp, int start, int end) {
        return new SpannableStringUtil.Builder(sp).underLine(start, end)
                                                  .build();
    }

    /**
     * 给某段字符设置下划线
     *
     * @param str              整段字符串
     * @param strikethroughStr 需要设置中划线的字符
     */
    public static SpannableString strikethroughLine(String str, String strikethroughStr) {
        int index = str.indexOf(strikethroughStr);
        return new SpannableStringUtil.Builder(str).strikethroughLine(index, index + strikethroughStr.length())
                                                   .build();
    }

    /**
     * 给某段字符设置下划线
     */
    public static SpannableString strikethroughLine(SpannableString sp, int start, int end) {
        return new SpannableStringUtil.Builder(sp).strikethroughLine(start, end)
                                                  .build();
    }

    /**
     * 给某段字符设置点击事件
     *
     * @param str           整段字符串
     * @param clickableStr  需要设置点击事件的字符
     * @param clickListener 点击事件回调
     */
    public static SpannableString clickable(String str, String clickableStr, final View.OnClickListener clickListener) {
        int index = str.indexOf(clickableStr);
        return new SpannableStringUtil.Builder(str).clickable(index, index + clickableStr.length(), clickListener)
                                                   .build();
    }


    /**
     * 给某段字符设置点击事件
     */
    public static SpannableString clickable(SpannableString sp, int start, int end, final View.OnClickListener clickListener) {
        return new SpannableStringUtil.Builder(sp).clickable(start, end, clickListener)
                                                  .build();
    }

    /**
     * 创建一个含有超链接的字符串
     *
     * @param str      整段字符串
     * @param clickStr 含有超链接的字符
     * @param url      超链接地址 文本对应的链接地址，需要注意格式：
     *                 （1）电话以"tel:"打头，比如"tel:02355692427"
     *                 （2）邮件以"mailto:"打头，比如"mailto:zmywly8866@gmail.com"
     *                 （3）短信以"sms:"打头，比如"sms:02355692427"
     *                 （4）彩信以"mms:"打头，比如"mms:02355692427"
     *                 （5）地图以"geo:"打头，比如"geo:68.426537,68.123456"
     *                 （6）网络以"http://"打头，比如"http://www.google.com"
     */
    public static SpannableString link(String str, String clickStr, String url) {
        int index = str.indexOf(clickStr);
        return new SpannableStringUtil.Builder(str).link(index, index + clickStr.length(), url)
                                                   .build();
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
        return new SpannableStringUtil.Builder(sp).link(start, end, url)
                                                  .build();
    }

    /**
     * 高亮所有关键字
     *
     * @param str            整段字符串
     * @param key            关键字
     * @param highlightColor 高亮颜色
     */
    public static SpannableString highlightKeyword(String str, String key, int highlightColor) {
        SpannableString sp = new SpannableString(str);
        Pattern p = Pattern.compile(key);
        Matcher m = p.matcher(str);

        while (m.find()) {  //通过正则查找，逐个高亮
            int start = m.start();
            int end = m.end();
            sp.setSpan(new ForegroundColorSpan(highlightColor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sp;
    }

    /**
     * 高亮整段字符串
     *
     * @param str            整段字符串
     * @param highlightColor 高亮颜色
     */
    public static SpannableString highLight(String str, int highlightColor) {
        return new SpannableStringUtil.Builder(str).highLight(0, str.length(), highlightColor)
                                                   .build();
    }

    /**
     * 高亮整段字符串
     */
    public static SpannableString highLight(SpannableString sp, int start, int end, int highlightColor) {
        return new SpannableStringUtil.Builder(sp).highLight(start, end, highlightColor)
                                                  .build();
    }


    /**
     * 高亮一段文字
     *
     * @param str            整段字符串
     * @param highLightStr   要高亮的代码段
     * @param highlightColor 高亮颜色
     */
    public static SpannableString highLight(String str, String highLightStr, int highlightColor) {
        int start = str.indexOf(highLightStr);
        return new SpannableStringUtil.Builder(str).highLight(start, start + highLightStr.length(), highlightColor)
                                                   .build();
    }

    /**
     * 给一段文字设置背景色
     *
     * @param str              整段字符串
     * @param backgroundStr    要设置背景色的代码段
     * @param basckgroundColor 背景颜色
     */
    public static SpannableString backgroundColor(String str, String backgroundStr, int basckgroundColor) {
        int start = str.indexOf(backgroundStr);
        return new SpannableStringUtil.Builder(str).backgroundColor(start, start + backgroundStr.length(), basckgroundColor)
                                                   .build();
    }

    /**
     * 给一段文字设置背景色
     */
    public static SpannableString backgroundColor(SpannableString sp, int start, int end, int basckgroundColor) {
        return new SpannableStringUtil.Builder(sp).backgroundColor(start, end, basckgroundColor)
                                                  .build();
    }


    /**
     * 给一段文字设置字号
     *
     * @param str         整段字符串
     * @param textSizeStr 要设置字体大小的代码段
     * @param textSize    字体大小  sp
     */
    public static SpannableString textSize(String str, String textSizeStr, int textSize) {
        textSize = DisplayUtil.sp2px(BaseApp.APP_CONTEXT, textSize);
        int start = str.indexOf(textSizeStr);
        return new SpannableStringUtil.Builder(str).textSize(start, start + textSizeStr.length(), textSize)
                                                   .build();
    }


    /**
     * 给一段文字设置字号
     */
    public static SpannableString textSize(SpannableString sp, int start, int end, int textSize) {
        return new SpannableStringUtil.Builder(sp).textSize(start, end, textSize)
                                                  .build();
    }

    /**
     * 给一段文字设置下标
     *
     * @param str          整段字符串
     * @param subscriptStr 要设置下标字段
     */
    public static SpannableString subScript(String str, String subscriptStr) {
        int start = str.indexOf(subscriptStr);
        return new SpannableStringUtil.Builder(str).subScript(start, start + subscriptStr.length())
                                                   .build();
    }

    /**
     * 给一段文字设置下标
     */
    public static SpannableString subScript(SpannableString sp, int start, int end) {
        return new SpannableStringUtil.Builder(sp).subScript(start, end)
                                                  .build();
    }


    /**
     * 给一段文字设置上标
     *
     * @param str            整段字符串
     * @param superScriptStr 要设置上标字段
     */
    public static SpannableString superScript(String str, String superScriptStr) {
        int start = str.indexOf(superScriptStr);
        return new SpannableStringUtil.Builder(str).superScript(start, start + superScriptStr.length())
                                                   .build();
    }

    /**
     * 给一段文字设置上标
     */
    public static SpannableString superScript(SpannableString sp, int start, int end) {
        return new SpannableStringUtil.Builder(sp).superScript(start, end)
                                                  .build();
    }

    /**
     * 给一段文字设置加粗
     *
     * @param str     整段字符串
     * @param boldStr 要设置加粗字段
     */
    public static SpannableString bold(String str, String boldStr) {
        int start = str.indexOf(boldStr);
        return new SpannableStringUtil.Builder(str).bold(start, start + boldStr.length())
                                                   .build();
    }

    /**
     * 给一段文字设置加粗
     */
    public static SpannableString bold(SpannableString sp, int start, int end) {
        return new SpannableStringUtil.Builder(sp).bold(start, end)
                                                  .build();
    }

    /**
     * 给一段文字设置斜体
     *
     * @param str       整段字符串
     * @param italicStr 要设置斜体字段
     */
    public static SpannableString italic(String str, String italicStr) {
        int start = str.indexOf(italicStr);
        return new SpannableStringUtil.Builder(str).italic(start, start + italicStr.length())
                                                   .build();
    }

    /**
     * 给一段文字设置斜体
     */
    public static SpannableString italic(SpannableString sp, int start, int end) {
        return new SpannableStringUtil.Builder(sp).italic(start, end)
                                                  .build();
    }

    /**
     * 给一段文字设置斜体加粗
     *
     * @param str           整段字符串
     * @param italicBoldStr 要设置斜体加粗字段
     */
    public static SpannableString italicBold(String str, String italicBoldStr) {
        int start = str.indexOf(italicBoldStr);
        return new SpannableStringUtil.Builder(str).italicBold(start, start + italicBoldStr.length())
                                                   .build();
    }

    /**
     * 给一段文字设置斜体加粗
     */
    public static SpannableString italicBold(SpannableString sp, int start, int end) {
        return new SpannableStringUtil.Builder(sp).italicBold(start, end)
                                                  .build();
    }

    /**
     * 给一段文字设置Style
     *
     * @param str               整段字符串
     * @param tetxtApperenceStr 要设置样式的字符串
     * @param tetxtApperence    要设置样式
     */
    public static SpannableString style(String str, String tetxtApperenceStr, int tetxtApperence) {
        int start = str.indexOf(tetxtApperenceStr);
        return new SpannableStringUtil.Builder(str).style(start, start + tetxtApperenceStr.length(), tetxtApperence)
                                                   .build();
    }

    /**
     * 给一段文字设置Style
     */
    public static SpannableString style(SpannableString sp, int start, int end, int textApperence) {
        return new SpannableStringUtil.Builder(sp).style(start, end, textApperence)
                                                  .build();
    }

    /**
     * 插入图片
     *
     * @param str      整段字符串
     * @param imageStr 要设置斜体加粗字段
     * @param drawable 图片
     */
    public static SpannableString image(String str, String imageStr, Drawable drawable) {
        int start = str.indexOf(imageStr);
        return new SpannableStringUtil.Builder(str).image(start, start + imageStr.length(), drawable)
                                                   .build();
    }

    /**
     * 插入图片
     */
    public static SpannableString image(SpannableString sp, int start, int end, Drawable drawable) {
        return new SpannableStringUtil.Builder(sp).image(start, end, drawable)
                                                  .build();
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
