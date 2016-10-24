/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-8 下午4:47
 * *********************************************************
 */

package com.fosung.usedemo.views;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup.LayoutParams;
import android.widget.TextView;

import com.fosung.usedemo.R;

import java.util.ArrayList;


/**
 * 菜单弹出框
 */
public class ZDlgMenu extends ZDlg {

    private LinearLayout                         llMenu;        //菜单容器布局
    private TextView                             tvTitle;        //标题控件
    private ZDialogParamSubmitInterface<Integer> submitInter;    // 点击确定按钮回调接口
    private Context                              context;

    public ZDlgMenu(Activity context) {
        super(context, R.layout.dlg_menu);
        this.context = context;
        initRes();
    }

    private void initRes() {
        llMenu = (LinearLayout) getView(R.id.dialogmenu_ll);
        tvTitle = (TextView) getView(R.id.dialogmenu_title);
    }

    public ZDlgMenu setTitle(String str) {
        if (!TextUtils.isEmpty(str)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(str);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        return this;
    }

    public ZDlgMenu setDatas(ArrayList<String> listData) {
        setDatas(listData.toArray(new String[listData.size()]));
        return this;
    }

    public ZDlgMenu setDatas(String[] arrStr) {
        int padding = (int) context.getResources()
                                   .getDimension(R.dimen.dimens_big);
        float textSize = context.getResources()
                                .getDimension(R.dimen.textsize_big);
        int color = context.getResources()
                           .getColor(R.color.gray_dark);
        LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        llMenu.removeAllViews();
        for (int i = 0, attrStrLength = arrStr.length; i < attrStrLength; i++) {
            final int index = i;
            String anAttrStr = arrStr[i];
            TextView tv = new AppCompatTextView(context);
            tv.setText(anAttrStr);
            tv.setBackgroundResource(R.drawable.common_listitem_sel);
            tv.setTextColor(color);
            tv.setPadding(padding, padding, padding, padding);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (submitInter != null) {
                        submitInter.submit(index);
                        cancel();
                    }
                }
            });
            llMenu.addView(tv, layout);
        }
        return this;
    }

    /**
     * 添加点击回调接口
     */
    public ZDlgMenu addSubmitListener(ZDialogParamSubmitInterface<Integer> submitInter) {
        this.submitInter = submitInter;
        return this;
    }


}
