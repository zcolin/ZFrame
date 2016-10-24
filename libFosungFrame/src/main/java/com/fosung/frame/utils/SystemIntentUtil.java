/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-14 下午1:54
 * *********************************************************
 */

package com.fosung.frame.utils;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.fosung.frame.app.BaseFrameActivity;
import com.fosung.frame.app.BaseFrameFrag;
import com.fosung.frame.app.FramePathConst;
import com.fosung.frame.app.ResultActivityHelper;
import com.fosung.frame.permission.PermissionHelper;
import com.fosung.frame.permission.PermissionsResultAction;

import java.io.File;


/**
 * 调用系统Intent
 */
public class SystemIntentUtil {
    /**
     * 获取默认的照片路径
     * 名称为：TEMP_PATH/zz_takephoto_yyyy-MM-dd HH:mm:ss.jpg
     */
    public static String getTempTakePhotoPath() {
        return FramePathConst.getInstance()
                             .getPathTemp() + "zz_takephoto_" + CalendarUtil.getDateTime() + ".jpg";
    }

    /**
     * 调用系统拍照
     *
     * @param context        只能为BaseFrag或者BaseActivity的子类
     * @param savePath       拍照完后的保存路径
     * @param resultListener 拍照完的回调函数接口
     */
    public static Uri takePhoto(final Object context, String savePath, final ResultActivityHelper.ResultActivityListener resultListener) {
        if (!SDCardUtil.isSDCardEnable()) {
            return null;
        }

        FileUtil.checkFilePath(savePath, false);
        File out = new File(savePath);
        final Uri uri = Uri.fromFile(out);
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        PermissionHelper.requestPermission(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                if (context instanceof BaseFrameActivity) {
                    ((BaseFrameActivity) context).startActivityWithCallback(intent, resultListener);
                } else if (context instanceof BaseFrameFrag) {
                    ((BaseFrameFrag) context).startActivityWithCallback(intent, resultListener);
                }
            }

            @Override
            public void onDenied(String permission) {
                ToastUtil.toastShort("请授予本程序SD卡写入权限和相机权限！");
            }
        });
        return uri;
    }


    /**
     * 获取默认的裁剪图片路径
     * 名称为：TEMP_PATH/zz_cropphoto_yyyy-MM-dd HH:mm:ss.jpg
     */
    public static String getTempCropPhotoPath() {
        return FramePathConst.getInstance()
                             .getPathTemp() + "zz_cropphoto_" + CalendarUtil.getDateTime() + ".jpg";
    }

    /**
     * 拍照后裁剪图片
     *
     * @param context        只能为BaseFrag或者BaseActivity的子类
     * @param savePath       裁剪完成后保存路径
     * @param data           需要裁剪的图片的Uri
     * @param cropX          x像素（单位为dp）
     * @param cropY          y像素（单位为dp）
     * @param resultListener 完成后回调
     */
    public static String cropPhoto(final Object context, final String savePath, final Uri data, final int cropX,
                                   final int cropY, final ResultActivityHelper.ResultActivityListener resultListener) {
        if (!SDCardUtil.isSDCardEnable()) {
            return null;
        }

        PermissionHelper.requestReadWriteSdCardPermission(context, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                FileUtil.checkFilePath(savePath, false);
                
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(data, "image/*");

                // 裁剪头像的绝对路径
                Uri cropUri = Uri.fromFile(new File(savePath));
                intent.putExtra("output", cropUri);
                intent.putExtra("crop", "true");
                int[] arr = minScale(cropX, cropY);
                intent.putExtra("aspectX", arr[0]);// 裁剪框比例
                intent.putExtra("aspectY", arr[1]);
                intent.putExtra("outputX", cropX);// 输出图片大小
                intent.putExtra("outputY", cropY);
                intent.putExtra("scale", true);// 去黑边
                intent.putExtra("scaleUpIfNeeded", true);// 去黑边
                //是否要返回值。
                //intent.putExtra("return-data", true);
                if (context instanceof BaseFrameActivity) {
                    ((BaseFrameActivity) context).startActivityWithCallback(intent, resultListener);
                } else if (context instanceof BaseFrameFrag) {
                    ((BaseFrameFrag) context).startActivityWithCallback(intent, resultListener);
                }
            }

            @Override
            public void onDenied(String permission) {
                ToastUtil.toastShort("请授予本程序读写SD卡权限");
            }
        });

        return savePath;
    }

    /**
     * 选取照片
     *
     * @param context 只能为BaseFrag或者BaseActivity的子类
     */
    public void selectPhoto(final Object context, final ResultActivityHelper.ResultActivityListener resultListener) {
        PermissionHelper.requestReadSdCardPermission(context, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                if (context instanceof BaseFrameActivity) {
                    ((BaseFrameActivity) context).startActivityWithCallback(intent, resultListener);
                } else if (context instanceof BaseFrameFrag) {
                    ((BaseFrameFrag) context).startActivityWithCallback(intent, resultListener);
                }
            }

            @Override
            public void onDenied(String permission) {
                ToastUtil.toastShort("请授予本程序读取SD卡权限");
            }
        });
    }

    /**
     * 两数最小整数比
     */
    private static int[] minScale(int a, int b) {
        int[] arr = new int[2];
        int tmp = a;
        if (a > b) {
            tmp = b;
        }
        for (int i = tmp; i > 0; i--) {
            if (a % i == 0 && b % i == 0) {
                arr[0] = a / i;
                arr[1] = b / i;
                break;
            }
        }
        return arr;
    }

    /**
     * 拨打电话
     *
     * @param context 只能为BaseFrag或者BaseActivity的子类
     */
    public static void call(final Object context, final String phoneNum) {
        PermissionHelper.requestCallPhonePermission(context, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
                phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    if (context instanceof BaseFrameActivity) {
                        ((BaseFrameActivity) context).startActivity(phoneIntent);
                    } else if (context instanceof BaseFrameFrag) {
                        ((BaseFrameFrag) context).startActivity(phoneIntent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDenied(String permission) {
                ToastUtil.toastShort("请授予本程序拨打电话权限！");
            }
        });
    }

    /**
     * 跳转到拨打电话页面，写入指定号码
     *
     * @param context 只能为BaseFrag或者BaseActivity的子类
     */
    public static void dial(final Object context, String phoneNum) {
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
        phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (context instanceof BaseFrameActivity) {
            ((BaseFrameActivity) context).startActivity(phoneIntent);
        } else if (context instanceof BaseFrameFrag) {
            ((BaseFrameFrag) context).startActivity(phoneIntent);
        }
    }

    /**
     * 获取默认的视频路径
     * 名称为：TEMP_PATH/zz_takephoto_yyyy-MM-dd HH:mm:ss.jpg
     */
    public static String getTempVideoCapturePath() {
        return FramePathConst.getInstance()
                             .getPathTemp() + "zz_videoCapture_" + CalendarUtil.getDateTime() + ".mp4";
    }
    
    /**
     * @param savePath      视频保存路径
     * @param durationLimit 最大时间限制 秒  为0不限制
     * @param isHighQuality 是否高质量
     * @return 视频保存路径
     */
    public static String videoCapture(final Object context, final String savePath, final int durationLimit, final boolean isHighQuality,
                                      final ResultActivityHelper.ResultActivityListener resultListener) {
        PermissionHelper.requestPermission(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                FileUtil.checkFilePath(savePath, false);
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(savePath)));
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, isHighQuality ? 1 : 0);
                if (durationLimit > 0) {
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, durationLimit);
                }
                if (context instanceof BaseFrameActivity) {
                    ((BaseFrameActivity) context).startActivityWithCallback(intent, resultListener);
                } else if (context instanceof BaseFrameFrag) {
                    ((BaseFrameFrag) context).startActivityWithCallback(intent, resultListener);
                }
            }

            @Override
            public void onDenied(String permission) {
                ToastUtil.toastShort("请授予本程序SD卡写入权限和相机权限！");
            }
        });
        return savePath;
    }
}
