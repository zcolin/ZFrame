/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-9-6 上午9:29
 * ********************************************************
 */

package com.zcolin.frame.imageloader;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.module.GlideModule;
import com.zcolin.frame.app.FramePathConst;

import java.io.File;

/**
 * 创建一个额外的类去定制 Glide。
 * 在 AndroidManifest.xml 的 application 标签内去声明这个SimpleGlideModule。
 */
public class SimpleGlideModule implements GlideModule {
    public static DiskCache cache;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 在 Android 中有两个主要的方法对图片进行解码：ARGB8888 和 RGB565。前者为每个像素使用了 4 个字节，
        // 后者仅为每个像素使用了 2 个字节。ARGB8888 的优势是图像质量更高以及能存储一个 alpha 通道。
        // Picasso 使用 ARGB8888，Glide 默认使用低质量的 RGB565。
        // 对于 Glide 使用者来说：你使用 Glide module 方法去改变解码规则。
        //  builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        //设置缓存目录
        File cacheDir = new File(FramePathConst.getInstance()
                                               .getPathImgCache());
        cache = DiskLruCacheWrapper.get(cacheDir, 100 * 1024 * 1024);// 100 MB 
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                return cache;
            }
        });

        //        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        //        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

    }
}