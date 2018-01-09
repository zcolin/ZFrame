/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */

package com.zcolin.frame.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.zcolin.frame.app.BaseFrameActivity;
import com.zcolin.frame.app.BaseFrameFrag;
import com.zcolin.frame.app.FramePathConst;
import com.zcolin.frame.permission.PermissionHelper;
import com.zcolin.frame.permission.PermissionsResultAction;

import java.io.File;

import static com.zcolin.frame.util.NUriParseUtil.getImageContentUri;
import static com.zcolin.frame.util.NUriParseUtil.getUriFromPath;


/**
 * 调用系统Intent
 */
public class SystemIntentUtil {
    /**
     * 获取默认的照片路径
     * 名称为：TEMP_PATH/zz_takephoto_yyyy-MM-dd HH:mm:ss.jpg
     */
    public static String getTempTakePhotoPath() {
        return FramePathConst.getInstance().getPathTemp() + "zz_takephoto_" + CalendarUtil.getDateTime() + ".jpg";
    }

    /**
     * 调用系统拍照
     *
     * @param object             只能为BaseFrag或者BaseActivity的子类
     * @param savePath           拍照完后的保存路径
     * @param onCompleteLisenter 拍照完的回调函数接口
     */
    public static void takePhoto(final Object object, final String savePath, final OnCompleteLisenter onCompleteLisenter) {
        if (!SDCardUtil.isSDCardEnable()) {
            return;
        }

        PermissionHelper.requestPermission(object, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new 
                PermissionsResultAction() {
            @Override
            public void onGranted() {
                FileUtil.checkFilePath(savePath, false);
                final Uri uri = getUriFromPath(savePath);
                if (uri != null) {
                    final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    if (object instanceof BaseFrameActivity) {
                        ((BaseFrameActivity) object).startActivityWithCallback(intent, (resultCode, data) -> {
                            if (onCompleteLisenter != null) {
                                if (resultCode == Activity.RESULT_OK) {
                                    onCompleteLisenter.onSelected(uri);
                                } else {
                                    onCompleteLisenter.onCancel();
                                }
                            }
                        });
                    } else if (object instanceof BaseFrameFrag) {
                        ((BaseFrameFrag) object).startActivityWithCallback(intent, (resultCode, data) -> {
                            if (onCompleteLisenter != null) {
                                if (resultCode == Activity.RESULT_OK) {
                                    onCompleteLisenter.onSelected(uri);
                                } else {
                                    onCompleteLisenter.onCancel();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onDenied(String permission) {
                ToastUtil.toastShort("请授予本程序SD卡写入权限和相机权限！");
            }
        });
    }

    /**
     * 选取照片
     *
     * @param context 只能为BaseFrag或者BaseActivity的子类
     */
    public static void selectPhoto(final Object context, final OnCompleteLisenter onCompleteLisenter) {
        PermissionHelper.requestReadSdCardPermission(context, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                if (context instanceof BaseFrameActivity) {
                    ((BaseFrameActivity) context).startActivityWithCallback(intent, (resultCode, data) -> {
                        if (resultCode == Activity.RESULT_OK) {
                            onCompleteLisenter.onSelected(data.getData());
                        } else {
                            onCompleteLisenter.onCancel();
                        }

                    });
                } else if (context instanceof BaseFrameFrag) {
                    ((BaseFrameFrag) context).startActivityWithCallback(intent, (resultCode, data) -> {
                        if (resultCode == Activity.RESULT_OK) {
                            onCompleteLisenter.onSelected(data.getData());
                        } else {
                            onCompleteLisenter.onCancel();
                        }
                    });
                }
            }

            @Override
            public void onDenied(String permission) {
                ToastUtil.toastShort("请授予本程序读取SD卡权限");
            }
        });
    }


    public static void takePhotoWithCrop(final Object object, final String savePath, final int cropX, final int cropY,
            final OnCompleteLisenter onCompleteLisenter) {
        final String tempPath = FramePathConst.getInstance().getTempFilePath("jpg");
        takePhoto(object, tempPath, new OnCompleteLisenter() {
            @Override
            public void onSelected(Uri fileProviderUri) {
                SystemIntentUtil.cropPhoto(object, new File(tempPath), savePath, cropX, cropY, onCompleteLisenter);
            }

            @Override
            public void onCancel() {
                if (onCompleteLisenter != null) {
                    onCompleteLisenter.onCancel();
                }
            }
        });
    }

    public static void selectPhotoWithCrop(final Object object, final String savePath, final int cropX, final int cropY,
            final OnCompleteLisenter onCompleteLisenter) {
        selectPhoto(object, new OnCompleteLisenter() {
            @Override
            public void onSelected(Uri fileProviderUri) {
                SystemIntentUtil.cropPhoto(object, NUriParseUtil.getImageFileFromUri(fileProviderUri), savePath, cropX, cropY, onCompleteLisenter);
            }

            @Override
            public void onCancel() {
                if (onCompleteLisenter != null) {
                    onCompleteLisenter.onCancel();
                }
            }
        });
    }

    /**
     * 拍照后裁剪图片
     *
     * @param object             只能为BaseFrag或者BaseActivity的子类
     * @param savePath           裁剪完成后保存路径
     * @param file               需要裁剪的图片文件
     * @param cropX              x像素
     * @param cropY              y像素
     * @param onCompleteLisenter 完成后回调
     */
    public static void cropPhoto(final Object object, final File file, final String savePath, final int cropX, final int cropY,
            final OnCompleteLisenter onCompleteLisenter) {
        if (!SDCardUtil.isSDCardEnable()) {
            return;
        }

        PermissionHelper.requestReadWriteSdCardPermission(object, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                FileUtil.checkFilePath(savePath, false);
                final Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(getImageContentUri(file), "image/*");
                final Uri uri = Uri.fromFile(new File(savePath));
                if (uri != null) {
                    // 裁剪头像的绝对路径
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    intent.putExtra("crop", "true");
                    int[] arr = minScale(cropX, cropY);
                    intent.putExtra("aspectX", arr[0]);// 裁剪框比例
                    intent.putExtra("aspectY", arr[1]);
                    intent.putExtra("outputX", cropX);// 输出图片大小
                    intent.putExtra("outputY", cropY);
                    intent.putExtra("scale", true);// 去黑边
                    intent.putExtra("noFaceDetection", true);//去除默认的人脸识别，否则和剪裁匡重叠
                    intent.putExtra("scaleUpIfNeeded", true);// 去黑边
                    //是否要返回值。
                    //intent.putExtra("return-data", true);
                    if (object instanceof BaseFrameActivity) {
                        ((BaseFrameActivity) object).startActivityWithCallback(intent, (resultCode, data) -> {
                            if (onCompleteLisenter != null) {
                                if (resultCode == Activity.RESULT_OK) {
                                    onCompleteLisenter.onSelected(uri);
                                } else {
                                    onCompleteLisenter.onCancel();
                                }
                            }
                        });
                    } else if (object instanceof BaseFrameFrag) {
                        ((BaseFrameFrag) object).startActivityWithCallback(intent, (resultCode, data) -> {
                            if (onCompleteLisenter != null) {
                                if (resultCode == Activity.RESULT_OK) {
                                    onCompleteLisenter.onSelected(uri);
                                } else {
                                    onCompleteLisenter.onCancel();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onDenied(String permission) {
                ToastUtil.toastShort("请授予本程序读写SD卡权限");
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
        return FramePathConst.getInstance().getPathTemp() + "zz_videoCapture_" + CalendarUtil.getDateTime() + ".mp4";
    }

    /**
     * @param savePath      视频保存路径
     * @param durationLimit 最大时间限制 秒  为0不限制
     * @param isHighQuality 是否高质量
     * @return 视频保存路径
     */
    public static void videoCapture(final Object object, final String savePath, final int durationLimit, final boolean isHighQuality,
            final OnCompleteLisenter onCompleteLisenter) {
        PermissionHelper.requestPermission(object, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new 
                PermissionsResultAction() {
            @Override
            public void onGranted() {
                FileUtil.checkFilePath(savePath, false);
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                final Uri uri = getUriFromPath(savePath);
                if (uri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, isHighQuality ? 1 : 0);
                    if (durationLimit > 0) {
                        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, durationLimit);
                    }
                    if (object instanceof BaseFrameActivity) {
                        ((BaseFrameActivity) object).startActivityWithCallback(intent, (resultCode, data) -> {
                            if (onCompleteLisenter != null) {
                                if (resultCode == Activity.RESULT_OK) {
                                    onCompleteLisenter.onSelected(uri);
                                } else {
                                    onCompleteLisenter.onCancel();
                                }
                            }
                        });
                    } else if (object instanceof BaseFrameFrag) {
                        ((BaseFrameFrag) object).startActivityWithCallback(intent, (resultCode, data) -> {
                            if (onCompleteLisenter != null) {
                                if (resultCode == Activity.RESULT_OK) {
                                    onCompleteLisenter.onSelected(uri);
                                } else {
                                    onCompleteLisenter.onCancel();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onDenied(String permission) {
                ToastUtil.toastShort("请授予本程序SD卡写入权限和相机权限！");
            }
        });
        return;
    }

    public interface OnCompleteLisenter {
        /**
         * @param fileProviderUri FileProvider的Uri，如果要获取实际路径需要使用 {@link NUriParseUtil#getFileFromUri(String, Uri)}
         */
        void onSelected(Uri fileProviderUri);

        void onCancel();
    }
}
