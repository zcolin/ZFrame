/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-18 下午5:24
 **********************************************************/

package com.fosung.usedemo.demo.demo_mvp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fosung.usedemo.R;
import com.fosung.usedemo.amodule.base.BaseNoToolBarActivity;
import com.fosung.usedemo.amodule.base.BaseSecondLevelActivity;
import com.fosung.usedemo.amodule.base.BaseToolBarActivity;
import com.fosung.usedemo.views.ZEditTextWithClear;


/**
 * 需要带ToolBar的继承 {@link BaseToolBarActivity}
 * 需要ToolBar带返回按钮的继承 {@link BaseSecondLevelActivity}
 * 需要没有ToolBar的继承 {@link BaseNoToolBarActivity}
 */
public class DemoSecondLevelActivity extends BaseSecondLevelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_demo_activity_toolbar);

        setToolbarTitle(getClass().getSimpleName());

        final ZEditTextWithClear et = getView(R.id.edittext);
        et.setHint("输入需要回传的数据");

        Button btn = getView(R.id.button);
        btn.setText("finishActivity");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DemoSecondLevelActivity.this.setResult(RESULT_OK, new Intent().putExtra("data", et.getText().toString()));
                DemoSecondLevelActivity.this.finish();
            }
        });
    }
}
