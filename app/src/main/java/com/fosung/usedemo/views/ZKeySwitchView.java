/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-20 下午4:34
 **********************************************************/
package com.fosung.usedemo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fosung.usedemo.R;


/**
 * 封装的左边为说明右边为值的控件
 */
public class ZKeySwitchView extends RelativeLayout {

    private Context      context;
    private SwitchButton switchButton;
    private ImageView    ivImg;

    public ZKeySwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZKeySwitchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context)
                      .inflate(R.layout.view_keyswitch, this);
        switchButton = (SwitchButton) findViewById(R.id.switchButton);
        ivImg = (ImageView) findViewById(R.id.iv_img);
        View bottomLine = findViewById(R.id.view_bottomline);

        TypedArray a = context.getTheme()
                              .obtainStyledAttributes(attrs, R.styleable.ZKeySwitchView, defStyle, 0);
        String keyText = a.getString(R.styleable.ZKeySwitchView_key_text);
        int keyImg = a.getResourceId(R.styleable.ZKeySwitchView_key_img, 0);
        boolean isBottomLine = a.getBoolean(R.styleable.ZKeySwitchView_is_bottomline, true);
        int keyEms = a.getInteger(R.styleable.ZKeySwitchView_key_ems, 0);
        float keyTextSize = a.getDimension(R.styleable.ZKeySwitchView_key_text_size, 0);
        int keyTextColor = a.getColor(R.styleable.ZKeySwitchView_key_text_color, 0);
        boolean isChecked = a.getBoolean(R.styleable.ZKeySwitchView_is_checked, false);
        a.recycle();

        switchButton.setText(keyText);
        switchButton.setChecked(isChecked);

        if (keyImg != 0) {
            ivImg.setImageResource(keyImg);
        } else {
            ivImg.setVisibility(View.GONE);
            ((LayoutParams) ivImg.getLayoutParams()).rightMargin = 0;
        }

        if (keyEms > 0) {
            switchButton.setEms(keyEms);
        }

        if (keyTextSize > 0) {
            switchButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, keyTextSize);
        }

        if (keyTextColor != 0) {
            switchButton.setTextColor(keyTextColor);
        }

        bottomLine.setVisibility(isBottomLine ? View.VISIBLE : View.GONE);
    }

    public void setKeyText(String key) {
        switchButton.setText(key);
    }

    public String getKeyText() {
        return switchButton.getText()
                           .toString();
    }

    public SwitchButton getSwitchButton() {
        return switchButton;
    }

    public boolean isChecked() {
        return switchButton.isChecked();
    }

    public void setChecked(boolean isChecked) {
        switchButton.setChecked(isChecked);
    }

    public void setOncheckedListener(CompoundButton.OnCheckedChangeListener listener) {
        switchButton.setOnCheckedChangeListener(listener);
    }

}