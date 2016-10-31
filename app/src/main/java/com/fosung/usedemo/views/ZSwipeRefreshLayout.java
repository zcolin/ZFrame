/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-8 下午3:55
 * *********************************************************
 */
package com.fosung.usedemo.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * 设定了颜色的自定义的SwipeRefreshLayout，用来统一颜色
 */
public class ZSwipeRefreshLayout extends SwipeRefreshLayout {
    private final int     mTouchSlop;
    private       float   startY;
    private       float   startX;
    private       boolean mIsVpDragger;// 记录viewPager是否拖拽的标记
    private       boolean isProceeConflict;// 是否初始冲突，如ViewPager

    public ZSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public ZSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeColors(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        mTouchSlop = ViewConfiguration.get(context)
                                      .getScaledTouchSlop();
    }

    /**
     * 设置是否处理冲突，默认不处理
     */
    public void setIsProceeConflict(boolean isProceeConflict) {
        this.isProceeConflict = isProceeConflict;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isProceeConflict) {
            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // 记录手指按下的位置
                    startY = ev.getY();
                    startX = ev.getX();
                    // 初始化标记
                    mIsVpDragger = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    // 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
                    if (mIsVpDragger) {
                        return false;
                    }

                    // 获取当前手指位置
                    float endY = ev.getY();
                    float endX = ev.getX();
                    float distanceX = Math.abs(endX - startX);
                    float distanceY = Math.abs(endY - startY);
                    // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                    if (distanceX > mTouchSlop && distanceX > distanceY) {
                        mIsVpDragger = true;
                        return false;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mIsVpDragger = false; // 初始化标记
                    break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }
}
