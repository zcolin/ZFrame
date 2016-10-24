/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-18 下午1:49
 **********************************************************/
package com.fosung.usedemo.demo.demo_mvp.contract;

import com.fosung.frame.app.BaseFrameActivity;

/**
 * MVP协约接口，定义Presenter的和View接口
 */
public interface DemoMvpContract {
    public interface View {
        /**
         * TextView展示信息
         */
        void setTvText(CharSequence charSequence);
    }

    public interface Presenter {
        /**
         * 百度获取网络数据
         */
        void getBaiduStringData(BaseFrameActivity context);

        /**
         * 数据库保存Student信息
         */
        void saveStudent();

        /**
         * 数据库查询Student信息
         */
        void queryStudent();

        /**
         * 网络获取User信息
         */
        void getUser(BaseFrameActivity context);
    }
}
