/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午9:54
 **********************************************************/

package com.fosung.usedemo.views;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.fosung.usedemo.R;


/**
 * 普遍对话框，有一个确定按钮
 */
public class ZAlert extends ZDlg implements OnClickListener {

    protected ZDialogSubmitInterface submitInterface;    // 点击确定按钮回调接口
    private   TextView               tvOK;            // 确定按钮
    private   TextView               tvMessage;           // 消息内容
    private   TextView               tvTitle;

    /**
     * @param context
     */
    public ZAlert(Context context) {
        super(context, R.layout.dlg_alert);
        initRes();
    }

    /**
     * 设置标题
     */
    public ZAlert setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    /**
     * 设置内容
     */
    public ZAlert setMessage(String msg) {
        tvMessage.setText(msg);
        return this;
    }

    /**
     * 设置按钮文字
     */
    public ZAlert setBtnText(String text) {
        tvOK.setText(text);
        return this;
    }

    /**
     * 添加确定回调接口
     */
    public ZAlert addSubmitListener(ZDialogSubmitInterface submitInterface) {
        this.submitInterface = submitInterface;
        return this;
    }

    /**
     * 设置提示内容对齐方式
     */
    public ZAlert setMessageGravity(int gravity) {
        tvMessage.setGravity(gravity);
        return this;
    }

    /**
     * 追加字符串，会自动回车
     *
     * @param strMsg 要追加的内容
     */
    public ZAlert append(String strMsg) {
        String str = tvMessage.getText()
                              .toString();
        if (str.length() > 0) {
            if (!str.contains(strMsg)) {
                tvMessage.append("\n" + strMsg);
            }
        } else {
            tvMessage.setText(strMsg);
        }
        return this;
    }

    /**
     * 刷新页面
     *
     * @param strMsg 提示内容
     */
    public void notifyDataChanged(String strMsg) {
        tvMessage.setText(strMsg);
    }


    @Override
    public void show() {
        if (tvOK.getText()
                .length() == 0) {
            tvOK.setText("确定");
        }
        if (tvTitle.getText()
                   .length() == 0) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
        }

        super.show();
    }

    @Override
    public void onClick(View v) {
        if (v == tvOK) {
            cancel();
            if (submitInterface != null) {
                submitInterface.submit();
            }
        }
    }


    private void initRes() {
        tvOK = (TextView) getView(R.id.dialog_okbutton);
        tvMessage = (TextView) getView(R.id.dialog_message);
        tvTitle = (TextView) getView(R.id.dialog_tilte);
        tvMessage.setMovementMethod(new ScrollingMovementMethod());

        tvOK.setOnClickListener(this);
    }
}
