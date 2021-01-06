/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */
package com.zcolin.frame.app;

import android.app.Activity;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import android.util.SparseArray;

/**
 * 用于管理startActivityForResult, 可以实现不需要重写onActivityResult，直接通过回调函数回调
 *
 * @see BaseFrameActivity#startActivityWithCallback(Intent, ResultActivityListener)
 * @see BaseFrameFrag#startActivityWithCallback(Intent, ResultActivityListener)
 */

public class ResultActivityHelper {
    private final static int REQUEST_CODE_START = 10000;//起始requestCode，不让request code冲突

    private SparseArray<ResultActivityListener> requests       = new SparseArray<>();   //记录每一次请求的回调方法
    private int                                 currentReqCode = REQUEST_CODE_START;    //记录下一个请求的时候会生成的REQUEST_CODE
    private Activity                            mActivity;
    private Fragment                            mFragment;

    public ResultActivityHelper(Activity activity) {
        this.mActivity = activity;
    }

    public ResultActivityHelper(Fragment fragment) {
        this.mFragment = fragment;
    }

    public void startActivityForResult(Intent i, ResultActivityListener listener) {
        currentReqCode++;
        requests.put(currentReqCode, listener);
        if (mActivity != null) {
            mActivity.startActivityForResult(i, currentReqCode);
        } else if (mFragment != null) {
            mFragment.startActivityForResult(i, currentReqCode);
        }
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        ResultActivityListener listener = requests.get(requestCode);
        if (listener != null) {
            listener.onResult(resultCode, data);
            requests.remove(requestCode); //请求完就清除掉
            return true;
        }
        return false;
    }

    public interface ResultActivityListener {
        void onResult(int resultCode, Intent data);
    }
}
