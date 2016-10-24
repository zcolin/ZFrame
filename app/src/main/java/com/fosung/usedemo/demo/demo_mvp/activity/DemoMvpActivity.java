/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-18 下午1:47
 **********************************************************/

package com.fosung.usedemo.demo.demo_mvp.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fosung.frame.permission.PermissionHelper;
import com.fosung.frame.permission.PermissionsResultAction;
import com.fosung.frame.utils.ActivityUtil;
import com.fosung.frame.utils.ToastUtil;
import com.fosung.usedemo.R;
import com.fosung.usedemo.amodule.base.BaseToolBarActivity;
import com.fosung.usedemo.demo.demo_mvp.activity.videorecord.DemoRecordVideoActivity;
import com.fosung.usedemo.demo.demo_mvp.contract.DemoMvpContract;
import com.fosung.usedemo.demo.demo_mvp.presenter.DemoMvpPresenter;


/**
 * Mvp Demo页面， DBDemo页面， HttpDemo页面
 * 此处为View层，处理简单的页面逻辑和view显示, 可以对应一个或多个presenter（尽量只有一个）
 * <p/>
 * MVP具体分工
 * View             {@link DemoMvpActivity}
 * Presenter        {@link DemoMvpPresenter}
 * Model            {@link com.fosung.usedemo.demo.demo_mvp.model.UserModel}
 * {@link com.fosung.usedemo.demo.demo_mvp.model.StudentModel}
 * {@link com.fosung.usedemo.demo.demo_mvp.model.BaiduStringModel}
 */
public class DemoMvpActivity extends BaseToolBarActivity implements View.OnClickListener, DemoMvpContract.View {
    private DemoMvpPresenter presenter;

    private TextView     tvText;
    private LinearLayout llContent;
    private Button[] arrayButton = new Button[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_demo_activity_mvp);
        setToolbarTitle(getClass().getSimpleName());

        presenter = new DemoMvpPresenter(this);
        init();
    }

    private void init() {
        tvText = getView(R.id.textview);
        tvText.setMovementMethod(new ScrollingMovementMethod());
        llContent = getView(R.id.ll_content);
        arrayButton[0] = addButton("Http获取baidu首页数据");
        arrayButton[1] = addButton("插入数据库数据");
        arrayButton[2] = addButton("读取数据库数据");
        arrayButton[3] = addButton("网络获取User对象");
        arrayButton[4] = addButton("回传数据的ToolBarActivity");
        arrayButton[5] = addButton("DemoDesignSupportActivity");
        arrayButton[6] = addButton("DemoDesignSupportActivity1");
        arrayButton[7] = addButton("视频录制Demo");
        arrayButton[8] = addButton("扫码");
        arrayButton[9] = addButton("图片选择");

        for (Button btn : arrayButton) {
            btn.setOnClickListener(this);
        }
    }

    private Button addButton(String text) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button button = new Button(this);
        button.setText(text);
        button.setGravity(Gravity.CENTER);
        button.setAllCaps(false);
        llContent.addView(button, params);
        return button;
    }

    @Override
    public void setTvText(CharSequence charSequence) {
        tvText.setText(charSequence);
    }


    @Override
    public void onClick(View v) {
        if (v == arrayButton[0]) {
            presenter.getBaiduStringData(this);
        } else if (v == arrayButton[1]) {
            presenter.saveStudent();
        } else if (v == arrayButton[2]) {
            presenter.queryStudent();
        } else if (v == arrayButton[3]) {
            presenter.getUser(this);
        } else if (v == arrayButton[4]) {
            ActivityUtil.startActivity(this, DemoToolBarActivity.class);
        } else if (v == arrayButton[5]) {
            ActivityUtil.startActivity(this, DemoDesignSupportActivity.class);
        } else if (v == arrayButton[6]) {
            ActivityUtil.startActivity(this, DemoDesignSupportActivity1.class);
        } else if (v == arrayButton[7]) {
            PermissionHelper.requestPermission(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, new PermissionsResultAction() {
                @Override
                public void onGranted() {
                    ActivityUtil.startActivity(mActivity, DemoRecordVideoActivity.class);
                }

                @Override
                public void onDenied(String permission) {
                    ToastUtil.toastShort("请授予本程序拍照和录音权限!");
                }
            });
        }else if (v == arrayButton[8]) {
            PermissionHelper.requestPermission(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.VIBRATE}, new PermissionsResultAction() {
                @Override
                public void onGranted() {
                    ActivityUtil.startActivity(mActivity, DemoQrCodeScanActivity.class);
                }

                @Override
                public void onDenied(String permission) {
                    ToastUtil.toastShort("请授予本程序拍照和震动权限!");
                }
            });
        }else if (v == arrayButton[9]) {
            ActivityUtil.startActivity(mActivity, DemoImageSelectorActivity.class);
        }
    }
}
