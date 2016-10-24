/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-18 下午5:25
 **********************************************************/
package com.fosung.usedemo.demo.demo_mvp.adapter;


import android.widget.TextView;

import com.fosung.usedemo.R;
import com.fosung.usedemo.views.pullrecyclerview.BaseRecyclerAdapter;


/**
 * PullRecyclerAdapter示例
 * <p/>
 * pullrecyclerView的Adapter
 */
public class DemoPullRecyclerAdapter extends BaseRecyclerAdapter<String> {

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.a_demo_recycler_view_item;
    }

    @Override
    public void setUpData(BaseRecyclerAdapter.CommonHolder holder, int position, int viewType, String data) {
        TextView tvTitle = get(holder, R.id.title);
        tvTitle.setText(data);
    }
}