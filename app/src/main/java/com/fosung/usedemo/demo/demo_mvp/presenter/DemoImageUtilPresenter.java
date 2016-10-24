/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-19 上午9:36
 **********************************************************/
package com.fosung.usedemo.demo.demo_mvp.presenter;

import com.fosung.usedemo.demo.demo_mvp.contract.DemoImageUtilContract;

import java.util.ArrayList;

/**
 *
 */
public class DemoImageUtilPresenter implements DemoImageUtilContract.Presenter {

    private DemoImageUtilContract.View view;

    public DemoImageUtilPresenter(DemoImageUtilContract.View view) {
        this.view = view;
    }

    @Override
    public void init() {
        //TODO 从服务器或其他途径获取URL，此处写死
        view.displayImage(getImageUrl());

        //TODO 从服务器或其他途径获取文字内容，此处写死
        view.startTextSwitcher(getSwiterText());

        view.startBanner(getListUrl());
    }

    private String getImageUrl() {
        return "http://img1.imgtn.bdimg.com/it/u=1480985633,1206349730&fm=21&gp=0.jpg";
    }

    private String getSwiterText() {
        return "只要用过mvp这个问题可能很多人都知道。写mvp的时候，presenter会持有view，如果presenter有后台异步的长时间的动作，" +
                "比如网络请求，这时如果返回退出了Activity，后台异步的动作不会立即停止，这里就会有内存泄漏的隐患，所以会在presenter中加入" +
                "一个销毁view的方法。现在就在之前的项目中做一下修改";
    }

    private ArrayList<String> getListUrl() {
        String url_1 = "http://img.ycwb.com/news/attachement/jpg/site2/20110226/90fba60155890ed3082500.jpg";
        String url_2 = "http://cdn.duitang.com/uploads/item/201112/04/20111204012148_wkT88.jpg";
        String url_3 = "http://img6.faloo.com/Picture/680x580/0/449/449476.jpg";
        ArrayList<String> listUrl = new ArrayList<>();
        listUrl.add(url_1);
        listUrl.add(url_2);
        listUrl.add(url_3);
        return listUrl;
    }


}
