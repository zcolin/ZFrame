/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-10-24 下午4:53
 * ********************************************************
 */

package com.zcolin.frame.imageloader;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Util;

import java.util.HashMap;


/**
 * 所有函数的Context
 * 可以传入Context、Fragment、FragmentActivity, Activity，函数会自定判断
 * <p>
 * 如果传入的是Context或者fragment，会根据生命周期暂停/重新加载
 * <p>
 * 特殊需求直接使用Glide
 */
public class ImageLoaderUtils {
    private static HashMap<Object, RequestOptions> mapRequestOptions = new HashMap<>();

    public static RequestOptions getRequestOption(int placeHolder) {
        RequestOptions requestOptions = mapRequestOptions.get(placeHolder);
        if (requestOptions == null) {
            requestOptions = new RequestOptions().placeholder(placeHolder);
            mapRequestOptions.put(placeHolder, requestOptions);
        }
        return requestOptions;
    }

    public static RequestOptions getRequestOption(int placeHolder, int error) {
        RequestOptions requestOptions = mapRequestOptions.get(placeHolder + "-" + error);
        if (requestOptions == null) {
            requestOptions = new RequestOptions().placeholder(placeHolder)
                                                 .error(error);
            mapRequestOptions.put(placeHolder + "-" + error, requestOptions);
        }
        return requestOptions;
    }

    public static RequestOptions getRequestOption(Drawable placeHolder) {
        RequestOptions requestOptions = mapRequestOptions.get(placeHolder);
        if (requestOptions == null) {
            requestOptions = new RequestOptions().placeholder(placeHolder);
            mapRequestOptions.put(placeHolder, requestOptions);
        }
        return requestOptions;
    }

    public static RequestOptions getRoundCornerRequestOption(int corner) {
        String key = "RoundCornerRequestOption" + corner;
        RequestOptions requestOptions = mapRequestOptions.get(key);
        if (requestOptions == null) {
            requestOptions = new RequestOptions().transform(new RoundedCornersTransformation(corner));
            mapRequestOptions.put(key, requestOptions);
        }
        return requestOptions;
    }

    public static RequestOptions getRoundCornerRequestOption(int corner, int placeHolder) {
        String key = "RoundCornerRequestOption" + corner + "holder" + placeHolder;
        RequestOptions requestOptions = mapRequestOptions.get(key);
        if (requestOptions == null) {
            requestOptions = new RequestOptions().transform(new RoundedCornersTransformation(corner))
                                                 .placeholder(placeHolder);
            mapRequestOptions.put(key, requestOptions);
        }
        return requestOptions;
    }

    public static RequestOptions getRoundCornerRequestOption(int corner, int placeHolder, int error) {
        String key = "RoundCornerRequestOption" + corner + "holder" + placeHolder + "error" + error;
        RequestOptions requestOptions = mapRequestOptions.get(key);
        if (requestOptions == null) {
            requestOptions = new RequestOptions().transform(new RoundedCornersTransformation(corner))
                                                 .placeholder(placeHolder);
            mapRequestOptions.put(key, requestOptions);
        }
        return requestOptions;
    }

    public static RequestOptions getCircleRequestOption() {
        String key = "CircleRequestOption";
        RequestOptions requestOptions = mapRequestOptions.get(key);
        if (requestOptions == null) {
            requestOptions = new RequestOptions().transform(new CircleTransform());
            mapRequestOptions.put(key, requestOptions);
        }
        return requestOptions;
    }

    public static RequestOptions getCircleRequestOption(int placeHolder) {
        String key = "CircleRequestOption" + "holder" + placeHolder;
        RequestOptions requestOptions = mapRequestOptions.get(key);
        if (requestOptions == null) {
            requestOptions = new RequestOptions().transform(new CircleTransform())
                                                 .placeholder(placeHolder);
            mapRequestOptions.put(key, requestOptions);
        }
        return requestOptions;
    }

    public static RequestOptions getCircleRequestOption(int placeHolder, int error) {
        String key = "CircleRequestOption" + "holder" + placeHolder + "error" + error;
        RequestOptions requestOptions = mapRequestOptions.get(key);
        if (requestOptions == null) {
            requestOptions = new RequestOptions().transform(new CircleTransform())
                                                 .placeholder(placeHolder);
            mapRequestOptions.put(key, requestOptions);
        }
        return requestOptions;
    }

    /**
     * @param context 传入Context、Fragment、FragmentActivity、Activity四项的实体及其子类 否则抛出异常
     * @param uri     传入Uri、File、res（int）、url(String), 否则抛出异常
     */
    public static <T, Z> void displayImage(T context, Z uri, ImageView iv) {
        try {
            getRequestManager(context).load(uri)
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayImage(T context, Z uri, ImageView iv, int placeHolder) {
        try {
            getRequestManager(context).load(uri)
                                      .apply(getRequestOption(placeHolder))
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayImage(T context, Z uri, ImageView iv, int placeHolder, int error) {
        try {
            getRequestManager(context).load(uri)
                                      .apply(getRequestOption(placeHolder, error))
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayImage(T context, Z uri, ImageView iv, Drawable placeHolder) {
        try {
            getRequestManager(context).load(uri)
                                      .apply(getRequestOption(placeHolder))
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayImage(T context, Z uri, ImageView iv, RequestOptions requestOptions) {
        try {
            getRequestManager(context).load(uri)
                                      .apply(requestOptions)
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayImageWithAnim(T context, Z uri, ImageView iv, int anim, int placeHolder) {
        try {
            getRequestManager(context).load(uri)
                                      .transition(new DrawableTransitionOptions().transition(anim))
                                      .apply(getRequestOption(placeHolder))
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayImageWithAnim(T context, Z uri, ImageView iv, int anim, int placeHolder, int error) {
        try {
            getRequestManager(context).load(uri)
                                      .transition(new DrawableTransitionOptions().transition(anim))
                                      .apply(getRequestOption(placeHolder, error))
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayImageWithAnim(T context, Z uri, ImageView iv, int anim, Drawable placeHolder) {
        try {
            getRequestManager(context).load(uri)
                                      .transition(new DrawableTransitionOptions().transition(anim))
                                      .apply(getRequestOption(placeHolder))
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayImageWithAnim(T context, Z uri, ImageView iv, int anim, RequestOptions requestOptions) {
        try {
            getRequestManager(context).load(uri)
                                      .transition(new DrawableTransitionOptions().transition(anim))
                                      .apply(requestOptions)
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static <T, Z> void displayRoundCornersImage(T context, Z uri, ImageView iv, int corner) {
        try {
            getRequestManager(context).asBitmap()
                                      .load(uri)
                                      .apply(getRoundCornerRequestOption(corner))
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayRoundCornersImage(T context, Z uri, ImageView iv, int corner, int placeHolder) {
        try {
            getRequestManager(context).asBitmap()
                                      .load(uri)
                                      .apply(getRoundCornerRequestOption(corner, placeHolder))
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayRoundCornersImage(T context, Z uri, ImageView iv, int corner, int placeHolder, int error) {
        try {
            getRequestManager(context).asBitmap()
                                      .load(uri)
                                      .apply(getRoundCornerRequestOption(corner, placeHolder, error))
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayCircleImage(T context, Z uri, ImageView iv) {
        try {
            getRequestManager(context).asBitmap()
                                      .load(uri)
                                      .apply(getCircleRequestOption())
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayCircleImage(T context, Z uri, ImageView iv, int placeHolder) {
        try {
            getRequestManager(context).asBitmap()
                                      .load(uri)
                                      .apply(getCircleRequestOption(placeHolder))
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayCircleImage(T context, Z uri, ImageView iv, int placeHolder, int error) {
        try {
            getRequestManager(context).asBitmap()
                                      .load(uri)
                                      .apply(getCircleRequestOption(placeHolder, error))
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayImageWithThumbnails(T context, Z uri, ImageView iv, float sizeMultiplier) {
        try {
            getRequestManager(context).load(uri)
                                      .thumbnail(sizeMultiplier)
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayImageWithThumbnails(T context, Z uri, ImageView iv, float sizeMultiplier, int placeHolder) {
        try {
            getRequestManager(context).load(uri)
                                      .thumbnail(sizeMultiplier)
                                      .apply(getRequestOption(placeHolder))
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayImageWithThumbnails(T context, Z uri, ImageView iv, float sizeMultiplier, int placeHolder, int error) {
        try {
            getRequestManager(context).load(uri)
                                      .thumbnail(sizeMultiplier)
                                      .apply(getRequestOption(placeHolder, error))
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayImageWithThumbnails(T context, Z uri, ImageView iv, float sizeMultiplier, Drawable placeHolder) {
        try {
            getRequestManager(context).load(uri)
                                      .thumbnail(sizeMultiplier)
                                      .apply(getRequestOption(placeHolder))
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, Z> void displayImageWithThumbnails(T context, Z uri, ImageView iv, float sizeMultiplier, RequestOptions requestOptions) {
        try {
            getRequestManager(context).load(uri)
                                      .thumbnail(sizeMultiplier)
                                      .apply(requestOptions)
                                      .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> RequestManager getRequestManager(T context) {
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
            } else if (context instanceof ContextWrapper) {
                return Glide.with((((ContextWrapper) context).getBaseContext()));
            }
        }
        return Glide.with((Context) context);
    }
}
   