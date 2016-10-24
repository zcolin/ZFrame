/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-20 上午10:48
 * *********************************************************
 */
package com.fosung.usedemo.views;

import android.app.Activity;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.fosung.usedemo.R;


/**
 * 带有一个输入框的对话框 ，有两个按钮
 */
public class ZDlgEdit extends ZDlg implements OnClickListener {
    private TextView tvMakeSure;        // 确定按钮
    private TextView tvCancel;            // 取消按钮
    private TextView tvTitle;            // 消息内容
    private EditText etEdit;            // 编辑框

    private ZDialogParamSubmitInterface<String> submitInterface;
    private ZDialogCancelInterface              cancelInterface;

    public ZDlgEdit(Activity context) {
        super(context, R.layout.dlg_edit);
        initRes();
    }

    private void initRes() {
        tvMakeSure = (TextView) getView(R.id.dialog_okbutton);
        tvCancel = (TextView) getView(R.id.dialog_cancelbutton);
        tvTitle = (TextView) getView(R.id.dlgeditone_title);
        etEdit = (EditText) getView(R.id.dlgeditone_edit);
        tvMakeSure.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    public ZDlgEdit setTitle(String strTitle) {
        tvTitle.setText(strTitle);
        return this;
    }

    public ZDlgEdit setEditText(String str) {
        etEdit.setText(str);
        etEdit.selectAll();
        return this;
    }

    public ZDlgEdit setOkBtnText(String str) {
        tvMakeSure.setText(str == null ? "确定" : str);
        return this;
    }

    public ZDlgEdit setCancelBtnText(String str) {
        tvCancel.setText(str == null ? "取消" : str);
        return this;
    }

    public ZDlgEdit setHint(String str) {
        etEdit.setHint(str);
        return this;
    }

    /**
     * 设置说明文字
     */
    public ZDlgEdit setInstruction(CharSequence str) {
        TextView tv = (TextView) getView(R.id.dlgeditone_instruction);
        if (str != null) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(str);
        } else {
            tv.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置输入框最大输入长度
     */
    public ZDlgEdit setMaxLength(int length) {
        etEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
        return this;
    }

    /**
     * 设置输入框限制的输入类型
     *
     * @param type @see InputType
     */
    public ZDlgEdit setInputType(int type) {
        etEdit.setInputType(type);
        return this;
    }

    /**
     * 输入框选择全部
     */
    public ZDlgEdit selectAll() {
        etEdit.selectAll();
        return this;
    }

    public ZDlgEdit setIsCancelable(boolean isCancelable){
        super.setIsCancelAble(isCancelable);
        return this;
    }

    /**
     * 添加确定回调接口
     */
    public ZDlgEdit addSubmitStrListener(ZDialogParamSubmitInterface<String> submitInterface) {
        this.submitInterface = submitInterface;
        return this;
    }

    /**
     * 添加取消回调接口
     */
    public ZDlgEdit addCancelListener(ZDialogCancelInterface cancelInterface) {
        this.cancelInterface = cancelInterface;
        return this;
    }

    /**
     * 光标移到最后
     */
    public void setSelectionEnd() {
        etEdit.setSelection(etEdit.getEditableText()
                                  .length());
    }

    /**
     * 获取输入框对象
     */
    public EditText getEditText() {
        return etEdit;
    }


    @Override
    public void onClick(View v) {
        if (v == tvMakeSure) {
            if (submitInterface != null) {
                boolean flag = submitInterface.submit(etEdit.getText()
                                                            .toString());
                if (flag) {
                    cancel();
                }
            }
        } else if (v == tvCancel) {
            if (cancelInterface != null) {
                cancelInterface.cancel();
            } else {
                cancel();
            }
        }
    }
}
