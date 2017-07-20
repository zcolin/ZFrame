/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.zcolin.frame.imageloader;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.zcolin.frame.app.BaseApp;


/**
 * 所有函数的Context
 * 可以传入Context、Fragment、FragmentActivity, Activity，函数会自定判断
 * <p>
 * 如果传入的是Context或者fragment，会根据生命周期暂停/重新加载
 * <p>
 * 特殊需求直接使用Glide
 */
public class ImageLoaderUtils {

    /**
     * @param context 传入Context、Fragment、FragmentActivity、Activity四项的实体及其子类 否则抛出异常
     * @param uri     传入Uri、File、res（int）、url(String), 否则抛出异常
     */
    public static <T, Z> void displayImage(T context, Z uri, ImageView iv) {
        getDrawableTypeRequest(context, uri)
                .into(iv);
    }

    public static <T, Z> void displayImage(T context, Z uri, ImageView iv, int placeHolder) {
        getDrawableTypeRequest(context, uri)
                .placeholder(placeHolder)
                .into(iv);
    }

    public static <T, Z> void displayImage(T context, Z uri, ImageView iv, Drawable placeHolder) {
        getDrawableTypeRequest(context, uri)
                .placeholder(placeHolder)
                .into(iv);
    }


    public static <T, Z> void displayImageWithAnim(T context, Z uri, ImageView iv, int anim, int placeHolder) {
        getDrawableTypeRequest(context, uri)
                .animate(anim)
                .placeholder(placeHolder)
                .into(iv);
    }

    public static <T, Z> void displayImageWithAnim(T context, Z uri, ImageView iv, int anim, Drawable placeHolder) {
        getDrawableTypeRequest(context, uri)
                .animate(anim)
                .placeholder(placeHolder)
                .into(iv);
    }

    public static <T, Z> void displayRoundCornersImage(T context, Z uri, ImageView iv, int corner) {
        getDrawableTypeRequest(context, uri)
                .asBitmap()
                .transform(new RoundedCornersTransformation(BaseApp.APP_CONTEXT, corner))
                .into(iv);
    }

    public static <T, Z> void displayRoundCornersImage(T context, Z uri, ImageView iv, int corner, int placeHolder) {
        getDrawableTypeRequest(context, uri)
                .asBitmap()
                .transform(new RoundedCornersTransformation(BaseApp.APP_CONTEXT, corner))
                .placeholder(placeHolder)
                .into(iv);
    }

    public static <T, Z> void displayRoundCornersImage(T context, Z uri, ImageView iv, int corner, Drawable placeHolder) {
        getDrawableTypeRequest(context, uri)
                .asBitmap()
                .transform(new RoundedCornersTransformation(BaseApp.APP_CONTEXT, corner))
                .placeholder(placeHolder)
                .into(iv);
    }

    public static <T, Z> void displayCircleImage(T context, Z uri, ImageView iv) {
        getDrawableTypeRequest(context, uri)
                .asBitmap()
                .transform(new CircleTransform(BaseApp.APP_CONTEXT))
                .into(iv);
    }

    public static <T, Z> void displayCircleImage(T context, Z uri, ImageView iv, int placeHolder) {
        getDrawableTypeRequest(context, uri)
                .asBitmap()
                .transform(new CircleTransform(BaseApp.APP_CONTEXT))
                .placeholder(placeHolder)
                .into(iv);
    }

    public static <T, Z> void displayCircleImage(T context, Z uri, ImageView iv, Drawable placeHolder) {
        getDrawableTypeRequest(context, uri)
                .asBitmap()
                .transform(new CircleTransform(BaseApp.APP_CONTEXT))
                .placeholder(placeHolder)
                .into(iv);
    }

    public static <T, Z> void displayImageWithThumbnails(T context, Z uri, ImageView iv, float sizeMultiplier) {
        getDrawableTypeRequest(context, uri)
                .thumbnail(sizeMultiplier)
                .into(iv);
    }

    public static <T, Z> void displayImageWithThumbnails(T context, Z uri, ImageView iv, float sizeMultiplier, int placeHolder) {
        getDrawableTypeRequest(context, uri)
                .thumbnail(sizeMultiplier)
                .placeholder(placeHolder)
                .into(iv);
    }

    public static <T, Z> void displayImageWithThumbnails(T context, Z uri, ImageView iv, float sizeMultiplier, Drawable placeHolder) {
        getDrawableTypeRequest(context, uri)
                .thumbnail(sizeMultiplier)
                .placeholder(placeHolder)
                .into(iv);
    }

    private static <T, Z> DrawableTypeRequest<Z> getDrawableTypeRequest(T context, Z uri) {
        if (context == null) {
            throw new IllegalArgumentException("You cannot start a load on a null Context");
        } else if (Util.isOnMainThread() && !(context instanceof Application)) {
            if (context instanceof FragmentActivity) {
                return Glide.with(((FragmentActivity) context))
                            .load(uri);
            } else if (context instanceof Activity) {
                return Glide.with(((Activity) context))
                            .load(uri);
            } else if (context instanceof Fragment) {
                return Glide.with(((Fragment) context))
                            .load(uri);
            } else if (context instanceof android.app.Fragment) {
                return Glide.with((((android.app.Fragment) context)))
                            .load(uri);
            } else if (context instanceof ContextWrapper) {
                return Glide.with((((ContextWrapper) context).getBaseContext()))
                            .load(uri);
            }
        }
        return Glide.with((Context) context)
                    .load(uri);
    }
}
   