/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-19 上午9:37
 **********************************************************/
package com.fosung.usedemo.demo.demo_mvp.contract;

import java.util.ArrayList;

/**
 * MVP协约接口，定义Presenter的和View接口
 */
public interface DemoImageUtilContract {
    public interface View {
        /**
         * 显示图片
         */
        void displayImage(String url);

        void startTextSwitcher(String text);

        void startBanner(ArrayList<String> listUrl);
    }

    public interface Presenter {
        /**
         * 初始化数据
         */
        void init();
    }
}
