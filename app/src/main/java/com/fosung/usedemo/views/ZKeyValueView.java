/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-20 下午4:34
 **********************************************************/
package com.fosung.usedemo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fosung.usedemo.R;


/**
 * 封装的左边为说明右边为值的控件
 */
public class ZKeyValueView extends RelativeLayout {

    private Context   context;
    private TextView  tvKey;
    private TextView  tvValue;
    private ImageView ivArrow;
    private ImageView ivImg;

    public ZKeyValueView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZKeyValueView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context)
                      .inflate(R.layout.view_keyvalue, this);
        tvKey = (TextView) findViewById(R.id.tv_key);
        tvValue = (TextView) findViewById(R.id.tv_value);
        ivArrow = (ImageView) findViewById(R.id.iv_arrow);
        ivImg = (ImageView) findViewById(R.id.iv_img);
        View bottomLine = findViewById(R.id.view_bottomline);

        TypedArray a = context.getTheme()
                              .obtainStyledAttributes(attrs, R.styleable.ZKeyValueView, defStyle, 0);
        String keyText = a.getString(R.styleable.ZKeyValueView_key_text);
        String valueText = a.getString(R.styleable.ZKeyValueView_value_text);
        String hintText = a.getString(R.styleable.ZKeyValueView_value_hint);
        String valueGravity = a.getString(R.styleable.ZKeyValueView_value_gravity);
        int keyImg = a.getResourceId(R.styleable.ZKeyValueView_key_img, 0);
        boolean isArrow = a.getBoolean(R.styleable.ZKeyValueView_is_arrow, true);
        boolean isBottomLine = a.getBoolean(R.styleable.ZKeyValueView_is_bottomline, true);
        int keyEms = a.getInteger(R.styleable.ZKeyValueView_key_ems, 0);
        float keyTextSize = a.getDimension(R.styleable.ZKeyValueView_key_text_size, 0);
        float valueTextSize = a.getDimension(R.styleable.ZKeyValueView_value_text_size, 0);
        int valueTextColor = a.getColor(R.styleable.ZKeyValueView_value_text_color, 0);
        int keyTextColor = a.getColor(R.styleable.ZKeyValueView_key_text_color, 0);
        a.recycle();

        tvKey.setText(keyText);
        if (!TextUtils.isEmpty(valueText)) {
            tvValue.setText(valueText);
        } else if (!TextUtils.isEmpty(hintText)) {
            tvValue.setHint(hintText);
        }

        if ("left".equals(valueGravity)) {
            tvValue.setGravity(Gravity.LEFT);
        } else {
            tvValue.setGravity(Gravity.RIGHT);
        }


        if (keyImg != 0) {
            ivImg.setImageResource(keyImg);
        } else {
            ivImg.setVisibility(View.GONE);
            ((LayoutParams) ivImg.getLayoutParams()).rightMargin = 0;
        }

        if (keyEms > 0) {
            tvKey.setEms(keyEms);
        }

        if (keyTextSize > 0) {
            tvKey.setTextSize(TypedValue.COMPLEX_UNIT_PX, keyTextSize);
        }

        if (valueTextSize > 0) {
            tvValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, valueTextSize);
        }

        if (valueTextColor != 0) {
            tvValue.setTextColor(valueTextColor);
        }

        if (keyTextColor != 0) {
            tvKey.setTextColor(keyTextColor);
        }

        bottomLine.setVisibility(isBottomLine ? View.VISIBLE : View.GONE);
        ivArrow.setVisibility(isArrow ? View.VISIBLE : View.INVISIBLE);
    }

    public void setKeyText(String key) {
        tvKey.setText(key);
    }

    public void setValueText(String valueText) {
        tvValue.setText(valueText);
    }

    public String getKeyText() {
        return tvKey.getText()
                    .toString();
    }

    public String getValueText() {
        return tvValue.getText()
                      .toString();
    }

    public TextView getTvKey() {
        return tvKey;
    }

    public TextView getTvValue() {
        return tvValue;
    }
}