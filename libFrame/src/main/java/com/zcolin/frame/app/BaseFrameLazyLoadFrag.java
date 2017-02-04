/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.zcolin.frame.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 懒加载的Fragment基类，和ViewPager配合使用时如果需要懒加载可继承此类，实现其抽象函数lazyLoad（）和父类继承的getRootViewLayId（）
 * lazyLoad（）函数中进行控件加载和数据加载。
 * <p/>
 * 注意 ： 第一次加载时onResume是在lazyLoad（）之前调用，如果需要在onResume中需要对控件进行操作，可以有两种选择:
 * 1 可以重写creatView函数进行控件初始化
 * 2 在onResume判断 控件是否为空
 *
 */
public abstract class BaseFrameLazyLoadFrag extends BaseFrameFrag {

    private boolean isVisible;//Fragment当前状态是否可见
    private boolean mHasLoadedOnce;//是否已被加载过一次，第二次就不再去请求数据了
    private boolean isPrepared;//是否已经准备好，防止在onCreateView之前调用

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 在OnCreatView之前执行
     * onAttach之前
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            isVisible = true;
            onPreLoad();
        } else {
            isVisible = false;
        }
    }

    /**
     * 懒加载之前的判断
     */
    private void onPreLoad() {
        if (!isVisible || !isPrepared || mHasLoadedOnce) {
            return;
        }

        mHasLoadedOnce = true;
        lazyLoad();
    }

    @Override
    protected void createView() {
        isPrepared = true;
        onPreLoad();
    }

    /**
     * 延迟加载， 子类必须重写此方法
     * <p>
     * 第一次调用onCreateView，并且可见时调用
     */
    protected abstract void lazyLoad();
}
