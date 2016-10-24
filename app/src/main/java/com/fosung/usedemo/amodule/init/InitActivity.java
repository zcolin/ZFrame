/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午9:54
 **********************************************************/

package com.fosung.usedemo.amodule.init;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fosung.frame.utils.ActivityUtil;
import com.fosung.usedemo.R;
import com.fosung.usedemo.demo.demo_mvp.activity.MainActivity;


public class InitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        ActivityUtil.startActivity(this, MainActivity.class);
        this.finish();
    }
}
