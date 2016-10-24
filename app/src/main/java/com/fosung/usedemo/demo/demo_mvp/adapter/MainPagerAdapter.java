/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-19 上午9:15
 **********************************************************/

/*    
 * 
 * @author		: WangLin  
 * @Company: 	：FCBN
 * @date		: 2015年5月13日 
 * @version 	: V1.0
 */
package com.fosung.usedemo.demo.demo_mvp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fosung.usedemo.demo.demo_mvp.activity.MainActivity;


/**
 * 主页面 ViewPager适配器
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private MainActivity mainActivity;

    public MainPagerAdapter(MainActivity mainPresenter, FragmentManager fm) {
        super(fm);
        this.mainActivity = mainPresenter;
    }

    @Override
    public int getCount() {
        return MainActivity.TAB_POSITION.length;
    }

    @Override
    public Fragment getItem(int arg0) {
        return mainActivity.getFragByPosition(arg0);
    }
}
