/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-18 下午5:24
 **********************************************************/
package com.fosung.usedemo.demo.demo_mvp.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fosung.frame.app.BaseFrameLazyLoadFrag;
import com.fosung.frame.utils.ToastUtil;
import com.fosung.usedemo.R;
import com.fosung.usedemo.demo.demo_mvp.adapter.DemoPullRecyclerAdapter;
import com.fosung.usedemo.demo.demo_mvp.contract.DemoPullRecyclerContract;
import com.fosung.usedemo.demo.demo_mvp.presenter.DemoPullRecyclerPresenter;
import com.fosung.usedemo.views.pullrecyclerview.BaseRecyclerAdapter;
import com.fosung.usedemo.views.pullrecyclerview.PullRecyclerView;

import java.util.ArrayList;


/**
 * PullRecyclerView Demo
 */
public class Demo_PullRecycler_Fragment extends BaseFrameLazyLoadFrag implements DemoPullRecyclerContract.View {

    private PullRecyclerView                   mPullRecyclerView;
    private DemoPullRecyclerAdapter            mRecyclerViewAdapter;
    private DemoPullRecyclerContract.Presenter presenter;
    private int mPage = 1;

    public static Demo_PullRecycler_Fragment newInstance() {
        Bundle args = new Bundle();
        Demo_PullRecycler_Fragment fragment = new Demo_PullRecycler_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootViewLayId() {
        return R.layout.a_demo_fragment_pullrecycler;
    }

    @Override
    protected void lazyLoad() {
        mPullRecyclerView = (PullRecyclerView) getView(R.id.pullLoadMoreRecyclerView);
        //设置下拉刷新是否可见
        //mPullRecyclerView.setRefreshing(true);
        //设置是否可以下拉刷新
        //mPullRecyclerView.setPullRefreshEnable(true);
        //设置是否可以上拉刷新
        //mPullRecyclerView.setPushRefreshEnable(false);
        //显示下拉刷新
        mPullRecyclerView.setRefreshing(true);
        mPullRecyclerView.setLinearLayout();

        mPullRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreListener());
        mPullRecyclerView.setEmptyView(LayoutInflater.from(getContext())
                                                     .inflate(R.layout.view_pullrecycler_empty, null));//setEmptyView

        presenter = new DemoPullRecyclerPresenter(this);
        presenter.getDataFromShopList(getActivity(), mPage);
    }


    @Override
    public void addDataToRecyclerView(ArrayList<String> list, boolean isClear) {
        if (mRecyclerViewAdapter == null) {
            mRecyclerViewAdapter = new DemoPullRecyclerAdapter();
            TextView tvHeader = new TextView(mActivity);
            tvHeader.setText("我是Header");
            tvHeader.setPadding(50, 50, 50, 50);
            mRecyclerViewAdapter.setHeaderView(tvHeader);
            mRecyclerViewAdapter.addDatas(list);
            mPullRecyclerView.setAdapter(mRecyclerViewAdapter);
            mRecyclerViewAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<String>() {
                @Override
                public void onItemClick(View covertView, int position, String data) {
                    ToastUtil.toastShort(position + ":" + data);
                }
            });
        } else {
            if (isClear) {
                mRecyclerViewAdapter.setDatas(list);
            }else {
                mRecyclerViewAdapter.addDatas(list);
            }
        }
    }

    @Override
    public void setPullLoadmoreComplete() {
        mPullRecyclerView.setPullLoadMoreCompleted();
    }


    class PullLoadMoreListener implements PullRecyclerView.PullLoadMoreListener {
        @Override
        public void onRefresh() {
            mPage = 1;
            presenter.getDataFromShopList(getActivity(), mPage);
        }

        @Override
        public void onLoadMore() {
            mPage = mPage + 1;
            presenter.getDataFromShopList(getActivity(), mPage);
        }
    }
}
