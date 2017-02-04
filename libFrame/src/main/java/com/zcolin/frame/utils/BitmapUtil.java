/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-20 下午5:34
 * *********************************************************
 */

/*    
 * 
 * @author		: WangLin  
 * @Company: 	：FCBN
 * @date		: 2015年5月13日 
 * @version 	: V1.0
 */
package com.zcolin.frame.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片操作工具类
 */
public class BitmapUtil {
    /**
     * 存储图片
     *
     * @param tagPath 目标路径
     */
    public static void savePic(Bitmap bitmap, String tagPath) {
        File file = new File(tagPath);
        FileUtil.checkFilePath(file, false);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 存储图片
     *
     * @param tagPath 目标路径
     */
    public static void savePic(Bitmap bitmap, String tagPath, int width, int height) {
        Bitmap map = null;
        if (width < 0 || height < 0) {
            map = decodeBitmap(bitmapToInputStream(bitmap));
        } else {
            map = decodeBitmap(bitmapToInputStream(bitmap), width, height);
        }

        File file = new File(tagPath);
        FileUtil.checkFilePath(file, false);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拷贝图片，拷贝过程可以进行缩放
     *
     * @param sourcePath 源路径
     * @param tagPath    目标路径
     * @param width      缩放到宽度， 小于0不缩放
     * @param height     缩放到高度， 小于0不缩放
     */
    public static void copyPic(String sourcePath, String tagPath, int width, int height) {
        Bitmap map = null;
        if (width < 0 || height < 0) {
            map = decodeBitmap(sourcePath);
        } else {
            map = decodeBitmap(sourcePath, width, height);
        }

        File file = new File(tagPath);
        FileUtil.checkFilePath(file, false);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            map.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (map != null && !map.isRecycled()) {
                map.recycle();
            }
        }
    }


    /**
     * 图片缩放（不按比例拉伸）
     *
     * @param resID     源图片资源
     * @param newWidth  缩放后宽度
     * @param newHeight 缩放后高度
     * @return 缩放后的Bitmap对象
     *
     * @deprecated 此方法占用内存较高，慎用
     */
    public static Bitmap zoomImage(Resources res, int resID, int newWidth, int newHeight) {
        Bitmap map = BitmapFactory.decodeResource(res, resID);
        return Bitmap.createScaledBitmap(map, newWidth, newHeight, false);
    }

    /**
     * 图片缩放（不按比例拉伸）
     *
     * @param width  缩放后宽度
     * @param height 缩放后高度
     * @return 缩放后的Bitmap对象
     *
     * @deprecated 此方法占用内存较高，慎用
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    /**
     * 图片缩放（等比缩放）
     *
     * @param resID  源图片资源
     * @param width  缩放后宽度
     * @param height 缩放后高度
     * @return 缩放后的Bitmap对象
     */
    public static Bitmap decodeBitmap(Resources res, int resID, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 设为true，BitmapFactory.decodeFile(Stringpath, Options opt)并不会真的返回一个Bitmap给你，仅会把它的宽，高取回
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resID, options);

        // 计算缩放比例
        options.inSampleSize = calculateOriginal(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resID, options);
    }

    /**
     * 根据文件路径返回Bitmap
     *
     * @param fileName 文件路径
     * @return Bitmap对象
     */
    public static Bitmap decodeBitmap(String fileName) {
        return BitmapFactory.decodeFile(fileName);
    }

    /**
     * 图片缩放（等比缩放）
     *
     * @param fileName 图片文件路径
     */
    public static Bitmap decodeBitmap(String fileName, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 设为true，BitmapFactory.decodeFile(Stringpath, Options opt)并不会真的返回一个Bitmap给你，仅会把它的宽，高取回
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);

        // 计算缩放比例
        options.inSampleSize = calculateOriginal(options, width, height);
        options.inJustDecodeBounds = false;
        System.out.println("samplesize:" + options.inSampleSize);
        return BitmapFactory.decodeFile(fileName, options);
    }

    /**
     * 图片缩放ImageView大小
     *
     * @param fileName 图片文件路径
     * @return 缩放后的图片
     */
    public static Bitmap decodeBitmap(String fileName, ImageView imageView) {
        LayoutParams lay = imageView.getLayoutParams();
        int width = lay.width;
        int height = lay.height;
        final BitmapFactory.Options options = new BitmapFactory.Options();

        // 设为true，BitmapFactory.decodeFile(Stringpath, Options opt)并不会真的返回一个Bitmap给你，仅会把它的宽，高取回
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);

        // 计算缩放比例
        options.inSampleSize = calculateOriginal(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(fileName, options);
    }

    /**
     * 根据文件路径返回Bitmap
     */
    public static Bitmap decodeBitmap(byte[] arrayByte) {
        return BitmapFactory.decodeByteArray(arrayByte, 0, arrayByte.length);
    }

    /**
     * 图片缩放（等比缩放）
     */
    public static Bitmap decodeBitmap(byte[] arrayByte, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 设为true，BitmapFactory.decodeFile(Stringpath, Options opt)并不会真的返回一个Bitmap给你，仅会把它的宽，高取回
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(arrayByte, 0, arrayByte.length, options);

        // 计算缩放比例
        options.inSampleSize = calculateOriginal(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(arrayByte, 0, arrayByte.length, options);
    }

    /**
     * 根据文件路径返回Bitmap
     */
    public static Bitmap decodeBitmap(InputStream inputStream) {
        return BitmapFactory.decodeStream(inputStream, null, new BitmapFactory.Options());
    }

    /**
     * 图片缩放（等比缩放）
     */
    public static Bitmap decodeBitmap(InputStream inputStream, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 设为true，BitmapFactory.decodeFile(Stringpath, Options opt)并不会真的返回一个Bitmap给你，仅会把它的宽，高取回
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);

        // 计算缩放比例
        options.inSampleSize = calculateOriginal(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(inputStream, null, options);
    }
    
    /**
     * 计算图片缩放比例,只能缩放2的指数
     *
     * @param reqWidth  缩放后的宽度
     * @param reqHeight 缩放后的高度
     * @return 计算的缩放比例
     */
    private static int calculateOriginal(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //TODO 如果出现height或者width为-1的情况，请参考以下进行调整
//        if (options.outHeight == -1 || options.outWidth == -1) {
//            try {
//                ExifInterface exifInterface = new ExifInterface(imagePath);
//                int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
//                int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
//                Log.i(TAG, "exif height: " + height);
//                Log.i(TAG, "exif width: " + width);
//                options.outWidth = width;
//                options.outHeight = height;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        options.inSampleSize = calculateInSampleSize(options, requestWidth, requestHeight); //计算获取新的采样率

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            long totalPixels = width/ inSampleSize * height / inSampleSize;
            final long totalReqPixelsCap = reqWidth * reqHeight;
            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 4;
            }
        }
        return inSampleSize;
    }

    /**
     * 获取圆角图片
     *
     * @param roundPx 圆角度数
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 获取带倒影的图片
     */
    private static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w, h / 2, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);
        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight() + reflectionGap, paint);
        return bitmapWithReflection;
    }

    /**
     * 收缩图片
     */
    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(null, newbmp);
    }

    /**
     * View显示的内容 转换为bitmap
     */
    public static Bitmap viewToBitmap(View v) {
        v.setDrawingCacheEnabled(true);
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false); // clear drawing cache
        return b;
    }

    /**
     * Bitmap对象转为Drawable对象
     *
     * @param bitmap bitmap对象
     * @return Drawable对象
     */
    public static Drawable bitmapToDrawable(Resources res, Bitmap bitmap) {
        return new BitmapDrawable(res, bitmap);
    }

    /**
     * drawable转bitMap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * BitMap2Byte
     */
    public static InputStream bitmapToInputStream(Bitmap bitmap) {
        return new ByteArrayInputStream(bitmapToByte(bitmap));
    }

    /**
     * BitMap2Byte
     */
    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /**
     * Byte2BitMap
     */
    public static Bitmap byteToBitmap(byte[] temp) {
        if (temp != null) {
            return BitmapFactory.decodeByteArray(temp, 0, temp.length);
        } else {
            return null;
        }
    }
}
