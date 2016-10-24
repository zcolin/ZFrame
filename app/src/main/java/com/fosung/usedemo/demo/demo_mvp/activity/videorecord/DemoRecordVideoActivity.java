/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-19 下午5:22
 * *********************************************************
 */

package com.fosung.usedemo.demo.demo_mvp.activity.videorecord;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fosung.frame.app.ResultActivityHelper;
import com.fosung.frame.utils.FileUtil;
import com.fosung.frame.utils.ScreenUtil;
import com.fosung.frame.utils.ToastUtil;
import com.fosung.usedemo.R;
import com.fosung.usedemo.amodule.base.BaseSecondLevelActivity;
import com.fosung.usedemo.views.VideoRecorderView;

/**
 * 视频拍摄页面
 */
public class DemoRecordVideoActivity extends BaseSecondLevelActivity implements View.OnTouchListener, VideoRecorderView.OnRecordListener {
    private VideoRecorderView videoRecorderView;
    private Button            buttonShoot;
    private RelativeLayout    rlBottomRoot;
    private ProgressBar       progressVideo;
    private TextView          textViewUpToCancel;     //上移取消
    private TextView          textViewReleaseToCancel;//释放取消

    private boolean isTouchOnUpToCancel = false;//是否触摸在松开取消的状态
    private float startY;                       //按下的位置

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_demo_activity_recordvideo);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recordStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recordStop();
    }

    public void initView() {
        videoRecorderView = getView(R.id.movieRecorderView);
        buttonShoot = getView(R.id.button_shoot);
        rlBottomRoot = getView(R.id.rl_bottom_root);
        progressVideo = getView(R.id.progressBar_loading);
        textViewUpToCancel = getView(R.id.textView_up_to_cancel);
        textViewReleaseToCancel = getView(R.id.textView_release_to_cancel);

        progressVideo.setMax(10);
        videoRecorderView.setOnRecordListener(this);
        resizeView();
        buttonShoot.setOnTouchListener(DemoRecordVideoActivity.this);
    }

    private void resizeView() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoRecorderView.getLayoutParams();
        layoutParams.height = ScreenUtil.getScreenWidth(this) * 4 / 3;//根据屏幕宽度设置预览控件的尺寸，为了解决预览拉伸问题
        videoRecorderView.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams rlBottomRootLayoutParams = (LinearLayout.LayoutParams) rlBottomRoot.getLayoutParams();
        rlBottomRootLayoutParams.height = ScreenUtil.getScreenWidth(this) / 3 * 2;
        rlBottomRoot.setLayoutParams(rlBottomRootLayoutParams);
    }

    /**
     * 重置状态
     */
    private void resetData() {
        recordStop();
        recordStart();

        if (videoRecorderView.getRecordFile() != null) {
            FileUtil.delete(videoRecorderView.getRecordFile());
        }
    }

    private void recordStop() {
        videoRecorderView.stop();
    }

    private void recordStart() {
        progressVideo.setProgress(0);
        buttonShoot.setOnTouchListener(this);
        textViewUpToCancel.setVisibility(View.GONE);
        textViewReleaseToCancel.setVisibility(View.GONE);
        videoRecorderView.initCamera();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            textViewUpToCancel.setVisibility(View.VISIBLE);//提示上移取消
            startY = event.getY();//记录按下的坐标
            videoRecorderView.record();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            textViewUpToCancel.setVisibility(View.GONE);
            textViewReleaseToCancel.setVisibility(View.GONE);

            if (startY - event.getY() > 100) {//上移超过一定距离取消录制，删除文件
                if (!videoRecorderView.isRecordFinish()) {
                    resetData();
                }
            } else {
                if (videoRecorderView.getRecordTime() > 2) {//录制时间超过2秒，录制完成
                    videoRecorderView.stop();
                    recordFinish();
                } else {
                    //时间不足取消录制，删除文件
                    ToastUtil.toastShort("视频录制时间太短");
                    resetData();
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //根据触摸上移状态切换提示
            if (startY - event.getY() > 100) {
                isTouchOnUpToCancel = true;//触摸在松开就取消的位置
                if (textViewUpToCancel.getVisibility() == View.VISIBLE) {
                    textViewUpToCancel.setVisibility(View.GONE);
                    textViewReleaseToCancel.setVisibility(View.VISIBLE);
                }
            } else {
                isTouchOnUpToCancel = false;//触摸在正常录制的位置
                if (textViewUpToCancel.getVisibility() == View.GONE) {
                    textViewUpToCancel.setVisibility(View.VISIBLE);
                    textViewReleaseToCancel.setVisibility(View.GONE);
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            resetData();
        }
        return true;
    }

    private void recordFinish() {
        if (isTouchOnUpToCancel) {//录制结束，还在上移删除状态没有松手，就复位录制
            resetData();
        } else {
            //录制结束，在正常位置，录制完成跳转页面
            buttonShoot.setOnTouchListener(null);
            Intent intent = new Intent(this, DemoRecordVideoPreviewActivity.class);
            intent.putExtra("data", videoRecorderView.getRecordFile().getAbsolutePath());
            startActivityWithCallback(intent, new ResultActivityHelper.ResultActivityListener() {
                @Override
                public void onResult(int requestCode, int resultCode, Intent data) {
                    if (resultCode == RESULT_OK) {
                        finish();
                    }
                }
            });
        }
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        resetData();
    }

    @Override
    public void onRecordFinish() {
        recordFinish();
    }

    @Override
    public void onProgressChanged(int maxTime, int currentTime) {
        progressVideo.setProgress(currentTime);
    }
}
