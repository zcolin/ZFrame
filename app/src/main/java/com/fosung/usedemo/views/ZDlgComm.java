/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-20 上午10:48
 * *********************************************************
 */

package com.fosung.usedemo.views;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.fosung.usedemo.R;


/**
 * 普通对话框，有确定和取消按钮
 */
public class ZDlgComm extends ZDlg implements OnClickListener {

    protected ZDialogSubmitInterface submitInterface;    // 点击确定按钮回调接口
    protected ZDialogCancelInterface cancelInterface;    // 点击取消按钮回调接口
    private TextView tvTitle;            // 标题文字
    private TextView tvOK;        // 确定按钮
    private TextView tvCancel;            // 取消按钮
    private TextView tvMessage;            // 消息内容

    /**
     * @param context
     */
    public ZDlgComm(Context context) {
        super(context, R.layout.dlg_common);
        initRes();
    }

    /**
     * 设置标题
     */
    public ZDlgComm setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    /**
     * 设置内容
     */
    public ZDlgComm setMessage(String msg) {
        tvMessage.setText(msg);
        return this;
    }

    /**
     * 设置按钮文字
     */
    public ZDlgComm setOKBtnText(String text) {
        tvOK.setText(text);
        return this;
    }

    /**
     * 设置按钮文字
     */
    public ZDlgComm setCancelBtnText(String text) {
        tvCancel.setText(text);
        return this;
    }

    /**
     * 添加确定回调接口
     */
    public ZDlgComm addSubmitListener(ZDialogSubmitInterface submitInterface) {
        this.submitInterface = submitInterface;
        return this;
    }


    /**
     * 添加取消回调接口
     */
    public ZDlgComm addCancelListener(ZDialogCancelInterface cancelInterface) {
        this.cancelInterface = cancelInterface;
        return this;
    }

    /**
     * 设置提示内容对齐方式
     */
    public ZDlgComm setMessageGravity(int gravity) {
        tvMessage.setGravity(gravity);
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
        if (tvCancel.getText()
                    .length() == 0) {
            tvCancel.setText("取消");
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
            if (submitInterface != null) {
                boolean flag = submitInterface.submit();
                if (flag) {
                    cancel();
                }
            }
        } else if (v == tvCancel) {
            if (cancelInterface != null) {
                boolean flag = cancelInterface.cancel();
                if (flag) {
                    cancel();
                }
            } else {
                cancel();
            }
        }
    }


    private void initRes() {
        tvOK = (TextView) getView(R.id.dialog_okbutton);
        tvCancel = (TextView) getView(R.id.dialog_cancelbutton);
        tvMessage = (TextView) getView(R.id.dialog_message);
        tvTitle = (TextView) getView(R.id.dialog_tilte);
        tvMessage.setMovementMethod(new ScrollingMovementMethod());

        tvOK.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

}
