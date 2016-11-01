/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.fosung.frame.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fosung.frame.permission.PermissionsManager;


/**
 * Fragment的基类，实现公共操作
 * <p>
 * 1 权限回调处理
 * 2 onActivityResult回调处理
 * 3 继承此类之后可使用getView（int resId）替代findViewById。
 * 4 子类的ActivityContext可使用mActivity
 */
public abstract class BaseFrameFrag extends Fragment {
    protected Activity             mActivity;
    protected View                 rootView;
    private   ResultActivityHelper resultActivityHelper;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*在ViewPager切换过程中会重新调用onCreateView，此时如果实例化过，需要移除，会自动再次添加*/
        if (rootView == null) {
            rootView = inflater.inflate(getRootViewLayId(), null);
            createView();
        } else {
            if (rootView.getParent() != null) {
                ((ViewGroup) rootView.getParent()).removeView(rootView);
            }
        }

        return rootView;
    }

    /**
     * 获取关联的LayoutId
     */
    protected abstract int getRootViewLayId();

    /**
     * 初始化view及数据,第一次调用onCreateView时调用
     */
    protected abstract void createView();

    protected <T> T getView(int id) {
        if (rootView != null) {
            return (T) rootView.findViewById(id);
        }
        return null;
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance()
                          .notifyPermissionsChange(permissions, grantResults);
    }
}
