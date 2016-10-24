/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-19 下午3:45
 * *********************************************************
 */
package com.fosung.usedemo.demo.demo_mvp.adapter;


import android.widget.TextView;

import com.fosung.usedemo.R;
import com.fosung.usedemo.views.pullrecyclerview.BaseRecyclerAdapter;


/**
 * PullRecyclerAdapter示例
 * <p>
 * pullrecyclerView的Adapter
 */
public class DemoPullRecyclerMultiItemAdapter extends BaseRecyclerAdapter<String> {

    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;

    @Override
    public int getItemLayoutId(int type) {
        return R.layout.a_demo_recycler_view_item;
    }


    @Override
    public int getItemViewType(int position) {
        int type = super.getItemViewType(position);
        if (type == TYPE_NORMAL) {
            if (position % 2 == 0) {
                type = TYPE_1;
            } else {
                type = TYPE_2;
            }
        }
        return type;
    }

    @Override
    public void setUpData(CommonHolder holder, int position, int viewType, String data) {
        if (viewType == TYPE_1) {
            TextView tvTitle = getView(holder, R.id.title);
            tvTitle.setText(data + "TYPE_1");
        }else {
            TextView tvTitle = getView(holder, R.id.title);
            tvTitle.setText(data + "TYPE_2");
        }
    }
}