package com.fosung.usedemo.views.listview;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseAdapter的封装，需要实现抽象函数
 */

public abstract class CommonBaseAdapter<T> extends android.widget.BaseAdapter {
    private   List<T> listData;
    protected Context context;

    public CommonBaseAdapter(Context context) {
        init(context, new ArrayList<T>());
    }

    public CommonBaseAdapter(Context context, List<T> list) {
        init(context, list);
    }

    private void init(Context context, List<T> list) {
        this.listData = list;
        this.context = context;
    }

    public List<T> getListData() {
        return listData;
    }

    /**
     * 替换List数据，将内置的list替换为传入的list
     * @param list
     */
    public void setListData(List<T> list) {
        this.listData = list;
    }

    public void clear() {
        this.listData.clear();
        notifyDataSetChanged();
    }

    /**
     * 追加条目数据
     * @param datas
     */
    public void addDatas(List<T> datas) {
        if (listData != null) {
            this.listData.addAll(datas);
            notifyDataSetChanged();
        }
    }

    /**
     * 将数据替换为传入的数据集
     * @param datas
     */
    public void setDatas(List<T> datas) {
        listData.clear();
        if (datas != null) {
            listData.addAll(datas);
        }
        notifyDataSetChanged();
    }

    /**
     * 追加单条数据
     * @param data
     */
    public void addData(T data) {
        if (listData != null) {
            this.listData.add(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return listData == null ? 0 : listData.size();
    }

    @Override
    public T getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(context)
                                        .inflate(getItemLayoutId(), null);
        }

        setUpData(parent, convertView, position, getItemViewType(position), listData.get(position));
        return convertView;
    }

    /**
     * 获取布局ID
     *
     * @return 布局Id,  ex:R.layout.listitem_***
     */
    public abstract int getItemLayoutId();

    /**
     * 设置显示数据,替代getView，在此函数中进行赋值操作
     * <p>
     * ex:
     * TextView tvNumb = get(view, R.id.tv);
     * tvNumb.setText(String.valueOf(position + 1));
     */
    public abstract void setUpData(ViewGroup parent, View convertView, int position, int viewType, T data);

    /**
     * @param view converView
     * @param id   控件的id
     * @return 返回<E extends View>
     */
    @SuppressWarnings("unchecked")
    protected <E extends View> E get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (null == viewHolder) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (null == childView) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (E) childView;
    }
}