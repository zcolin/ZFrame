/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.fosung.frame.imageloader;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.Util;
import com.fosung.frame.app.BaseApp;

import java.io.File;


/**
 * 所有函数的Context
 * 可以传入Context、Fragment、FragmentActivity, Activity，函数会自定判断
 * <p>
 * 如果传入的是Context或者fragment，会根据生命周期暂停/重新加载
 */
public class ImageLoaderUtils {

    public static void displayImage(Object context, Uri uri, ImageView iv) {
        getRequestManager(context)
                .load(uri)
                .into(iv);
    }

    public static void displayImage(Object context, File file, ImageView iv) {
        getRequestManager(context)
                .load(file)
                .into(iv);
    }

    public static void displayImage(Object context, int res, ImageView iv) {
        getRequestManager(context)
                .load(res)
                .into(iv);
    }

    public static void displayImage(Object context, String url, ImageView iv) {
        getRequestManager(context)
                .load(url)
                .into(iv);
    }

    /**
     * 设置有加载动画
     */
    public static void displayImageWithAnim(Object context, String url, ImageView iv, int anim) {
        getRequestManager(context)
                .load(url)
                .animate(anim)
                .into(iv);
    }

    /**
     * 显示为圆角图图片
     */
    public static void displayRoundCornersImage(Object context, String url, ImageView iv, int corner) {
        getRequestManager(context)
                .load(url)
                .asBitmap()
                .transform(new RoundedCornersTransformation(BaseApp.APP_CONTEXT, corner))
                .into(iv);
    }

    /**
     * 显示为圆角图图片
     */
    public static void displayRoundCornersImageWithPlaceHolder(Object context, String url, ImageView iv, int corner, int placeHolder) {
        getRequestManager(context)
                .load(url)
                .asBitmap()
                .placeholder(placeHolder)
                .transform(new RoundedCornersTransformation(BaseApp.APP_CONTEXT, corner))
                .into(iv);
    }

    /**
     * 显示为圆形图片
     */
    public static void displayCircleImage(Object context, String url, ImageView iv) {
        getRequestManager(context)
                .load(url)
                .transform(new CircleTransform((Context) context))
                .into(iv);
    }

    /**
     * 显示为圆形图片
     */
    public static void displayCircleImageWithPlaceholder(Object context, String url, ImageView iv, int placeholder) {
        getRequestManager(context)
                .load(url)
                .placeholder(placeholder)
                .transform(new CircleTransform((Context) context))
                .into(iv);
    }

    /**
     * 加载过程中设置有加载中图片
     */
    public static void displayImageWithPlaceholder(Object context, String url, ImageView iv, int placeholder) {
        getRequestManager(context)
                .load(url)
                .placeholder(placeholder)
                .error(placeholder)
                .into(iv);
    }


    /**
     * 加载过程中显示缩略图，全部加载完毕再显示原图，缩略大小为10%
     */
    public static void displayImageWithThumbnails(Object context, String url, ImageView imv) {
        getRequestManager(context)
                .load(url)
                .thumbnail(0.1f)
                .into(imv);
    }

    /**
     * 加载过程中显示缩略图，全部加载完毕再显示原图
     */
    public static void displayImageWithThumbnails(Object context, String url, ImageView imv, float sizeMultiplier) {
        getRequestManager(context)
                .load(url)
                .thumbnail(sizeMultiplier)
                .into(imv);
    }

    private static RequestManager getRequestManager(Object context) {
        if (context == null) {
            throw new IllegalArgumentException("You cannot start a load on a null Context");
        } else if (Util.isOnMainThread() && !(context instanceof Application)) {
            if (context instanceof FragmentActivity) {
                return Glide.with(((FragmentActivity) context));
            } else if (context instanceof Activity) {
                return Glide.with(((Activity) context));
            } else if (context instanceof Fragment) {
                return Glide.with(((Fragment) context));
            } else if (context instanceof android.app.Fragment) {
                return Glide.with((((android.app.Fragment) context)));
            }else if (context instanceof ContextWrapper) {
                return Glide.with((((ContextWrapper) context).getBaseContext()));
            }
        }
        return Glide.with((Context) context);
    }
}
   