/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-18 下午5:24
 **********************************************************/

package com.fosung.usedemo.demo.demo_mvp.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fosung.frame.app.BaseFrameLazyLoadFrag;
import com.fosung.frame.imageloader.ImageLoaderUtils;
import com.fosung.frame.utils.ActivityUtil;
import com.fosung.frame.utils.ToastUtil;
import com.fosung.usedemo.R;
import com.fosung.usedemo.demo.demo_mvp.activity.DemoMvpActivity;
import com.fosung.usedemo.demo.demo_mvp.contract.DemoImageUtilContract;
import com.fosung.usedemo.demo.demo_mvp.presenter.DemoImageUtilPresenter;
import com.fosung.usedemo.views.ZBanner;
import com.fosung.usedemo.views.ZTextSwitcher;

import java.util.ArrayList;


/**
 * @author wanglin
 */
public class Demo_ImageUtil_Fragment extends BaseFrameLazyLoadFrag implements DemoImageUtilContract.View {
    private ImageView                       iv;
    private ZTextSwitcher                   textSwitcher;
    private ZBanner                         banner;
    private DemoImageUtilContract.Presenter presenter;

    public static Demo_ImageUtil_Fragment newInstance() {
        Bundle args = new Bundle();
        Demo_ImageUtil_Fragment fragment = new Demo_ImageUtil_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void lazyLoad() {
        iv = getView(R.id.iv_content);
        textSwitcher = getView(R.id.view_textswitcher);
        banner = getView(R.id.view_banner);
        Button btn = getView(R.id.btn_mvp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startActivity(mActivity, DemoMvpActivity.class);
            }
        });

        presenter = new DemoImageUtilPresenter(this);
        presenter.init();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!textSwitcher.isStart() && textSwitcher.isInit()) {
            textSwitcher.startSwitcher();
        }

        if (!banner.isStart() && banner.isInit()) {
            banner.startAutoPlay();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        textSwitcher.stopSwitcher();
        banner.stopAutoPlay();
    }


    @Override
    protected int getRootViewLayId() {
        return R.layout.a_demo_fragment_imageutil;
    }

    @Override
    public void displayImage(String url) {
        ImageLoaderUtils.displayImage(this, url, iv);
    }

    @Override
    public void startTextSwitcher(String text) {
        textSwitcher.setTextColor(Color.BLACK)
                    .setSwitcherTextSize(20)
                    .setSwitchInterval(4000)
                    .setSwitcherText(text)
                    .setOutAnima(mActivity, R.anim.textswitcher_slide_out)
                    .setInAnima(mActivity, R.anim.textswitcher_slide_in)
                    .startSwitcher();
        
    }

    @Override
    public void startBanner(final ArrayList<String> listUrl) {
        banner.setBannerStyle(ZBanner.CIRCLE_INDICATOR_TITLE)
              .setIndicatorGravity(ZBanner.CENTER)
              .setBannerTitle(listUrl)
              .setDelayTime(4000)
              .setOnBannerClickListener(new ZBanner.OnBannerClickListener() {
                  @Override
                  public void OnBannerClick(View view, int position) {
                      ToastUtil.toastShort("点击了第" + (position + 1) + "张图片");
                  }
              })
              .setImages(listUrl)
              .startAutoPlay();
    }
}
