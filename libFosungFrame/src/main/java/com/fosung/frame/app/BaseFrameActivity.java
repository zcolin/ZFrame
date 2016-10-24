/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.fosung.frame.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.fosung.frame.permission.PermissionsManager;
import com.fosung.frame.utils.ActivityUtil;


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
    private   ResultActivityHelper resultActivityHelper;
    protected AppCompatActivity    mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        ActivityUtil.addActivityToList(this);

        if (isImmerse()) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivity = null;
        ActivityUtil.removeActivityFromList(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public <T> T getView(int resId) {
        return (T) findViewById(resId);
    }

    /**
     * 是否使用沉浸式状态栏
     */
    protected boolean isImmerse() {
        return VERSION.SDK_INT >= VERSION_CODES.KITKAT;
    }

    /**
     * 因为核心库中需要使用ProgressDialog, 如果应用模块自定义ProgressDialog， 则需要在应用模块重写次函数返回自定义的ProgressDialog
     */
    public ProgressDialog getProgressDialog() {
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance()
                          .notifyPermissionsChange(permissions, grantResults);
    }
}
