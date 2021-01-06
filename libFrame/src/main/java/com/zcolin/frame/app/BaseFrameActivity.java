/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */

package com.zcolin.frame.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;

import com.zcolin.frame.permission.PermissionsManager;
import com.zcolin.frame.util.ActivityUtil;


/**
 * 所有Acitivity的基类，实现公共操作
 * <p/>
 * 1 沉浸式状态栏的封装
 * 2 Activity实例集合的加入和移除
 * 3 权限回调处理
 * 4 onActivityResult回调处理
 * 5 继承此类之后可使用getView（int resId）替代findViewById。
 */
public class BaseFrameActivity extends AppCompatActivity {
    private final SparseArray<View>    mViews = new SparseArray<>();
    private       ResultActivityHelper resultActivityHelper;
    protected     Activity             mActivity;
    protected     Bundle               mBundle; //Activity 销毁/恢复 时 保存/获取 数据
    private       boolean              isDestroyed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroyed = false;
        mActivity = this;
        ActivityUtil.addActivityToList(this);
        putExtra(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        isDestroyed = true;
        super.onDestroy();
        //mActivity = null; //防止引用activity导致空指针，这样可能会有内存泄漏，但是相比空指针要好得多
        ActivityUtil.removeActivityFromList(this);
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void putExtra(Bundle savedInstanceState) {
        mBundle = new Bundle();
        if (savedInstanceState != null) {
            mBundle.putAll(savedInstanceState);
        } else if (getIntent() != null && getIntent().getExtras() != null) {
            mBundle.putAll(getIntent().getExtras());
        }
    }

    public <T extends View> T getView(int resId) {
        T view = (T) mViews.get(resId);
        if (view == null) {
            view = findViewById(resId);
            mViews.put(resId, view);
        }
        return view;
    }

    /**
     * 因为核心库中需要使用ProgressDialog, 如果应用模块自定义ProgressDialog， 则需要在应用模块重写次函数返回自定义的ProgressDialog
     */
    public Dialog getProgressDialog() {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultActivityHelper != null) {
            resultActivityHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mBundle != null) {
            outState.putAll(mBundle);
        }
    }
}
