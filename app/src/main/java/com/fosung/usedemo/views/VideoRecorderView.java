/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-19 下午4:59
 * *********************************************************
 */

package com.fosung.usedemo.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioSource;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.VideoSource;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.LinearLayout;

import com.fosung.frame.app.FramePathConst;
import com.fosung.frame.utils.CalendarUtil;
import com.fosung.frame.utils.FileUtil;
import com.fosung.frame.utils.LogUtil;
import com.fosung.frame.utils.ToastUtil;
import com.fosung.usedemo.R;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 视频录制控件
 */
public class VideoRecorderView extends LinearLayout implements OnErrorListener, Comparator<Camera.Size> {
    private static final int RECORD_WIDTH_DEFAULT   = 640;
    private static final int RECORD_HEIGHT_DEFAULT  = 360;
    private static final int RECORD_MAXTIME_DEFAULT = 10;

    private SurfaceView   surfaceView;
    private SurfaceHolder surfaceHolder;

    private int     mWidth;//视频录制分辨率宽度
    private int     mHeight;//视频录制分辨率高度
    private boolean isOpenCamera;//是否一开始就打开摄像头
    private int     recordMaxTime;//最长拍摄时间
    private long    sizePicture    = 0;
    private int     cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头  
    private boolean isRecordFinish = true;
    private OnRecordListener onRecordListener;//录制监听

    private MediaRecorder            mediaRecorder;
    private Camera                   camera;
    private ScheduledExecutorService timer;//计时器
    private int                      timeCount;//时间计数
    private File recordFile = null;//视频文件
    private Context context;

    public VideoRecorderView(Context context) {
        this(context, null);
    }

    public VideoRecorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public VideoRecorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VideoRecorderView, defStyle, 0);
        mWidth = a.getInteger(R.styleable.VideoRecorderView_record_width, RECORD_WIDTH_DEFAULT);//默认640
        mHeight = a.getInteger(R.styleable.VideoRecorderView_record_height, RECORD_HEIGHT_DEFAULT);//默认360
        isOpenCamera = a.getBoolean(R.styleable.VideoRecorderView_is_open_camera, true);//默认打开摄像头
        recordMaxTime = a.getInteger(R.styleable.VideoRecorderView_record_max_time, RECORD_MAXTIME_DEFAULT);//默认最大拍摄时间为10s
        a.recycle();

        LayoutInflater.from(context)
                      .inflate(R.layout.view_videorecorder, this);
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new CustomCallBack());
    }

    /**
     * SurfaceHolder回调
     */
    private class CustomCallBack implements Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (isOpenCamera) {
                initCamera();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            try {
                // 实现自动对焦
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera arg1) {
                        if (success) {
                            startPreView();
                            camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦。
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (isOpenCamera) {
                releaseCameraResource();
            }
        }
    }

    /**
     * 初始化摄像头
     */
    public void initCamera() {
        if (camera != null) {
            releaseCameraResource();
        }

        try {
            if (checkCameraFacing(Camera.CameraInfo.CAMERA_FACING_FRONT)) {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);// 默认打后置摄像头
                cameraPosition = 1;
            } else if (checkCameraFacing(Camera.CameraInfo.CAMERA_FACING_BACK)) {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                cameraPosition = 0;
            }

            setCameraParams();
            camera.setDisplayOrientation(90);
            camera.cancelAutoFocus();// 如果要实现连续的自动对焦，这一句必须加上
            startPreView();
            camera.unlock();
        } catch (Exception e) {
            ToastUtil.toastShort("摄像头初始化失败!");
            releaseCameraResource();
            ((Activity) context).finish();
        }
    }

    private void startPreView() {
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查是否有摄像头
     *
     * @param facing 前置还是后置
     */
    private boolean checkCameraFacing(int facing) {
        int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);
            if (facing == info.facing) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置摄像头参数
     */
    private void setCameraParams() {
        if (camera != null) {
            Parameters params = camera.getParameters();
            params.setRotation(90);
            params.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
            List<Camera.Size> supportedPictureSizes = params.getSupportedPictureSizes();
            for (Camera.Size size : supportedPictureSizes) {
                sizePicture = (size.height * size.width) > sizePicture ? size.height * size.width : sizePicture;
            }
            LogUtil.i("VideoRecorderView.setCameraParams", "手机支持的最大像素supportedPictureSizes====" + sizePicture);
            setPreviewSize(params);
            camera.setParameters(params);
        }
    }

    /**
     * 根据手机支持的视频分辨率，设置预览尺寸
     */
    private void setPreviewSize(Parameters params) {
        if (camera == null) {
            return;
        }
        //获取手机支持的分辨率集合，并以宽度为基准降序排序
        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        Collections.sort(previewSizes, this);

        float tmp = 0f;
        float minDiff = 100f;
        float ratio = 3.0f / 4.0f;//高宽比率3:4，且最接近屏幕宽度的分辨率，可以自己选择合适的想要的分辨率
        Camera.Size best = null;
        for (Camera.Size s : previewSizes) {
            tmp = Math.abs(((float) s.height / (float) s.width) - ratio);
            if (tmp < minDiff) {
                minDiff = tmp;
                best = s;
            }
        }

        if (best != null) {
            params.setPreviewSize(best.width, best.height);//预览比率
            LogUtil.i("VideoRecorderView.setPreviewSize", "setPreviewSize BestSize: width:" + best.width + "   height:" + best.height);

            //大部分手机支持的预览尺寸和录制尺寸是一样的，也有特例，有些手机获取不到，那就把设置录制尺寸放到设置预览的方法里面
            if (params.getSupportedVideoSizes() == null || params.getSupportedVideoSizes()
                                                                 .size() == 0) {
                mWidth = best.width;
                mHeight = best.height;
            } else {
                setVideoSize(params);
            }
        } else {
            ToastUtil.toastShort("预览屏幕比例无法获取");
        }
    }

    /**
     * 根据手机支持的视频分辨率，设置录制尺寸
     */
    private void setVideoSize(Parameters params) {
        if (camera == null) {
            return;
        }
        //获取手机支持的分辨率集合，并以宽度为基准降序排序
        List<Camera.Size> previewSizes = params.getSupportedVideoSizes();
        Collections.sort(previewSizes, this);

        float tmp = 0f;
        float minDiff = 100f;
        float ratio = 3.0f / 4.0f;//高宽比率3:4，且最接近屏幕宽度的分辨率
        Camera.Size best = null;
        for (Camera.Size s : previewSizes) {
            tmp = Math.abs(((float) s.height / (float) s.width) - ratio);
            if (tmp < minDiff) {
                minDiff = tmp;
                best = s;
            }
        }

        if (best != null) {
            //设置录制尺寸
            mWidth = best.width;
            mHeight = best.height;
        } else {
            ToastUtil.toastShort("录制屏幕比例无法获取");
        }
    }

    @Override
    public int compare(Camera.Size lhs, Camera.Size rhs) {
        if (lhs.width > rhs.width) {
            return -1;
        } else if (lhs.width == rhs.width) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * 释放摄像头资源
     */
    private void releaseCameraResource() {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.lock();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            camera = null;
        }
    }

    /**
     * 录制视频初始化
     */
    private void initRecord() throws Exception {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
        }
        mediaRecorder.reset();

        if (camera != null)
            mediaRecorder.setCamera(camera);

        mediaRecorder.setOnErrorListener(this);
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        mediaRecorder.setVideoSource(VideoSource.CAMERA);//视频源
        mediaRecorder.setAudioSource(AudioSource.MIC);//音频源
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mediaRecorder.setVideoSize(mWidth, mHeight);//设置分辨率
        //mediaRecorder.setVideoFrameRate(25);//设置每秒帧数 这个设置有可能会出问题，有的手机不支持这种帧率就会录制失败，这里使用默认的帧率，当然视频的大小肯定会受影响

        //设置编码比特率
        if (sizePicture <= 8000000) {
            mediaRecorder.setVideoEncodingBitRate(8 * 1024 * 512);
        } else {
            mediaRecorder.setVideoEncodingBitRate(5 * 1024 * 512);
        }

        mediaRecorder.setVideoSize(mWidth, mHeight);
        mediaRecorder.setOrientationHint(90);//输出旋转90度，保持竖屏录制
        //mediaRecorder.setMaxDuration(Constant.MAXVEDIOTIME * 1000);
        mediaRecorder.setOutputFile(recordFile.getAbsolutePath());
        mediaRecorder.prepare();
        mediaRecorder.start();
    }

    public boolean isRecordFinish() {
        return isRecordFinish;
    }

    /**
     * 开始录制视频
     */
    public void record() {
        String savePath = FramePathConst.getInstance()
                                        .getPathTemp() + "zz_videoCapture_" + CalendarUtil.getDateTime("yyyyMMddHHmmss") + ".mp4";
        FileUtil.checkFilePath(savePath, false);
        recordFile = new File(savePath);

        try {
            if (!isOpenCamera)
                initCamera();

            initRecord();

            if (timer != null) {
                timer.shutdown();
            }
            timeCount = 0;
            timer = Executors.newSingleThreadScheduledExecutor();
            timer.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    timeCount++;
                    if (onRecordListener != null) {
                        onRecordListener.onProgressChanged(recordMaxTime, timeCount);
                    }

                    //达到指定时间，停止拍摄
                    if (timeCount >= recordMaxTime) {
                        boolean isFinish = isRecordFinish;
                        stop();
                        if (onRecordListener != null && !isFinish) {
                            onRecordListener.onRecordFinish();
                        }
                    }
                }
            }, 0, 1, TimeUnit.SECONDS);
            isRecordFinish = false;
        } catch (Exception e) {
            e.printStackTrace();
            if (mediaRecorder != null) {
                mediaRecorder.release();
            }
            releaseCameraResource();
        }
    }

    /**
     * 停止拍摄
     */
    public void stop() {
        stopRecord();
        releaseRecord();
        releaseCameraResource();
    }

    /**
     * 停止录制
     */
    public void stopRecord() {
        isRecordFinish = true;
        if (timer != null) {
            timer.shutdown();
        }

        if (mediaRecorder != null) {
            mediaRecorder.setOnErrorListener(null);//设置后防止崩溃
            mediaRecorder.setPreviewDisplay(null);
            try {
                mediaRecorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 释放资源
     */
    private void releaseRecord() {
        if (mediaRecorder != null) {
            mediaRecorder.setOnErrorListener(null);
            try {
                mediaRecorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mediaRecorder = null;
    }

    /**
     * 设置最大录像时间
     */
    public void setRecordMaxTime(int recordMaxTime) {
        this.recordMaxTime = recordMaxTime;
    }

    /**
     * 返回录像文件
     *
     * @return recordFile
     */
    public File getRecordFile() {
        return recordFile;
    }

    /**
     * 返回录像时长
     *
     * @return recordFile
     */
    public int getRecordTime() {
        return timeCount;
    }

    /**
     * 设置录制进度监听
     */
    public void setOnRecordListener(OnRecordListener onRecordListener) {
        this.onRecordListener = onRecordListener;
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        if (onRecordListener != null) {
            onRecordListener.onError(mr, what, extra);
        }

        try {
            if (mr != null)
                mr.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 录制相关接口
     */
    public interface OnRecordListener {
        void onError(MediaRecorder mr, int what, int extra);

        void onRecordFinish();

        /**
         * 进度变化
         *
         * @param maxTime     最大时间，单位秒
         * @param currentTime 当前进度
         */
        void onProgressChanged(int maxTime, int currentTime);
    }
}