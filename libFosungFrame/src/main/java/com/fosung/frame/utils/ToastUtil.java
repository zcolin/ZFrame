/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.fosung.frame.utils;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

import com.fosung.frame.app.BaseApp;


/**
 * Toast封装提示
 */
public class ToastUtil {

    private static Toast toast;    // 提示框，静态全局

    /**
     * 普通提示
     *
     * @param strRes 显示信息
     */
    public static void toastShort(int strRes) {
        if (null == BaseApp.APP_CONTEXT) {
            return;
        }
        toastShow(BaseApp.APP_CONTEXT.getString(strRes), Toast.LENGTH_SHORT, null);
    }

    /**
     * 普通提示
     *
     * @param strRes 显示信息
     */
    public static void toastLong(int strRes) {
        if (null == BaseApp.APP_CONTEXT) {
            return;
        }
        toastShow(BaseApp.APP_CONTEXT.getString(strRes), Toast.LENGTH_LONG, null);
    }

    /**
     * 普通提示，   Toast.LENGTH_SHORT
     *
     * @param strInfo 显示信息
     */
    public static void toastShort(String strInfo) {
        toastShow(strInfo, Toast.LENGTH_SHORT, null);
    }

    /**
     * 普通提示，   Toast.LENGTH_SHORT
     *
     * @param strInfo 显示信息
     */
    public static void toastLong(String strInfo) {
        toastShow(strInfo, Toast.LENGTH_LONG, null);
    }

    /**
     * 设置信息显示
     *
     * @param message   显示信息
     * @param nKeepTime 显示时间
     * @param able      显示的图片
     */
    public static void toastShow(String message, int nKeepTime, Drawable able) {
        ToastInfo toastInfo = new ToastInfo();
        toastInfo.msg = message;
        toastInfo.nKeepTime = nKeepTime;
        toastInfo.able = able;
        MYHandler handler = new MYHandler(Looper.getMainLooper());
        Message msg = handler.obtainMessage();
        msg.arg1 = 0;
        msg.obj = toastInfo;
        handler.sendMessage(msg);
    }

    /*
     * 显示Toast，必须主线程调用
     * 
     * @param strInfo		显示信息
     * @param nKeepTime		显示时间
     * @param able			显示的图片
     */
    private static void ToastShowWithDrawble(String strInfo, int nKeepTime, Drawable able) {
        try {
            if (null == BaseApp.APP_CONTEXT) {
                return;
            } else if (nKeepTime < 0) {
                nKeepTime = Toast.LENGTH_SHORT;
            }

            if (toast != null) {
                toast.setText(strInfo);
            } else {
                toast = Toast.makeText(BaseApp.APP_CONTEXT, strInfo, nKeepTime);
/*                LinearLayout view = (LinearLayout) toast.getView();
                view.setBackgroundColor(Color.TRANSPARENT);
                TextView messageView = (TextView) view.getChildAt(0);
                messageView.setGravity(Gravity.CENTER_VERTICAL);
                messageView.setBackgroundResource(R.drawable.bg_toast);
                messageView.setPadding(10, 15, 15, 15);
                 view.setBackgroundColor(Color.rgb(30, 144, 255));
                 view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                if (able != null) {
                    able.setBounds(0, 0, able.getMinimumWidth(), able.getMinimumHeight());
                    messageView.setCompoundDrawables(able, null, null, null);
                }
                messageView.setTextSize(14);
                messageView.setTextColor(Color.parseColor("#ffffff"));
                messageView.setShadowLayer(0, 0, 0, 0);
                toast.setView(view);*/
                toast.setGravity(Gravity.BOTTOM, 0, DisplayUtil.dip2px(BaseApp.APP_CONTEXT, 45));
            }
            toast.show();
        } catch (Exception e) {
            LogUtil.d("Toast", LogUtil.ExceptionToString(e));
        }
    }

    /*
     * 子线程与主线程的通讯
     * 使子线程也可以通过通知主线程调用Toast
     */
    static class MYHandler extends Handler {

        public MYHandler(Looper mainLooper) {
            super(mainLooper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg != null && msg.arg1 == 0 && msg.obj != null) {
                ToastInfo info = (ToastInfo) msg.obj;
                ToastShowWithDrawble(info.msg, info.nKeepTime, info.able);
            }
        }
    }

    /*
     * Toast信息携带类
     */
    private static class ToastInfo {
        String   msg;
        int      nKeepTime;
        Drawable able;
    }
}
