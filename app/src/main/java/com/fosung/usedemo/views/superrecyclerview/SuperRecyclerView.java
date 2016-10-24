/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-11 下午5:31
 * *********************************************************
 */

package com.fosung.usedemo.views.superrecyclerview;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;

import com.fosung.usedemo.views.pullrecyclerview.BaseRecyclerAdapter;


/**
 * Created by super南仔 on 07/28/16.
 * blog: http://supercwn.github.io/
 * GitHub: https://github.com/supercwn
 * <p/>
 * 下拉刷新组件
 * update by colin on 2016-10-12
 */
public class SuperRecyclerView extends android.support.v7.widget.RecyclerView {
    private static final float DRAG_RATE            = 3;
    //下面的ItemViewType是保留值(ReservedItemViewType),如果用户的adapter与它们重复将会强制抛出异常。不过为了简化,我们检测到重复时对用户的提示是ItemViewType必须小于10000
    private static final int   TYPE_REFRESH_HEADER  = 100000;//设置一个很大的数字,尽可能避免和用户的adapter冲突
    private static final int   TYPE_LOADMORE_FOOTER = 100001;

    private boolean isLoadingData             = false;
    private boolean isNoMore                  = false;
    private int     mRefreshProgressStyle     = ProgressStyle.SysProgress;
    private int     mLoadingMoreProgressStyle = ProgressStyle.SysProgress;
    private float   mLastY                    = -1;

    private WrapAdapter                            mWrapAdapter;
    private SuperRecyclerView.PullLoadMoreListener mLoadingListener;
    private ArrowRefreshHeader                     mRefreshHeader;
    private View                                   mLoadMoreFootView;
    private View                                   mEmptyView;

    private boolean refreshEnabled     = true;
    private boolean loadingMoreEnabled = true;

    private final AdapterDataObserver             mEmptyDataObserver = new DataObserver();
    private       AppBarStateChangeListener.State appbarState        = AppBarStateChangeListener.State.EXPANDED;
    private boolean hasRegisterEmptyObserver;

    public SuperRecyclerView(Context context) {
        this(context, null);
    }

    public SuperRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (refreshEnabled) {
            mRefreshHeader = new ArrowRefreshHeader(getContext());
            mRefreshHeader.setProgressStyle(mRefreshProgressStyle);
        }
        
        /*设置默认的footer*/
        LoadingMoreFooter footView = new LoadingMoreFooter(getContext());
        footView.setProgressStyle(mLoadingMoreProgressStyle);
        mLoadMoreFootView = footView;
        mLoadMoreFootView.setVisibility(GONE);
    }

    /**
     * LinearLayoutManager
     */
    public void setLinearLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        super.setLayoutManager(linearLayoutManager);
    }

    /**
     * GridLayoutManager
     */
    public void setGridLayout(int spanCount) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), spanCount);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(gridLayoutManager);
    }

    /**
     * StaggeredGridLayoutManager
     */
    public void setStaggeredGridLayout(int spanCount) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(spanCount, LinearLayoutManager.VERTICAL);
        setLayoutManager(staggeredGridLayoutManager);
    }

    /**
     * 滚动到顶部
     */
    public void scrollToTop() {
        scrollToPosition(0);
    }

    /**
     * 设置自定义的FooterView
     */
    public void setLoadMoreFootView(final View view) {
        mLoadMoreFootView = view;
    }

    /**
     * 设置自定义的header
     */
    public void setRefreshHeader(ArrowRefreshHeader refreshHeader) {
        mRefreshHeader = refreshHeader;
    }

    /**
     * 下拉刷新是否可用
     */
    public void setRefreshEnabled(boolean enabled) {
        refreshEnabled = enabled;
    }

    /**
     * 到底加载是否可用
     */
    public void setLoadMoreEnabled(boolean enabled) {
        loadingMoreEnabled = enabled;
        if (!enabled) {
            if (mLoadMoreFootView instanceof LoadingMoreFooter) {
                ((LoadingMoreFooter) mLoadMoreFootView).setState(LoadingMoreFooter.STATE_COMPLETE);
            }
        }
    }

    /**
     * 设置下拉刷新的进度条风格
     */
    public void setRefreshProgressStyle(int style) {
        mRefreshProgressStyle = style;
        if (mRefreshHeader != null) {
            mRefreshHeader.setProgressStyle(style);
        }
    }

    /**
     * 设置加载更多的进度条风格
     */
    public void setLoadingMoreProgressStyle(int style) {
        mLoadingMoreProgressStyle = style;
        if (mLoadMoreFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mLoadMoreFootView).setProgressStyle(style);
        }
    }

    /**
     * 设置下拉刷新的箭头图标
     */
    public void setArrowImageView(int resId) {
        if (mRefreshHeader != null) {
            mRefreshHeader.setArrowImageView(resId);
        }
    }

    /**
     * 设置没有数据的EmptyView
     * <p/>
     * <p>注意：如果调用此函数，会将RecyclerView从原来的布局中移除添加到一个RelativeLayout中，然后将RelativeLayout放置到原来的布局中，
     * 也就是说，在RecyclerView和其父布局中间添加了一次RelitiveLayout，用来盛放RecyclerView和emptyView<p/>
     */
    public void setEmptyView(View emptyView) {
        RelativeLayout.LayoutParams emptyViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        emptyViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        ViewGroup group = (ViewGroup) getParent();
        RelativeLayout container = new RelativeLayout(getContext());
        int index = group.indexOfChild(this);
        group.removeView(this);
        group.addView(container, index, getLayoutParams());
        container.addView(this, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.addView(emptyView, emptyViewParams);

        this.mEmptyView = emptyView;
        this.mEmptyView.setVisibility(View.GONE);
        //mEmptyDataObserver.onChanged();
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * 手动调用下拉刷新，无下拉效果
     */
    public void refresh() {
        if (mLoadingListener != null) {
            mLoadingListener.onRefresh();
        }
    }

    /**
     * 手动调用下拉刷新，有下拉效果
     */
    public void refreshWithPull() {
        setRefreshing(true);
        refresh();
    }

    public void loadMore() {
        if (mLoadingListener != null && !isNoMore) {
            mLoadingListener.onLoadMore();
        }
    }

    /**
     * 下拉刷新和到底加载完成
     */
    public void setPullLoadMoreCompleted() {
        if (mRefreshHeader.getState() != BaseRefreshHeader.STATE_NORMAL) {
            mRefreshHeader.refreshComplete();
            setNoMore(false);
        }

        isLoadingData = false;
        if (mLoadMoreFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mLoadMoreFootView).setState(LoadingMoreFooter.STATE_COMPLETE);
        } else {
            mLoadMoreFootView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置是否已加载全部
     */
    public void setNoMore(boolean noMore) {
        isLoadingData = false;
        isNoMore = noMore;
        if (mLoadMoreFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mLoadMoreFootView).setState(isNoMore ? LoadingMoreFooter.STATE_NOMORE : LoadingMoreFooter.STATE_COMPLETE);
        } else {
            mLoadMoreFootView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mEmptyDataObserver);
        //        mEmptyDataObserver.onChanged();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE && mLoadingListener != null && !isLoadingData && loadingMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1 && layoutManager.getItemCount() > layoutManager.getChildCount() && !isNoMore && mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
                isLoadingData = true;
                if (mLoadMoreFootView instanceof LoadingMoreFooter) {
                    ((LoadingMoreFooter) mLoadMoreFootView).setState(LoadingMoreFooter.STATE_LOADING);
                } else {
                    mLoadMoreFootView.setVisibility(View.VISIBLE);
                }
                mLoadingListener.onLoadMore();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (isOnTop() && refreshEnabled && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    mRefreshHeader.onMove(deltaY / DRAG_RATE);
                    if (mRefreshHeader.getVisibleHeight() > 0 && mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            default:
                mLastY = -1; // reset
                if (isOnTop() && refreshEnabled && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    if (mRefreshHeader.releaseAction()) {
                        if (mLoadingListener != null) {
                            mLoadingListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private boolean isOnTop() {
        if (mRefreshHeader.getParent() != null) {
            return true;
        } else {
            return false;
        }
    }

    public void setOnPullLoadMoreListener(SuperRecyclerView.PullLoadMoreListener listener) {
        mLoadingListener = listener;
    }

    public void setRefreshing(boolean refreshing) {
        if (refreshing && refreshEnabled && mLoadingListener != null) {
            mRefreshHeader.setState(ArrowRefreshHeader.STATE_REFRESHING);
            mRefreshHeader.onMove(mRefreshHeader.getMeasuredHeight());
            //mLoadingListener.onRefresh();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //解决和CollapsingToolbarLayout冲突的问题
        AppBarLayout appBarLayout = null;
        ViewParent p = getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }

        if (p != null) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if (child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout) child;
                    break;
                }
            }
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {
                        appbarState = state;
                    }
                });
            }
        }

        Adapter<?> adapter = getAdapter();
        if (adapter != null && !hasRegisterEmptyObserver && mEmptyDataObserver != null) {
            adapter.registerAdapterDataObserver(mEmptyDataObserver);
            hasRegisterEmptyObserver = true;
        }
    }

    /**
     * When view detached from window , unregister adapter data observer, avoid momery leak.
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Adapter<?> adapter = getAdapter();
        if (adapter != null && hasRegisterEmptyObserver) {
            adapter.unregisterAdapterDataObserver(mEmptyDataObserver);
            hasRegisterEmptyObserver = false;
        }
    }


    private class DataObserver extends AdapterDataObserver {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                int emptyCount = 1;
                if (refreshEnabled) {
                    emptyCount++;
                }
                if (loadingMoreEnabled) {
                    emptyCount++;
                }
                if (adapter.getItemCount() == emptyCount) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    SuperRecyclerView.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    SuperRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    public class WrapAdapter extends Adapter<ViewHolder> {

        private Adapter adapter;

        public WrapAdapter(Adapter adapter) {
            this.adapter = adapter;
        }

        public boolean isHeader(int position) {
            return position >= 1 && position < 1;
        }

        public boolean isFooter(int position) {
            if (loadingMoreEnabled) {
                return position == getItemCount() - 1;
            } else {
                return false;
            }
        }

        public boolean isRefreshHeader(int position) {
            return position == 0;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_REFRESH_HEADER) {
                return new BaseRecyclerAdapter.CommonHolder((android.support.v7.widget.RecyclerView) parent, mRefreshHeader);
            } else if (viewType == TYPE_LOADMORE_FOOTER) {
                return new BaseRecyclerAdapter.CommonHolder((android.support.v7.widget.RecyclerView) parent, mLoadMoreFootView);
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            if (isHeader(position) || isRefreshHeader(position)) {
                return;
            }
            int adjPosition = position - 1;
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                    return;
                }
            }
        }

        @Override
        public int getItemCount() {
            if (loadingMoreEnabled) {
                if (adapter != null) {
                    return adapter.getItemCount() + 2;
                } else {
                    return 2;
                }
            } else {
                if (adapter != null) {
                    return adapter.getItemCount() + 1;
                } else {
                    return 1;
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            int adjPosition = position - 1;
            if (isReservedItemViewType(adapter.getItemViewType(adjPosition))) {
                throw new IllegalStateException("XRecyclerView require itemViewType in adapter should be less than 10000 ");
            }
            if (isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER;
            }
            if (isFooter(position)) {
                return TYPE_LOADMORE_FOOTER;
            }

            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemViewType(adjPosition);
                }
            }
            return 0;
        }

        //判断是否是SuperRecyclerView保留的itemViewType
        private boolean isReservedItemViewType(int itemViewType) {
            if (itemViewType == TYPE_REFRESH_HEADER || itemViewType == TYPE_LOADMORE_FOOTER) {
                return true;
            } else {
                return false;
            }
        }


        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= 1) {
                int adjPosition = position - 1;
                if (adjPosition < adapter.getItemCount()) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        @Override
        public void onAttachedToRecyclerView(android.support.v7.widget.RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeader(position) || isFooter(position) || isRefreshHeader(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(android.support.v7.widget.RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.getLayoutPosition()) || isRefreshHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }
    }

    public interface PullLoadMoreListener {

        void onRefresh();

        void onLoadMore();
    }
}