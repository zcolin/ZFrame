/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-19 上午10:16
 **********************************************************/
package com.fosung.usedemo.demo.demo_mvp.presenter;

import com.fosung.frame.utils.GsonUtil;
import com.fosung.usedemo.demo.demo_mvp.contract.DemoWebViewContract;

/**
 *
 */
public class DemoWebViewPresenter implements DemoWebViewContract.Presenter {

    private DemoWebViewContract.View view;

    public DemoWebViewPresenter(DemoWebViewContract.View view) {
        this.view = view;
    }

    @Override
    public void getUserDataFrom_xx() {
        User user = new User();
        Location location = new Location();
        location.address = "SDU";
        user.location = location;
        user.name = "大头鬼";

        view.callJsFunc("functionInJs", GsonUtil.beanToString(user));
    }


    static class Location {
        String address;
    }

    static class User {
        String   name;
        Location location;
        String   testStr;
    }
}
