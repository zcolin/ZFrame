/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-2-24 上午11:23
 * ********************************************************
 */

package com.zcolin.frame.demo;

import com.zcolin.frame.app.BaseFrameActivity;


/**
 * DBDemo
 */
public class BaseActivity extends BaseFrameActivity{

    
    @Override
    protected boolean isImmerse() {
        //不使用沉浸式状态栏
        return false;
    }
}
