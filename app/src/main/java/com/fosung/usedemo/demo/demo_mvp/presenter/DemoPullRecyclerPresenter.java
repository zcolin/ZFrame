/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-11 下午4:03
 * *********************************************************
 */
package com.fosung.usedemo.demo.demo_mvp.presenter;

import android.app.Activity;
import android.os.Handler;

import com.fosung.usedemo.demo.demo_mvp.contract.DemoPullRecyclerContract;

import java.util.ArrayList;

/**
 *
 */
public class DemoPullRecyclerPresenter implements DemoPullRecyclerContract.Presenter {

    private DemoPullRecyclerContract.View view;

    public DemoPullRecyclerPresenter(DemoPullRecyclerContract.View view) {
        this.view = view;
    }

    @Override
    public void getDataFromShopList(final Activity activity, final int page) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.addDataToRecyclerView(setList(page), page == 1);

                        view.setPullLoadmoreComplete();
                    }
                });
            }
        }, 1000);
    }

    //制造假数据
    private ArrayList<String> setList(int page) {
        ArrayList<String> dataList = new ArrayList<>();
        int start = 20 * (page - 1);
        for (int i = start; i < 20 * page; i++) {
            dataList.add("Frist" + i);
        }
        return dataList;
    }
}
