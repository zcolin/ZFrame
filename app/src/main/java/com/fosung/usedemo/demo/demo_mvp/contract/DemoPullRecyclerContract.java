/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-11 下午4:04
 * *********************************************************
 */
package com.fosung.usedemo.demo.demo_mvp.contract;

import android.app.Activity;

import java.util.ArrayList;

/**
 * MVP协约接口，定义Presenter的和View接口
 */
public interface DemoPullRecyclerContract {
    public interface View {
        /**
         * 向RecyclerView添加数据
         */
        void addDataToRecyclerView(ArrayList<String> list, boolean isClear);

        /**
         * 设置下拉加载结束
         */
        void setPullLoadmoreComplete();
    }

    public interface Presenter {
        /**
         * 从哪个途径获取Data
         */
        void getDataFromShopList(Activity activity, int page);
    }
}
