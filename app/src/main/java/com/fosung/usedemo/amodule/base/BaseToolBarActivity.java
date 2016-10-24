/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-18 下午1:41
 **********************************************************/

package com.fosung.usedemo.amodule.base;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fosung.frame.app.BaseFrameActivity;
import com.fosung.frame.utils.ScreenUtil;
import com.fosung.frame.utils.StringUtil;
import com.fosung.usedemo.R;


/**
 * 有ToolBar的页面继承此类， 默认包含了三个操作按钮（）左侧一个，右侧两个）和标题
 */
public class BaseToolBarActivity extends BaseFrameActivity {
    /*
    * 两个属性
    * 1、toolbar是否悬浮在窗口之上
    * 2、toolbar的高度获取
    * */
    public static int[] ATTRS = {R.attr.windowActionBarOverlay, R.attr.actionBarSize};
    public  Toolbar  toolbar;
    private View     toolBarView;            //自定义的toolBar的布局
    private TextView toolbarTitleView;    //标题
    private TextView toolbarLeftBtn;    //预制按钮一
    private TextView toolbarRightBtn;    //预制按钮二

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(initToolBar(layoutResID));
        setSupportActionBar(toolbar);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(initToolBar(view));
        setSupportActionBar(toolbar);
    }

    private FrameLayout initToolBar(int layoutResID) {
        View userView = LayoutInflater.from(this)
                                      .inflate(layoutResID, null);
        return initToolBar(userView);
    }

    private FrameLayout initToolBar(View userView) {
        /*直接创建一个帧布局，作为视图容器的父容器*/
        FrameLayout contentView = new FrameLayout(this);

		/*将toolbar引入到父容器中*/
        View toolbarLay = LayoutInflater.from(this)
                                        .inflate(R.layout.toolbar, null);
        FrameLayout.LayoutParams layParam = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        contentView.addView(toolbarLay, layParam);

        //不明原因导致布局向右移动了一些，移动回来
        //        ((FrameLayout.LayoutParams) toolbarLay.getLayoutParams()).leftMargin = -40;
        toolbar = (Toolbar) toolbarLay.findViewById(R.id.id_tool_bar);
        if (isImmerse()) {
            int statusBarHeight = ScreenUtil.getStatusBarHeight(this);
            toolbar.setPadding(0, statusBarHeight, 0, 0);
            toolbar.getLayoutParams().height += statusBarHeight;
        }
        toolBarView = getLayoutInflater().inflate(R.layout.toolbar_baseview, toolbar);
        toolbarTitleView = (TextView) toolBarView.findViewById(R.id.toolbar_title);
        toolbarLeftBtn = (TextView) toolBarView.findViewById(R.id.toolbar_btn_left);
        toolbarRightBtn = (TextView) toolBarView.findViewById(R.id.toolbar_btn_right);
        toolbarTitleView.setVisibility(View.GONE);
        toolbarLeftBtn.setVisibility(View.GONE);
        toolbarRightBtn.setVisibility(View.GONE);
        BaseClickListener clickListener = new BaseClickListener();
        toolbarLeftBtn.setOnClickListener(clickListener);
        toolbarRightBtn.setOnClickListener(clickListener);

		/*将自定义的布局引入到父容器中*/
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        TypedArray typedArray = getTheme().obtainStyledAttributes(ATTRS);
        /*获取主题中定义的悬浮标志*/
        boolean overly = typedArray.getBoolean(0, false);
        /*获取主题中定义的toolbar的高度*/
        int toolBarSize = toolbar.getLayoutParams().height;
        typedArray.recycle();
        /*如果是悬浮状态，则不需要设置间距*/
        params.topMargin = overly ? 0 : toolBarSize;
        contentView.addView(userView, params);

        return contentView;
    }

    /**
     * 设置ToolBar的标题
     *
     * @param title ：ToolBar的标题
     */
    public void setToolbarTitle(String title) {
        if (StringUtil.isNotEmpty(title)) {
            toolbarTitleView.setText(title);
            toolbarTitleView.setVisibility(View.VISIBLE);
        } else {
            toolbarTitleView.setText(null);
            toolbarTitleView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置ToolBar的预置按钮长按可用
     */
    public void setLongClickEnable() {
        BaseLongClickListener longClickListener = new BaseLongClickListener();
        toolbarLeftBtn.setOnLongClickListener(longClickListener);
        toolbarRightBtn.setOnLongClickListener(longClickListener);
    }

    /**
     * 设置ToolBar的预置按钮长按不可用
     */
    public void setLongClickDisable() {
        toolbarLeftBtn.setOnLongClickListener(null);
        toolbarRightBtn.setOnLongClickListener(null);
    }

    /**
     * 设置ToolBar预制按钮一的文字
     *
     * @param extra : 显示的文字
     */
    public void setToolbarLeftBtnText(String extra) {
        if (StringUtil.isNotEmpty(extra)) {
            toolbarLeftBtn.setText(extra);
            toolbarLeftBtn.setVisibility(View.VISIBLE);
        } else {
            toolbarLeftBtn.setText(null);
            toolbarLeftBtn.setVisibility(View.GONE);
        }
    }

    public void setToolbarLeftBtnCompoundDrawableLeft(int res) {
        toolbarLeftBtn.setCompoundDrawablesWithIntrinsicBounds(res, 0, 0, 0);
        toolbarLeftBtn.setVisibility(View.VISIBLE);
    }

    public void setToolbarLeftBtnCompoundDrawableLeft(Drawable able) {
        toolbarLeftBtn.setCompoundDrawablesWithIntrinsicBounds(able, null, null, null);
        toolbarLeftBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 设置ToolBar预制按钮一的图片
     *
     * @param res : 按钮显示图片资源ID
     */
    public void setToolbarLeftBtnBackground(int res) {
        toolbarLeftBtn.setBackgroundResource(res);
        toolbarLeftBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 设置ToolBar预制按钮一的图片
     *
     * @param able : 按钮显示图片
     */
    @SuppressWarnings("deprecation")
    public void setToolbarLeftBtnBackground(Drawable able) {
        if (able != null) {
            toolbarLeftBtn.setBackgroundDrawable(able);
            toolbarLeftBtn.setVisibility(View.VISIBLE);
        } else {
            toolbarLeftBtn.setBackgroundDrawable(null);
            toolbarLeftBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 设置ToolBar预制按钮二的文字
     *
     * @param extra : 按钮显示的文字
     */
    public void setToolbarRightBtnText(String extra) {
        if (StringUtil.isNotEmpty(extra)) {
            toolbarRightBtn.setText(extra);
            toolbarRightBtn.setVisibility(View.VISIBLE);
        } else {
            toolbarRightBtn.setText(null);
            toolbarRightBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 设置ToolBar预制按钮二的图片
     *
     * @param res 显示的资源ID
     */
    public void setToolBarRightBtnBackground(int res) {
        toolbarRightBtn.setBackgroundResource(res);
        toolbarRightBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 设置ToolBar预制按钮二的图片
     *
     * @param able 显示的Drawable对象
     */
    @SuppressWarnings("deprecation")
    public void setToolBarRightBtnBackground(Drawable able) {
        if (able != null) {
            toolbarRightBtn.setBackgroundDrawable(able);
            toolbarRightBtn.setVisibility(View.VISIBLE);
        } else {
            toolbarRightBtn.setBackgroundDrawable(null);
            toolbarRightBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 设置ToolBar的背景颜色
     *
     * @param color 颜色值
     */
    public void setToolBarBackBroundColor(int color) {
        toolBarView.setBackgroundColor(color);
    }

    /**
     * 设置ToolBar的背景资源
     *
     * @param res 资源ID
     */
    public void setToolBarBackBroundRes(int res) {
        toolBarView.setBackgroundResource(res);
    }

    /**
     * 获取ToolBar的标题控件
     *
     * @return 标题控件
     */
    public TextView getToolBarTitleView() {
        return toolbarTitleView;
    }

    /**
     * 获取ToolBar的预制按钮一
     *
     * @return 预制按钮一控件
     */
    public TextView getToolBarExtraView() {
        return toolbarLeftBtn;
    }

    /**
     * 获取ToolBar的预制按钮二
     *
     * @return 预制按钮二控件
     */
    public TextView getToolBarExtra2View() {
        return toolbarRightBtn;
    }

    /**
     * 预制按钮一点击回调，子类如需要处理点击事件，重写此方法
     */
    protected void onToolBarLeftBtnClick() {
    }

    /**
     * 预制按钮二点击回调，子类如需要处理点击事件，重写此方法
     */
    protected void onToolBarRightBtnClick() {
    }

    /**
     * 预制按钮一长按回调，子类如需要处理点击事件，重写此方法
     */
    protected void onToolBarLeftBtnLongClick() {
    }

    /**
     * 预制按钮二长按回调，子类如需要处理点击事件，重写此方法
     */
    protected void onToolBarRightBtnLongClick() {
    }

    /*
     * 预置按钮的点击事件类 
     */
    private class BaseClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.toolbar_btn_left) {
                onToolBarLeftBtnClick();
            } else if (v.getId() == R.id.toolbar_btn_right) {
                onToolBarRightBtnClick();
            }
        }
    }

    /*
     * 预制按钮的 长按事件类
     */
    private class BaseLongClickListener implements OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == R.id.toolbar_btn_left) {
                onToolBarLeftBtnLongClick();
            } else if (v.getId() == R.id.toolbar_btn_right) {
                onToolBarRightBtnLongClick();
            }
            return true;
        }
    }
}
