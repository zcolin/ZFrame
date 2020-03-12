/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */

package com.zcolin.frame.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 打开或关闭软键盘
 */
public class KeyBoardUtil {
    /**
     * 打卡软键盘
     *
     * @param mContext 上下文
     * @param view     输入框
     */
    public static void openKeybord(Context mContext, View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param mContext 上下文
     * @param view     输入框
     */
    public static void closeKeybord(Context mContext, View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 延时弹出软键盘（软键盘即时弹出有时会出现bug）
     */
    public static void openKeybordWithDelay(final Context context, final View view) {
        view.postDelayed(() -> openKeybord(context, view), 500);
    }
}  