package com.fosung.usedemo.demo.demo_mvp.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fosung.frame.app.ResultActivityHelper;
import com.fosung.frame.permission.PermissionHelper;
import com.fosung.frame.permission.PermissionsResultAction;
import com.fosung.frame.utils.ToastUtil;
import com.fosung.usedemo.R;
import com.fosung.usedemo.amodule.base.BaseSecondLevelActivity;

import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelector;


public class DemoImageSelectorActivity extends BaseSecondLevelActivity {

    private TextView   mResultText;
    private RadioGroup mChoiceMode, mShowCamera;
    private EditText mRequestNum;

    private ArrayList<String> mSelectPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_demo_activity_imageselector);

        mResultText = (TextView) findViewById(R.id.result);
        mChoiceMode = (RadioGroup) findViewById(R.id.choice_mode);
        mShowCamera = (RadioGroup) findViewById(R.id.show_camera);
        mRequestNum = (EditText) findViewById(R.id.request_num);

        mChoiceMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.multi) {
                    mRequestNum.setEnabled(true);
                } else {
                    mRequestNum.setEnabled(false);
                    mRequestNum.setText("");
                }
            }
        });

        View button = findViewById(R.id.button);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PermissionHelper.requestPermission(mActivity, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionsResultAction() {
                        @Override
                        public void onGranted() {
                            pickImage();
                        }

                        @Override
                        public void onDenied(String permission) {
                            ToastUtil.toastShort("请授予本程序拍照和读取文件权限!");
                        }
                    });
                }
            });
        }

    }

    private void pickImage() {
        boolean showCamera = mShowCamera.getCheckedRadioButtonId() == R.id.show;
        int maxNum = 9;

        if (!TextUtils.isEmpty(mRequestNum.getText())) {
            try {
                maxNum = Integer.valueOf(mRequestNum.getText()
                                                    .toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        Intent intent = null;
        if (mChoiceMode.getCheckedRadioButtonId() == R.id.single) {
            intent = MultiImageSelector.create()
                                       .showCamera(showCamera)
                                       .single()
                                       .origin(mSelectPath)
                                       .createIntent(mActivity);
        } else {
            intent = MultiImageSelector.create()
                                       .showCamera(showCamera)
                                       .count(maxNum)
                                       .multi()
                                       .origin(mSelectPath)
                                       .createIntent(mActivity);
        }

        startActivityWithCallback(intent, new ResultActivityHelper.ResultActivityListener() {
            @Override
            public void onResult(int requestCode, int resultCode, Intent data) {
                if (resultCode == RESULT_OK) {
                    mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    StringBuilder sb = new StringBuilder();
                    for (String p : mSelectPath) {
                        sb.append(p);
                        sb.append("\n");
                    }
                    mResultText.setText(sb.toString());
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meun_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
