/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */

package com.zcolin.frame.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zcolin.frame.permission.PermissionsManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * Fragment的基类，实现公共操作
 * <p>
 * 1 权限回调处理
 * 2 onActivityResult回调处理
 * 3 继承此类之后可使用getView（int resId）替代findViewById。
 * 4 子类的ActivityContext可使用mActivity
 */
public abstract class BaseFrameFrag extends Fragment {
    private final SparseArray<View>    mViews = new SparseArray<>();
    protected     Activity             mActivity;
    protected     View                 rootView;
    protected     boolean              mIsDetached;
    /** Fragment当前状态是否可见 */
    protected     boolean              isVisible;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    protected     boolean              mHasLoadedOnce;
    /** 是否已经准备好，防止在onCreateView之前调用 */
    protected     boolean              isPrepared;
    private       ResultActivityHelper resultActivityHelper;


    /**
     * 获取关联的LayoutId
     */
    protected abstract int getRootViewLayId();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mIsDetached = false;
    }

    @Override
    public void onDetach() {
        mIsDetached = true;
        super.onDetach();
        // mActivity = null; 防止fragment中引用activity导致空指针，这样可能会有内存泄漏，但是相比空指针要好得多
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        /*在ViewPager切换过程中会重新调用onCreateView，此时如果实例化过，需要移除，会自动再次添加*/
        if (rootView == null) {
            rootView = inflater.inflate(getRootViewLayId(), null);
            createView(savedInstanceState);
            isPrepared = true;
            onPreLoad(savedInstanceState);
        } else {
            if (rootView.getParent() != null) {
                ((ViewGroup) rootView.getParent()).removeView(rootView);
            }
        }

        return rootView;
    }

    /**
     * androidX中对于ViewPager的传参是BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT，不再执行setUserVisibleHint方法，需要结合onResume和onPause
     * 实现懒加载。
     */
    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
        onPreLoad(null);
    }

    /**
     * androidX中对于ViewPager的传参是BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT，不再执行setUserVisibleHint方法，需要结合onResume和onPause
     * 实现懒加载。
     */
    @Override
    public void onPause() {
        super.onPause();
        isVisible = false;
    }

    /**
     * 懒加载之前的判断
     */
    protected void onPreLoad(@Nullable Bundle savedInstanceState) {
        if (!isVisible || !isPrepared || mHasLoadedOnce) {
            return;
        }

        mHasLoadedOnce = true;
        lazyLoad(savedInstanceState);
    }

    /**
     * 用于Framgment的show/hide方式切换
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onShow();
        } else {
            onHidden();
        }
    }

    /**
     * fragment显示
     */
    public void onShow() {

    }


    /**
     * fragment隐藏
     */
    public void onHidden() {

    }

    /**
     * 初始化view及数据,第一次调用onCreateView时调用
     */
    protected void createView(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 延迟加载， 子类需要懒加载可以重写此方法
     * 第一次调用onCreateView，并且可见时调用
     * <p>
     * 注意 ： 第一次加载时onResume是在lazyLoad（）之前调用，如果需要在onResume中需要对控件进行操作，可以有两种选择:
     * 1 可以重写creatView函数进行控件初始化
     * 2 在onResume判断 控件是否为空
     */
    protected void lazyLoad(@Nullable Bundle savedInstanceState) {

    }

    public <T extends View> T getView(int resId) {
        T view = (T) mViews.get(resId);
        if (view == null) {
            view = rootView.findViewById(resId);
            mViews.put(resId, view);
        }
        return view;
    }

    /**
     * 启动带有回调的Activity
     */
    public void startActivityWithCallback(Intent intent, ResultActivityHelper.ResultActivityListener listener) {
        if (resultActivityHelper == null) {
            resultActivityHelper = new ResultActivityHelper(this);
        }
        resultActivityHelper.startActivityForResult(intent, listener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultActivityHelper != null) {
            resultActivityHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }
}
