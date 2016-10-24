/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-20 下午2:25
 **********************************************************/
package com.fosung.usedemo.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * 设定了颜色的自定义的SwipeRefreshLayout，用来统一颜色
 */
public class ZSwipeRefreshLayout extends SwipeRefreshLayout {
    public ZSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public ZSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeColors(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
    }

}
