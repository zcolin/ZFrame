/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-19 下午5:22
 * *********************************************************
 */

package com.fosung.usedemo.demo.demo_mvp.activity.videorecord;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.fosung.frame.utils.ScreenUtil;
import com.fosung.frame.utils.ToastUtil;
import com.fosung.usedemo.R;
import com.fosung.usedemo.amodule.base.BaseSecondLevelActivity;

import java.io.File;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 视频认证上传界面
 */
public class DemoRecordVideoPreviewActivity extends BaseSecondLevelActivity implements View.OnClickListener {
    private VideoView                videoViewShow;
    private ImageView                imageViewShow;
    private Button                   buttonDone;
    private RelativeLayout           rlBottomRoot;
    private Button                   buttonPlay;
    private ProgressBar              progressVideo;
    private File                     file;//视频路径
    private int                      time;//视频时间
    private int                      currentTime;
    private ScheduledExecutorService timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_demo_activity_recordvideo_preview);
        String path = getIntent().getStringExtra("data");
        setToolbarTitle("视频预览");
        if (path == null) {
            ToastUtil.toastShort("数据传递错误!");
            this.finish();
            return;
        }
        file = new File(path);

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoViewShow != null) {
            videoViewShow.stopPlayback();
        }
        if (timer != null) {
            timer.shutdown();
        }
    }

    public void initView() {
        videoViewShow = getView(R.id.videoView_show);
        imageViewShow = getView(R.id.imageView_show);
        buttonDone = getView(R.id.button_done);
        rlBottomRoot = getView(R.id.rl_bottom_root);
        buttonPlay = getView(R.id.button_play);
        progressVideo = getView(R.id.progressBar_loading);

        buttonDone.setOnClickListener(this);
        buttonPlay.setOnClickListener(this);
        resizeView();
    }

    private void resizeView() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoViewShow.getLayoutParams();
        layoutParams.height = ScreenUtil.getScreenWidth(this) * 4 / 3;//根据屏幕宽度设置预览控件的尺寸，为了解决预览拉伸问题
        videoViewShow.setLayoutParams(layoutParams);
        imageViewShow.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams rlBottomRootLayoutParams = (LinearLayout.LayoutParams) rlBottomRoot.getLayoutParams();
        rlBottomRootLayoutParams.height = ScreenUtil.getScreenWidth(this) / 3 * 2;
        rlBottomRoot.setLayoutParams(rlBottomRootLayoutParams);
    }

    public void initData() {
        //获取第一帧图片，预览使用
        if (file.length() != 0) {
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(file.getPath());
            Bitmap bitmap = media.getFrameAtTime();
            imageViewShow.setImageBitmap(bitmap);
        }
    }

    /**
     * 播放视频
     */
    private void playVideo() {
        progressVideo.setProgress(0);
        videoViewShow.setVideoPath(file.getPath());
        videoViewShow.start();
        videoViewShow.requestFocus();
        videoViewShow.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!videoViewShow.isPlaying()) {
                    buttonPlay.setVisibility(View.VISIBLE);
                }
            }
        });
        videoViewShow.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (timer != null) {
                    timer.shutdown();
                    timer = null;
                }
                return false;
            }
        });

        currentTime = 0;//时间计数器重新赋值
        if (timer != null) {
            timer.shutdown();
            timer = null;
        }
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleWithFixedDelay(new TimerTask() {
            @Override
            public void run() {
                time = (videoViewShow.getDuration() + 1000) / 1000;
                currentTime = (videoViewShow.getCurrentPosition() + 1500) / 1000;
                progressVideo.setMax(videoViewShow.getDuration());
                progressVideo.setProgress(videoViewShow.getCurrentPosition());
                //达到指定时间，停止播放
                if (!videoViewShow.isPlaying() && time > 0) {
                    progressVideo.setProgress(videoViewShow.getDuration());
                    if (timer != null) {
                        timer.shutdown();
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_done:
                processComplate();
                break;
            case R.id.button_play:
                buttonPlay.setVisibility(View.GONE);
                imageViewShow.setVisibility(View.GONE);
                playVideo();
                break;
        }
    }


    private void processComplate() {
        //TODO 上传视频等逻辑


        setResult(RESULT_OK);
        finish();
    }
}
