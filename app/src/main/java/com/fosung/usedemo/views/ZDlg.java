/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午9:54
 **********************************************************/

package com.fosung.usedemo.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fosung.frame.utils.ScreenUtil;
import com.fosung.usedemo.R;


/**
 * 对话框基类
 */
public class ZDlg extends Dialog {

    public Context context;
    private boolean isCancelAble = true;//是否可以取消
    private int resBg;//对话框背景
    private int anim;//弹出消失动画

    /**
     * @param layResID layoutId
     */
    public ZDlg(Context context, int layResID) {
        super(context, R.style.style_dialog);
        this.context = context;
        setContentView(layResID);
        setLayout((int) (ScreenUtil.getScreenWidth(context) / 4 * 3), 0);
    }

    /**
     * @param view view
     */
    public ZDlg(Context context, View view) {
        super(context, R.style.style_dialog);
        this.context = context;
        setContentView(view);
    }

    /**
     * 对话框显示
     */
    public void show() {
        setDlgAnimation(anim);
        setDlgBackGround(resBg);                // 经测试，必须在setAnimation之后才可起效果
        setCanceledOnTouchOutside(isCancelAble);// 设置触摸对话框以外的地方取消对话框
        super.show();
    }

    /**
     * 对话框显示
     *
     * @param isCancelAble 点击其他地方是否可消失
     */
    public ZDlg setIsCancelAble(boolean isCancelAble) {
        this.isCancelAble = isCancelAble;
        return this;
    }

    /**
     * 对话框显示
     *
     * @param anim -1 无动画，else传入动画
     */
    public ZDlg setAnim(int anim) {
        this.anim = anim;
        return this;
    }

    /**
     * 对话框显示
     *
     * @param resBg -1透明， 0纯白， 背景图片资源Id
     */
    public ZDlg setDialogBackground(int resBg) {
        this.resBg = resBg;
        return this;
    }


    /*
     * 设定弹出动画
     *
     * @param animaStyle -1 无动画，0 默认动画， else传入动画
     */
    private void setDlgAnimation(int animaStyle) {
        switch (animaStyle) {
            case -1:
                break;
            case 0:
                getWindow().setWindowAnimations(R.style.style_anim_dialogWindow); // 设置窗口弹出动画
                break;
            default:
                getWindow().setWindowAnimations(animaStyle);
                break;
        }
    }

    /*
     * 设置对话框背景
     *
     * @param resBg -1透明， 0纯白， 背景图片资源Id
     */
    public void setDlgBackGround(int resBg) {
        switch (resBg) {
            case -1:
                getWindow().setBackgroundDrawableResource(R.color.transparent);
                break;
            case 0:
                getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
                break;
            default:
                getWindow().setBackgroundDrawableResource(resBg);
                break;
        }
    }

    /**
     * 设置窗口居中方式
     *
     * @param gravity 对齐方式
     */
    public ZDlg setGravity(int gravity) {
        getWindow().getAttributes().gravity = gravity;
        return this;
    }

    /**
     * 设置窗口透明度
     *
     * @param alpha 透明度
     */
    public ZDlg setAlpha(int alpha) {
        getWindow().getAttributes().alpha = alpha;
        return this;
    }

    /**
     * 设置窗口显示偏移
     *
     * @param x x小于0左移，大于0右移
     * @param y y小于0上移，大于0下移
     */
    public ZDlg windowDeploy(int x, int y) {
        if (x != 0 || y != 0) {
            Window window = getWindow();
            WindowManager.LayoutParams wl = window.getAttributes();

            // 根据x，y坐标设置窗口需要显示的位置
            wl.x = x;
            wl.y = y;
            window.setAttributes(wl);
        }
        return this;
    }

    /**
     * 设定Dialog的固定大小
     *
     * @param width 宽
     * @param high  高
     */
    public ZDlg setLayout(int width, int high) {
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        if (width > 0)
            wl.width = width;
        if (high > 0)
            wl.height = high;
        window.setAttributes(wl);
        return this;
    }

    /**
     * 通过资源ID获取view
     *
     * @param resId 资源ID
     * @return View
     */
    public <T> T getView(int resId) {
        return (T)findViewById(resId);
    }

    /**
     * 确定回调接口
     */
    public interface ZDialogParamSubmitInterface<T> {

        boolean submit(T t);
    }

    /**
     * 取消回调接口
     */
    public interface ZDialogParamCancelInterface<T> {

        boolean cancel(T t);
    }

    /**
     * 确定回调接口
     */
    public interface ZDialogSubmitInterface {

        boolean submit();
    }

    /**
     * 取消回调接口
     */
    public interface ZDialogCancelInterface {

        boolean cancel();
    }
}
