/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-2-24 上午10:53
 * ********************************************************
 */

package com.zcolin.frame.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * DBDemo
 */
public class PermissionAndActivityResultActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_activityresult);

        final EditText editText = getView(R.id.edittext);
        Button btn = getView(R.id.button);
        if (mBundle != null) {
            editText.setText(mBundle.getString("data"));
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.setResult(RESULT_OK, new Intent().putExtra("data", editText.getText()
                                                                                     .toString()));
                mActivity.finish();
            }
        });
    }
}
