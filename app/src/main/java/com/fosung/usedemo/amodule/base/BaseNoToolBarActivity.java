/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-18 下午1:41
 **********************************************************/

package com.fosung.usedemo.amodule.base;

import android.os.Bundle;
import android.view.ViewGroup;

import com.fosung.frame.app.BaseFrameActivity;
import com.fosung.frame.utils.ScreenUtil;


/**
 * 没有ToolBar的Activity, 不需要Toolbar可以继承此类
 */
public class BaseNoToolBarActivity extends BaseFrameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        if (isImmerse()) {
            setImmersePaddingTop();
        }
    }


    /**
     * 设定子View距离顶部的距离，空出状态栏的距离
     */
    protected void setImmersePaddingTop() {
        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        viewGroup.setPadding(0, ScreenUtil.getStatusBarHeight(this), 0, 0);
    }
}
