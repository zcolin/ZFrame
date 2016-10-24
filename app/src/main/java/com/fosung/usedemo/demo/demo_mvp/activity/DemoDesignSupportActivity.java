/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-11 下午4:05
 * *********************************************************
 */

package com.fosung.usedemo.demo.demo_mvp.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fosung.frame.app.BaseFrameActivity;
import com.fosung.frame.utils.ToastUtil;
import com.fosung.usedemo.R;
import com.fosung.usedemo.demo.demo_mvp.adapter.DemoPullRecyclerMultiItemAdapter;
import com.fosung.usedemo.demo.demo_mvp.contract.DemoPullRecyclerContract;
import com.fosung.usedemo.demo.demo_mvp.presenter.DemoPullRecyclerPresenter;
import com.fosung.usedemo.views.pullrecyclerview.BaseRecyclerAdapter;
import com.fosung.usedemo.views.pullrecyclerview.PullRecyclerView;

import java.util.ArrayList;


/**
 *
 */
public class DemoDesignSupportActivity extends BaseFrameActivity implements DemoPullRecyclerContract.View {

    private PullRecyclerView                   mPullRecyclerView;
    private DemoPullRecyclerMultiItemAdapter   mRecyclerViewAdapter;
    private DemoPullRecyclerContract.Presenter presenter;
    private int mPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_demo_activity_designsupport);
        init();
    }

    private void init() {
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "FloatActionBar-click", Snackbar.LENGTH_LONG)
                        .setAction("toast", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.toastShort("button-click");
                            }
                        });
            }
        });


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
        mPullRecyclerView.setEmptyView(LayoutInflater.from(this)
                                                     .inflate(R.layout.view_pullrecycler_empty, null));//setEmptyView

        presenter = new DemoPullRecyclerPresenter(this);
        presenter.getDataFromShopList(this, mPage);
    }


    @Override
    public void addDataToRecyclerView(ArrayList<String> list, boolean isClear) {
        if (mRecyclerViewAdapter == null) {
            mRecyclerViewAdapter = new DemoPullRecyclerMultiItemAdapter();
            TextView tvHeader = new TextView(this);
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
            } else {
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
            presenter.getDataFromShopList(DemoDesignSupportActivity.this, mPage);
        }

        @Override
        public void onLoadMore() {
            mPage = mPage + 1;
            presenter.getDataFromShopList(DemoDesignSupportActivity.this, mPage);
        }
    }
}
