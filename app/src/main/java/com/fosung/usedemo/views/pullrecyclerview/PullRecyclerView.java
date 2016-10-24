/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-13 上午11:45
 * *********************************************************
 */

package com.fosung.usedemo.views.pullrecyclerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fosung.usedemo.R;
import com.fosung.usedemo.views.ZSwipeRefreshLayout;


/**
 * Created by WuXiaolong on 2015/7/2.
 * github:https://github.com/WuXiaolong/PullLoadMoreRecyclerView
 * weibo:http://weibo.com/u/2175011601
 * version 1.0.8
 * <p>
 * RecyclerView下拉刷新和上拉加载更多以及RecyclerView线性、网格、瀑布流
 * 使用请参照https://github.com/WuXiaolong/PullLoadMoreRecyclerView
 * <p>
 */
public class PullRecyclerView extends LinearLayout {
    private RecyclerView         mRecyclerView;
    private ZSwipeRefreshLayout  mSwipeRefreshLayout;
    private PullLoadMoreListener mPullLoadMoreListener;
    private boolean hasMore                 = true;
    private boolean isRefresh               = false;
    private boolean isLoadMore              = false;
    private boolean pullRefreshEnable       = true;
    private boolean pushRefreshEnable       = true;
    private boolean nestedPullRefreshEnable = true;
    private View                                 mFooterView;
    private RelativeLayout                       mEmptyViewContainer;
    private Context                              mContext;
    private TextView                             loadMoreText;
    private LinearLayout                         loadMoreLayout;
    private PullRecyclerView.AdapterDataObserver mEmptyDataObserver;
    private boolean                              hasRegisterEmptyObserver;
    private Handler handler = new Handler(Looper.getMainLooper());

    public PullRecyclerView(Context context) {
        this(context, null);
    }

    public PullRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRecyclerView(Context context, AttributeSet attrs, int resId) {
        super(context, attrs);
        initView(context, resId);
    }

    private void initView(Context context, int resId) {
        mContext = context;
        View view = LayoutInflater.from(context)
                                  .inflate(resId > 0 ? resId : R.layout.view_pullrecycler, null);
        mSwipeRefreshLayout = (ZSwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutOnRefresh(this));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setVerticalScrollBarEnabled(true);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new RecyclerViewOnScroll(this));

        mRecyclerView.setOnTouchListener(new onTouchRecyclerView());

        mFooterView = view.findViewById(R.id.footerView);
        mEmptyViewContainer = (RelativeLayout) view.findViewById(R.id.emptyView);

        loadMoreLayout = (LinearLayout) view.findViewById(R.id.loadMoreLayout);
        loadMoreText = (TextView) view.findViewById(R.id.loadMoreText);
        mFooterView.setVisibility(View.GONE);
        mEmptyViewContainer.setVisibility(View.GONE);

        this.addView(view);

        initDefStyle();
    }

    private void initDefStyle() {
        setFooterViewText("加载中……");
        setFooterViewTextColor(R.color.black_light);
        setFooterViewBackgroundColor(R.color.bg_color);
    }


    /**
     * LinearLayoutManager
     */
    public void setLinearLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * GridLayoutManager
     */

    public void setGridLayout(int spanCount) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, spanCount);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    /**
     * 增加分割线
     *
     * @param itemDecoration ex:mPullRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL));
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    /**
     * 增加分割线
     *
     * @param itemDecoration ex:mPullRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL), 2);
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        mRecyclerView.addItemDecoration(itemDecoration, index);
    }

    /**
     * StaggeredGridLayoutManager
     */

    public void setStaggeredGridLayout(int spanCount) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(spanCount, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void scrollToTop() {
        mRecyclerView.scrollToPosition(0);
    }


    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            mRecyclerView.setAdapter(adapter);
            if (mEmptyDataObserver == null) {
                mEmptyDataObserver = new PullRecyclerView.AdapterDataObserver();
            }
            adapter.registerAdapterDataObserver(mEmptyDataObserver);
            hasRegisterEmptyObserver = true;
        }
    }

    public void showEmptyView() {

        RecyclerView.Adapter<?> adapter = mRecyclerView.getAdapter();
        if (adapter != null && adapter instanceof BaseRecyclerAdapter) {
            BaseRecyclerAdapter adapter1 = (BaseRecyclerAdapter) adapter;
            if (mEmptyViewContainer.getChildCount() != 0) {
                if (adapter1.getRealItemCount() == 0) {
                    mFooterView.setVisibility(View.GONE);
                    mEmptyViewContainer.setVisibility(VISIBLE);
                } else {
                    mEmptyViewContainer.setVisibility(GONE);
                }
            }
        } else {
            if (adapter != null && mEmptyViewContainer.getChildCount() != 0) {
                if (adapter.getItemCount() == 0) {
                    mFooterView.setVisibility(View.GONE);
                    mEmptyViewContainer.setVisibility(VISIBLE);
                } else {
                    mEmptyViewContainer.setVisibility(GONE);
                }
            }
        }
    }

    public boolean getPullRefreshEnable() {
        return pullRefreshEnable;
    }

    public void setPullRefreshEnable(boolean enable) {
        pullRefreshEnable = enable;
        setSwipeRefreshEnable(enable);
    }

    public boolean isNestedPullRefreshEnable() {
        return nestedPullRefreshEnable;
    }

    public void setNestedPullRefreshEnable(boolean nestedPullRefreshEnable) {
        this.nestedPullRefreshEnable = nestedPullRefreshEnable;
        mSwipeRefreshLayout.setEnabled(nestedPullRefreshEnable);
    }

    public boolean getSwipeRefreshEnable() {
        return mSwipeRefreshLayout.isEnabled();
    }

    public void setSwipeRefreshEnable(boolean enable) {
        if (nestedPullRefreshEnable) {
            mSwipeRefreshLayout.setEnabled(enable);
        }
    }

    public void setColorSchemeResources(int... colorResIds) {
        mSwipeRefreshLayout.setColorSchemeResources(colorResIds);

    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public void setRefreshing(final boolean isRefreshing) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (pullRefreshEnable)
                    mSwipeRefreshLayout.setRefreshing(isRefreshing);
            }
        });
    }


    /**
     * When view detached from window , unregister adapter data observer, avoid momery leak.
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        RecyclerView.Adapter<?> adapter = mRecyclerView.getAdapter();
        if (adapter != null && hasRegisterEmptyObserver) {
            adapter.unregisterAdapterDataObserver(mEmptyDataObserver);
            hasRegisterEmptyObserver = false;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        RecyclerView.Adapter<?> adapter = mRecyclerView.getAdapter();
        if (adapter != null && !hasRegisterEmptyObserver && mEmptyDataObserver != null) {
            adapter.registerAdapterDataObserver(mEmptyDataObserver);
            hasRegisterEmptyObserver = true;
        }
    }

    public boolean getPushRefreshEnable() {
        return pushRefreshEnable;
    }

    public void setPushRefreshEnable(boolean pushRefreshEnable) {
        this.pushRefreshEnable = pushRefreshEnable;
    }

    public LinearLayout getFooterViewLayout() {
        return loadMoreLayout;
    }

    private void setFooterViewBackgroundColor(int color) {
        loadMoreLayout.setBackgroundColor(ContextCompat.getColor(mContext, color));
    }

    private void setFooterViewText(CharSequence text) {
        loadMoreText.setText(text);
    }

    private void setFooterViewText(int resid) {
        loadMoreText.setText(resid);
    }

    private void setFooterViewTextColor(int color) {
        loadMoreText.setTextColor(ContextCompat.getColor(mContext, color));
    }

    public void setEmptyView(View emptyView) {
        mEmptyViewContainer.removeAllViews();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mEmptyViewContainer.addView(emptyView, params);
    }

    public void refresh() {
        if (mPullLoadMoreListener != null) {
            mPullLoadMoreListener.onRefresh();
        }
    }

    public void loadMore() {
        if (mPullLoadMoreListener != null && hasMore) {
            mFooterView.animate()
                       .translationY(0)
                       .setDuration(300)
                       .setInterpolator(new AccelerateDecelerateInterpolator())
                       .setListener(new AnimatorListenerAdapter() {
                           @Override
                           public void onAnimationStart(Animator animation) {
                               mFooterView.setVisibility(View.VISIBLE);
                           }
                       })
                       .start();
            invalidate();
            mPullLoadMoreListener.onLoadMore();

        }
    }

    /**
     * 结束下拉和上拉
     */
    public void setPullLoadMoreCompleted() {
        isRefresh = false;
        setRefreshing(false);

        isLoadMore = false;
        mFooterView.animate()
                   .translationY(mFooterView.getHeight())
                   .setDuration(300)
                   .setInterpolator(new AccelerateDecelerateInterpolator())
                   .start();
    }

    public void setNoMore(boolean noMore) {
        setPushRefreshEnable(false);
    }

    public void setOnPullLoadMoreListener(PullLoadMoreListener listener) {
        mPullLoadMoreListener = listener;
    }

    public boolean isLoadMore() {
        return isLoadMore;
    }

    public void setIsLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public interface PullLoadMoreListener {
        void onRefresh();

        void onLoadMore();
    }

    /**
     * Solve IndexOutOfBoundsException exception
     */
    public class onTouchRecyclerView implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return isRefresh || isLoadMore;
        }
    }

    /**
     * This Observer receives adapter data change.
     * When adapter's item count greater than 0 and empty view has been set,then show the empty view.
     * when adapter's item count is 0 ,then empty view hide.
     */
    private class AdapterDataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            showEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            showEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            showEmptyView();
        }
    }
}
