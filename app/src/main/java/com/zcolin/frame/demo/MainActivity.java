/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */

package com.zcolin.frame.demo;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zcolin.frame.app.FramePathConst;
import com.zcolin.frame.imageloader.ImageLoaderUtils;
import com.zcolin.frame.permission.PermissionHelper;
import com.zcolin.frame.permission.PermissionsResultAction;
import com.zcolin.frame.util.ActivityUtil;
import com.zcolin.frame.util.SystemIntentUtil;
import com.zcolin.frame.util.ToastUtil;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llContent;
    private ImageView    imageView;
    private ArrayList<Button> listButton = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        llContent = getView(R.id.ll_content);
        imageView = getView(R.id.imageview);
        listButton.add(addButton("Http演示"));
        listButton.add(addButton("Db演示"));
        listButton.add(addButton("权限处理和回传数据演示"));
        listButton.add(addButton("拍照和裁剪演示"));
        listButton.add(addButton("图片选取和裁剪演示"));

        for (Button btn : listButton) {
            btn.setOnClickListener(this);
        }
    }

    private Button addButton(String text) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button button = new Button(mActivity);
        button.setText(text);
        button.setGravity(Gravity.CENTER);
        button.setAllCaps(false);
        llContent.addView(button, params);
        return button;
    }

    @Override
    public void onClick(View v) {
        if (v == listButton.get(0)) {
            ActivityUtil.startActivity(this, HttpDemoActivity.class);
        } else if (v == listButton.get(1)) {
            ActivityUtil.startActivity(this, DbDemoActivity.class);
        } else if (v == listButton.get(2)) {
            PermissionHelper.requestPermission(mActivity, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA}, new 
                    PermissionsResultAction() {
                @Override
                public void onGranted() {
                    Intent intent = new Intent(mActivity, PermissionAndActivityResultActivity.class);
                    intent.putExtra("data", "传入数据-xxx");
                    startActivityWithCallback(intent, (resultCode, data) -> {
                        if (resultCode == RESULT_OK) {
                            if (data != null) {
                                ToastUtil.toastShort(data.getStringExtra("data"));
                            }
                        }
                    });
                }

                @Override
                public void onDenied(String permission) {
                    ToastUtil.toastShort("请赋予本程序拨打电话和摄像头权限!");
                }
            });
        } else if (v == listButton.get(3)) {
            final String savePath = FramePathConst.getInstance().getTempFilePath("jpg");
            SystemIntentUtil.takePhotoWithCrop(mActivity, savePath, 600, 600, new SystemIntentUtil.OnCompleteLisenter() {
                @Override
                public void onSelected(Uri fileProviderUri) {
                    ImageLoaderUtils.displayImage(mActivity, savePath, imageView);
                    System.out.println(fileProviderUri.getPath());
                }

                @Override
                public void onCancel() {

                }
            });
        } else if (v == listButton.get(4)) {
            final String savePath = FramePathConst.getInstance().getTempFilePath("jpg");
            SystemIntentUtil.selectPhotoWithCrop(mActivity, savePath, 600, 600, new SystemIntentUtil.OnCompleteLisenter() {
                @Override
                public void onSelected(Uri fileProviderUri) {
                    ImageLoaderUtils.displayImage(mActivity, fileProviderUri, imageView);
                    System.out.println(fileProviderUri.getPath());
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }
}
