/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-21 下午1:53
 * *********************************************************
 */

package com.fosung.usedemo.http;

import com.fosung.frame.http.okhttp.OkHttpUtils;
import com.fosung.frame.http.response.FileResponse;
import com.fosung.frame.http.response.StringResponse;
import com.fosung.usedemo.http.entity.HttpBaseReplyBean;
import com.fosung.usedemo.http.entity.HttpCommonReply;
import com.fosung.usedemo.http.response.ZResponse;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Http工具类, Http请求封装类
 * <p/>
 * 所有函数的返回值为http请求的唯一标识，可以使用此标识调用{@link HttpUtil#cancelRequest(String)}取消请求
 */
public class HttpUtil {

    /**
     * 普通get请求，回调返回字符串
     */
    public static String get(String url, StringResponse response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.get()
                   .url(url)
                   .tag(cancelTag)
                   .build()
                   .execute(response);
        return cancelTag;
    }

    /**
     * 带有参数的普通get请求，回调返回字符串
     */
    public static String get(String url, Map<String, String> contentParams, StringResponse response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.get()
                   .url(url)
                   .params(contentParams)
                   .tag(cancelTag)
                   .build()
                   .execute(response);
        return cancelTag;
    }

    /**
     * 带有参数的普通get请求，回调返回字符串
     */
    public static String get(String url, Object contentParams, StringResponse response) {
        return get(url, beanToMap(contentParams), response);
    }


    /**
     * 带有header的普通get请求，回调返回字符串
     */
    public static String get(String url, LinkedHashMap<String, String> headerParams, Map<String, String> contentParams, StringResponse response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.get()
                   .url(url)
                   .headers(headerParams)
                   .params(contentParams)
                   .tag(cancelTag)
                   .build()
                   .execute(response);
        return cancelTag;
    }

    /**
     * 带有参数的普通get请求，回调返回字符串
     */
    public static String get(String url, LinkedHashMap<String, String> headerParams, Object contentParams, StringResponse response) {
        return get(url, headerParams, beanToMap(contentParams), response);
    }


    /**
     * 普通get请求，回调返回字符串
     */
    public static <T extends HttpBaseReplyBean> String get(String url, ZResponse<T> response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.get()
                   .url(url)
                   .tag(cancelTag)
                   .build()
                   .execute(response.generatedProxy());
        return cancelTag;
    }

    /**
     * 带有参数的普通get请求，回调返回字符串
     */
    public static <T extends HttpBaseReplyBean> String get(String url, Map<String, String> contentParams, ZResponse<T> response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.get()
                   .url(url)
                   .params(contentParams)
                   .tag(cancelTag)
                   .build()
                   .execute(response.generatedProxy());
        return cancelTag;
    }

    /**
     * 带有参数的普通get请求，回调返回字符串
     */
    public static <T extends HttpBaseReplyBean> String get(String url, Object contentParams, ZResponse<T> response) {
        return get(url, beanToMap(contentParams), response);
    }


    /**
     * 带有header的普通get请求，回调返回字符串
     */
    public static <T extends HttpBaseReplyBean> String get(String url, LinkedHashMap<String, String> headerParams, Map<String, String> contentParams, ZResponse<T> response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.get()
                   .url(url)
                   .headers(headerParams)
                   .params(contentParams)
                   .tag(cancelTag)
                   .build()
                   .execute(response.generatedProxy());
        return cancelTag;
    }

    /**
     * 带有参数的普通get请求，回调返回字符串
     */
    public static <T extends HttpBaseReplyBean> String get(String url, LinkedHashMap<String, String> headerParams, Object contentParams, ZResponse<T> response) {
        return get(url, headerParams, beanToMap(contentParams), response);
    }

    /**
     * 带有参数的普通post请求，回调返回字符串
     */
    public static String post(String url, Map<String, String> contentParams, StringResponse response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.post()
                   .url(url)
                   .params(contentParams)
                   .tag(cancelTag)
                   .build()
                   .execute(response);
        return cancelTag;
    }

    /**
     * 带有参数的普通post请求，回调返回字符串
     */
    public static String post(String url, Object contentParams, StringResponse response) {
        return post(url, beanToMap(contentParams), response);
    }

    /**
     * 带有header的普通get请求，回调返回字符串
     */
    public static String post(String url, LinkedHashMap<String, String> headerParams, Map<String, String> contentParams, StringResponse response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.post()
                   .url(url)
                   .headers(headerParams)
                   .params(contentParams)
                   .tag(cancelTag)
                   .build()
                   .execute(response);
        return cancelTag;
    }

    /**
     * 带有参数的普通post请求，回调返回字符串
     */
    public static String post(String url, LinkedHashMap<String, String> headerParams, Object contentParams, StringResponse response) {
        return post(url, headerParams, beanToMap(contentParams), response);
    }

    /**
     * 带有参数的普通post请求，回调返回字符串
     */
    public static <T extends HttpBaseReplyBean> String post(String url, Map<String, String> contentParams, ZResponse<T> response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.post()
                   .url(url)
                   .params(contentParams)
                   .tag(cancelTag)
                   .build()
                   .execute(response.generatedProxy());
        return cancelTag;
    }


    /**
     * 带有参数的普通post请求，回调返回字符串
     */
    public static <T extends HttpBaseReplyBean> String post(String url, Object contentParams, ZResponse<T> response) {
        return post(url, beanToMap(contentParams), response);
    }

    /**
     * 带有header的普通post请求，回调返回字符串
     */
    public static <T extends HttpBaseReplyBean> String post(String url, LinkedHashMap<String, String> headerParams, Map<String, String> contentParams, ZResponse<T> response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.post()
                   .url(url)
                   .headers(headerParams)
                   .params(contentParams)
                   .tag(cancelTag)
                   .build()
                   .execute(response.generatedProxy());
        return cancelTag;
    }

    /**
     * 带有参数的普通post请求，回调返回字符串
     */
    public static <T extends HttpBaseReplyBean> String post(String url, LinkedHashMap<String, String> headerParams, Object contentParams, ZResponse<T> response) {
        return post(url, headerParams, beanToMap(contentParams), response);
    }


    /**
     * 上传文件
     */
    public static String uploadFile(String url, String key, File file, StringResponse response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.post()
                   .url(url)
                   .addFile(key, file)
                   .tag(cancelTag)
                   .build()
                   .execute(response);
        return cancelTag;
    }


    /**
     * 上传文件
     */
    public static String uploadFile(String url, Map<String, File> fileParams, StringResponse response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.post()
                   .url(url)
                   .files(fileParams)
                   .tag(cancelTag)
                   .build()
                   .execute(response);
        return cancelTag;
    }

    /**
     * 上传文件
     */
    public static String uploadFile(String url, String key, File file, ZResponse<HttpCommonReply> response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.post()
                   .url(url)
                   .addFile(key, file)
                   .tag(cancelTag)
                   .build()
                   .execute(response.generatedProxy());
        return cancelTag;
    }

    /**
     * 上传文件
     */
    public static String uploadFile(String url, Map<String, String> contentParams, Map<String, File> fileParams, ZResponse<HttpCommonReply> response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.post()
                   .url(url)
                   .params(contentParams)
                   .files(fileParams)
                   .tag(cancelTag)
                   .build()
                   .execute(response.generatedProxy());
        return cancelTag;
    }


    /**
     * 上传文件
     */
    public static String uploadFile(String url, Map<String, String> contentParams, String fileKey, File fileValue, ZResponse<HttpCommonReply> response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.post()
                   .url(url)
                   .params(contentParams)
                   .addFile(fileKey, fileValue)
                   .tag(cancelTag)
                   .build()
                   .execute(response.generatedProxy());
        return cancelTag;
    }


    /**
     * 上传文件
     */
    public static String uploadFile(String url, Map<String, File> contentParams, ZResponse<HttpCommonReply> response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.post()
                   .url(url)
                   .files(contentParams)
                   .tag(cancelTag)
                   .build()
                   .execute(response.generatedProxy());
        return cancelTag;
    }

    /**
     * 上传文件
     */
    public static String uploadFile(String url, String fileKey, File[] fileValue, ZResponse<HttpCommonReply> response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.post()
                   .url(url)
                   .files(fileKey, fileValue)
                   .tag(cancelTag)
                   .build()
                   .execute(response.generatedProxy());
        return cancelTag;
    }

    /**
     * 上传文件
     */
    public static String uploadFile(String url, Map<String, String> contentParams, String fileKey, File[] fileValue, ZResponse<HttpCommonReply> response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.post()
                   .url(url)
                   .params(contentParams)
                   .files(fileKey, fileValue)
                   .tag(cancelTag)
                   .build()
                   .execute(response.generatedProxy());
        return cancelTag;
    }

    /**
     * 上传文件
     */
    public static String uploadFileWithHeadr(String url, LinkedHashMap<String, String> headers, Map<String, File> contentParams, StringResponse response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.post()
                   .url(url)
                   .headers(headers)
                   .files(contentParams)
                   .tag(cancelTag)
                   .build()
                   .execute(response);
        return cancelTag;
    }

    /**
     * 上传文件
     */
    public static String uploadFileWithHeadr(String url, LinkedHashMap<String, String> headers, Map<String, File> contentParams, ZResponse<HttpCommonReply> response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.post()
                   .url(url)
                   .headers(headers)
                   .files(contentParams)
                   .tag(cancelTag)
                   .build()
                   .execute(response.generatedProxy());
        return cancelTag;
    }

    /**
     * 下载文件
     */
    public static String downLoadFile(String url, FileResponse response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.post()
                   .url(url)
                   .tag(cancelTag)
                   .build()
                   .execute(response);
        return cancelTag;
    }

    /**
     * 下载文件
     */
    public static String downLoadFile(String url, LinkedHashMap<String, String> headers, FileResponse response) {
        String cancelTag = UUID.randomUUID()
                               .toString();
        OkHttpUtils.post()
                   .url(url)
                   .headers(headers)
                   .tag(cancelTag)
                   .build()
                   .execute(response);
        return cancelTag;
    }

    /**
     * 取消请求
     */
    public static void cancelRequest(String tag) {
        OkHttpUtils.getInstance()
                   .cancelTag(tag);
    }


    /**
     * getFileds获取所有public 属性<br/>
     * getDeclaredFields 获取所有声明的属性<br/>
     * <p>
     * 所有属性必须为public，不能有list、对象等字段
     *
     * @return 将某个类及其继承属性全部添加到Map中
     */
    private static Map<String, String> beanToMap(Object bean) {
        if (bean == null) {
            return null;
        }

        Map<String, String> result = new HashMap<String, String>();
        Field[] fields = bean.getClass()
                             .getFields();
        if (fields == null || fields.length == 0) {
            return result;
        }

        for (Field field : fields) {
            //获取属性名称及值存入Map  
            String key = field.getName();
            try {
                Object object = field.get(bean);
                if (object != null) {
                    String value = String.valueOf(field.get(bean));
                    result.put(key, value);
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        //获取父类属性  
        fields = bean.getClass()
                     .getSuperclass()
                     .getFields();
        if (fields == null || fields.length == 0) {
            return result;
        }

        for (Field field : fields) {
            //获取属性名称及值存入Map  
            String key = field.getName();
            try {
                Object object = field.get(bean);
                if (object != null) {
                    String value = String.valueOf(field.get(bean));
                    result.put(key, value);
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
