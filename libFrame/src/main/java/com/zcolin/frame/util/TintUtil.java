/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */
package com.zcolin.frame.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * 着色器辅助类，主要用来改变图片的背景颜色
 */
public class TintUtil {
    public static Drawable tintDrawable(Context context, int resDrawable, int color) {
        return tintDrawable(context.getResources().getDrawable(resDrawable), ColorStateList.valueOf(color));
    }

    public static Drawable tintDrawable(Drawable drawable, int color) {
        return tintDrawable(drawable, ColorStateList.valueOf(color));
    }

    /**
     * 不改变原来内存中的图片状态，另起一份
     */
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }


    public static Drawable tintDrawableWithMemory(Context context, int resDrawable, int color) {
        return tintDrawableWithMemory(context.getResources().getDrawable(resDrawable), ColorStateList.valueOf(color));
    }

    public static Drawable tintDrawableWithMemory(Drawable drawable, int color) {
        return tintDrawableWithMemory(drawable, ColorStateList.valueOf(color));
    }

    /**
     * 改变原来内存中的图片状态，内存中共有的图片会被着色
     */
    public static Drawable tintDrawableWithMemory(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }
}
