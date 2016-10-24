/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-19 上午10:29
 **********************************************************/
package com.fosung.usedemo.demo.demo_mvp.contract;

/**
 * MVP协约接口，定义Presenter的和View接口
 */
public interface DemoWebViewContract {
    public interface View {


        /**
         * 调用Js函数
         */
        void callJsFunc(String funcName, String strParam);
    }

    public interface Presenter {
        /**
         * 从xx途径获取User对象
         */
        void getUserDataFrom_xx();
    }
}
