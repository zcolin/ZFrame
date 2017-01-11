package com.fosung.frame.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

import java.util.ArrayList;

/**
 */
public class StateListUtil {
    private int normal;
    private int pressed;
    private int selected;
    private int focused;
    private int checked;
    private int disabled;

    public static StateListUtil instance() {
        return new StateListUtil();
    }

    public StateListUtil setNormal(int normal) {
        this.normal = normal;
        return this;
    }

    public StateListUtil setPressed(int pressed) {
        this.pressed = pressed;
        return this;
    }

    public StateListUtil setSelected(int selected) {
        this.selected = selected;
        return this;
    }

    public StateListUtil setFocused(int focused) {
        this.focused = focused;
        return this;
    }

    public StateListUtil setChecked(int checked) {
        this.checked = checked;
        return this;
    }

    public StateListUtil setDisabled(int disabled) {
        this.disabled = disabled;
        return this;
    }

    /**
     * 生成颜色StateList
     * <p>
     * 设置的资源必须为颜色资源
     */
    public ColorStateList createColorStateList() {
        return new ColorStateList(getStates(), getResources());
    }

    /**
     * 生成图片StateList
     * <p>
     * 设置的资源必须为图片资源
     */
    public Drawable createDrawableStateList(Context context) {
        int[] resources = getResources();
        int[][] states = getStates();

        if (resources.length != states.length) {
            throw new IllegalStateException("数据出错");
        }

        StateListDrawable statesListDrawable = new StateListDrawable();
        for (int i = 0; i < resources.length; i++) {
            statesListDrawable.addState(states[i], context.getResources()
                                                          .getDrawable(resources[i]));
        }
        return statesListDrawable;
    }

    /**
     * 生成圆角纯色背景的StateListDrawable
     * <p>
     * 设置的资源必须为颜色资源
     */
    public Drawable createRoundCornerColorDrawableStateList(Context context, int radius) {
        int[] resources = getResources();
        int[][] states = getStates();

        if (resources.length != states.length) {
            throw new IllegalStateException("数据出错");
        }

        StateListDrawable statesListDrawable = new StateListDrawable();
        for (int i = 0; i < resources.length; i++) {
            GradientDrawable gdSelect = new GradientDrawable();
            gdSelect.setColor(resources[i]);
            gdSelect.setCornerRadius(radius);
            statesListDrawable.addState(states[i], gdSelect);
        }
        return statesListDrawable;
    }

    private int[] getResources() {
        ArrayList<Integer> listColor = new ArrayList<>();
        if (pressed != 0) {
            listColor.add(pressed);
        }
        if (selected != 0) {
            listColor.add(selected);
        }
        if (focused != 0) {
            listColor.add(focused);
        }
        if (checked != 0) {
            listColor.add(checked);
        }
        if (normal != 0) {
            listColor.add(normal);
        }
        if (disabled != 0) {
            listColor.add(disabled);
        }

        int[] arrColor = new int[listColor.size()];
        for (int i = 0; i < listColor.size(); i++) {
            arrColor[i] = listColor.get(i);
        }
        return arrColor;
    }

    private int[][] getStates() {
        ArrayList<int[]> listState = new ArrayList<>();
        if (pressed != 0) {
            listState.add(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled});
        }
        if (selected != 0) {
            listState.add(new int[]{android.R.attr.state_selected, android.R.attr.state_enabled});
        }
        if (focused != 0) {
            listState.add(new int[]{android.R.attr.state_focused, android.R.attr.state_enabled});
        }
        if (checked != 0) {
            listState.add(new int[]{android.R.attr.state_checked, android.R.attr.state_enabled});
        }
        if (normal != 0) {
            listState.add(new int[]{android.R.attr.state_enabled});
        }
        if (disabled != 0) {
            listState.add(new int[]{});
        }

        return listState.toArray(new int[listState.size()][]);
    }
}
