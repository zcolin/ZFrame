/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-13 上午10:46
 * *********************************************************
 */

package com.fosung.usedemo.views.pullrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

import com.fosung.usedemo.R;
import com.fosung.usedemo.views.pullrecyclerview.swipemenu.SwipeMenuLayout;
import com.fosung.usedemo.views.pullrecyclerview.swipemenu.SwipeMenuRecyclerView;


/**
 * 支持下拉、上拉、侧拉菜单的RecyclerView
 */
public class PullSwipeMenuRecyclerView extends PullRecyclerView {
    private SwipeMenuRecyclerView swipeMenuRecyclerView;

    public PullSwipeMenuRecyclerView(Context context) {
        this(context, null);
    }

    public PullSwipeMenuRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.view_swipemenu_pullrecycler);
        swipeMenuRecyclerView = (SwipeMenuRecyclerView) getRecyclerView();
    }

    public void setCloseInterpolator(Interpolator interpolator) {
        swipeMenuRecyclerView.setCloseInterpolator(interpolator);
    }

    public void setOpenInterpolator(Interpolator interpolator) {
        swipeMenuRecyclerView.setOpenInterpolator(interpolator);
    }

    public Interpolator getOpenInterpolator() {
        return swipeMenuRecyclerView.getOpenInterpolator();
    }

    public Interpolator getCloseInterpolator() {
        return swipeMenuRecyclerView.getCloseInterpolator();
    }
    /**
     * open menu manually
     * @param position the adapter position
     */
    public void smoothOpenMenu(int position) {
        swipeMenuRecyclerView.smoothOpenMenu(position);
    }

    /**
     * close the opened menu manually
     */
    public void smoothCloseMenu() {
        swipeMenuRecyclerView.smoothCloseMenu();
    }

    public void setOnSwipeListener(SwipeMenuRecyclerView.OnSwipeListener onSwipeListener) {
        swipeMenuRecyclerView.setOnSwipeListener(onSwipeListener);
    }

    /**
     * get current touched view
     * @return touched view, maybe null
     */
    public SwipeMenuLayout getTouchView() {
        return swipeMenuRecyclerView.getTouchView();
    }


    /**
     * set the swipe direction
     * @param direction swipe direction (left or right)
     */
    public void setSwipeDirection(int direction) {
        swipeMenuRecyclerView.setSwipeDirection(direction);
    }
}
