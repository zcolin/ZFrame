/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-11 下午4:01
 * *********************************************************
 */

package com.fosung.usedemo.demo.demo_mvp.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fosung.frame.app.BaseFrameActivity;
import com.fosung.frame.utils.ToastUtil;
import com.fosung.usedemo.R;
import com.fosung.usedemo.demo.demo_mvp.fragment.Demo_SuperRecycler_Fragment;
import com.fosung.usedemo.views.ZViewPager;


/**
 *
 */
public class DemoDesignSupportActivity1 extends BaseFrameActivity {

    private Toolbar              toolbar;
    private TabLayout            tabLayout;
    private ZViewPager           viewPager;
    private FloatingActionButton fabTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_demo_activity_designsupport1);

        init();
    }

    @Override
    protected boolean isImmerse() {
        return false;
    }

    private void init() {
        toolbar = getView(R.id.toolbar);
        tabLayout = getView(R.id.tabs);
        viewPager = getView(R.id.viewpager);
        fabTest = getView(R.id.fab);

        toolbar.setTitle("DesignSupport");
        setSupportActionBar(toolbar);

        //设置TabLayout的模式  
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return Demo_SuperRecycler_Fragment.newInstance();
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return "Label-1";
                } else {
                    return "Label-2";
                }
            }
        };
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        fabTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "FloatActionBar-click", Snackbar.LENGTH_LONG)
                        .setAction("toast", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.toastShort("button-click");
                            }
                        });
            }
        });
    }
}
