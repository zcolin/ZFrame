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
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

/**
 * 皮肤辅助类
 * 在attr中声明的属性，可以使用此类获取对应皮肤的资源
 */
public class ThemeUtil {
    public static int getColor(Context context, int colorAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(new int[]{colorAttr});
        int color = array.getColor(0, Color.WHITE);
        array.recycle();
        return color;
    }

    public static int[] getColors(Context context, int[] colorAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(colorAttr);
        int[] colors = new int[colorAttr.length];
        for (int i = 0; i < colorAttr.length; i++) {
            colors[i] = array.getColor(i, Color.WHITE);
        }
        array.recycle();
        return colors;
    }

    public static ColorStateList getColorStateList(Context context, int stateListAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(new int[]{stateListAttr});
        ColorStateList colorStateList = array.getColorStateList(0);
        array.recycle();
        return colorStateList;
    }

    public static ColorStateList[] getColorStateLists(Context context, int[] stateListAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(stateListAttr);
        ColorStateList[] colorStateLists = new ColorStateList[stateListAttr.length];
        for (int i = 0; i < colorStateLists.length; i++) {
            colorStateLists[i] = array.getColorStateList(i);
        }
        array.recycle();
        return colorStateLists;
    }

    public static Drawable getDrawable(Context context, int drawableAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(new int[]{drawableAttr});
        Drawable drawable = array.getDrawable(0);
        array.recycle();
        return drawable;
    }

    public static Drawable[] getDrawables(Context context, int[] drawableAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(drawableAttr);
        Drawable[] drawables = new Drawable[drawableAttr.length];
        for (int i = 0; i < drawableAttr.length; i++) {
            drawables[i] = array.getDrawable(i);
        }
        array.recycle();
        return drawables;
    }

    public static int getResource(Context context, int styleAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(new int[]{styleAttr});
        int styles = array.getResourceId(0, -1);
        array.recycle();
        return styles;
    }
}
