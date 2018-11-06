/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */

package com.zcolin.frame.http;

import com.zcolin.frame.http.okhttp.OkHttpUtils;
import com.zcolin.frame.http.okhttp.builder.GetBuilder;
import com.zcolin.frame.http.okhttp.builder.OtherRequestBuilder;
import com.zcolin.frame.http.okhttp.builder.PostFormBuilder;
import com.zcolin.frame.http.response.ZFileResponse;
import com.zcolin.frame.http.response.ZResponse;
import com.zcolin.frame.http.response.ZStringResponse;
import com.zcolin.frame.util.GsonUtil;
import com.zcolin.frame.util.StringUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Http工具类, Http请求封装类
 * <p/>
 * 所有函数的返回值为http请求的唯一标识，可以使用此标识调用{@link ZHttp#cancelRequest(String...)}取消请求
 */
public class ZHttp {
    static ZRequestIntercept REQUESTINTERCEPT;
    static boolean           IS_CLEAR_EMPTY_VALUE;//清除为空的字段
    static boolean           IS_CLEAR_NULL_VALUE;//清除为null的字段

    /**
     * 设置请求拦截器
     */
    public static void setRequestIntercept(ZRequestIntercept intercept) {
        REQUESTINTERCEPT = intercept;
    }


    public static void setIsClearEmptyValue(boolean isClearEmptyValue) {
        IS_CLEAR_EMPTY_VALUE = isClearEmptyValue;
    }

    public static void setIsClearNullValue(boolean isClearNullValue) {
        IS_CLEAR_NULL_VALUE = isClearNullValue;
    }

    //--------------------------------------------------ZSResponse START----------------------------------------------------

    public static <T extends ZReply> String get(String url, ZResponse<T> response) {
        return get(url, null, response);
    }

    public static <T extends ZReply> String get(String url, Object contentParams, ZResponse<T> response) {
        return get(url, beanToMap(contentParams), response);
    }

    public static <T extends ZReply> String get(String url, Map<String, String> contentParams, ZResponse<T> response) {
        return getWithHeader(url, null, contentParams, response);
    }

    public static <T extends ZReply> String getWithHeader(String url, LinkedHashMap<String, String> headerParams, ZResponse<T> response) {
        return getWithHeader(url, headerParams, null, response);
    }

    public static <T extends ZReply> String getWithHeader(String url, LinkedHashMap<String, String> headerParams, Object contentParams, ZResponse<T> response) {
        return getWithHeader(url, headerParams, beanToMap(contentParams), response);
    }

    public static <T extends ZReply> String getWithHeader(String url, LinkedHashMap<String, String> headerParams, Map<String, String> contentParams,
            ZResponse<T> response) {
        String cancelTag = UUID.randomUUID().toString();
        removeNullValue(contentParams);
        if (REQUESTINTERCEPT == null || !REQUESTINTERCEPT.onRequest(url, headerParams, contentParams, response)) {
            OkHttpUtils.get().url(url).headers(headerParams).params(contentParams).tag(cancelTag).build().execute(response.generatedProxy());
        }
        return cancelTag;
    }


    public static <T extends ZReply> String post(String url, Map<String, String> contentParams, ZResponse<T> response) {
        return postWithHeader(url, null, contentParams, response);
    }


    public static <T extends ZReply> String post(String url, Object contentParams, ZResponse<T> response) {
        return post(url, beanToMap(contentParams), response);
    }

    public static <T extends ZReply> String postWithHeader(String url, LinkedHashMap<String, String> headerParams, Object contentParams,
            ZResponse<T> response) {
        return postWithHeader(url, headerParams, beanToMap(contentParams), response);
    }

    public static <T extends ZReply> String postWithHeader(String url, LinkedHashMap<String, String> headerParams, Map<String, String> contentParams,
            ZResponse<T> response) {
        String cancelTag = UUID.randomUUID().toString();
        removeNullValue(contentParams);
        if (REQUESTINTERCEPT == null || !REQUESTINTERCEPT.onRequest(url, headerParams, contentParams, response)) {
            OkHttpUtils.post().url(url).headers(headerParams).params(contentParams).tag(cancelTag).build().execute(response.generatedProxy());
        }
        return cancelTag;
    }
    //--------------------------------------------------ZSResponse END---------------------------------------------------

    //--------------------------------------------------PostString start---------------------------------------------------
    public static <T extends ZReply> String postString(String url, String string, ZResponse<T> response) {
        return postStringWithHeader(url, null, string, null, response);
    }

    public static <T extends ZReply> String postString(String url, String string, String mimeType, ZResponse<T> response) {
        return postStringWithHeader(url, null, string, mimeType, response);
    }

    public static <T extends ZReply> String postStringWithHeader(String url, LinkedHashMap<String, String> headerParams, String string, String mimeType,
            ZResponse<T> response) {
        String cancelTag = UUID.randomUUID().toString();
        if (REQUESTINTERCEPT == null || !REQUESTINTERCEPT.onRequest(url, headerParams, string, response)) {
            OkHttpUtils.postString()
                       .url(url)
                       .headers(headerParams)
                       .mimeType(mimeType)
                       .content(string)
                       .tag(cancelTag)
                       .build()
                       .execute(response.generatedProxy());
        }
        return cancelTag;
    }

    public static <T extends ZReply> String postJson(String url, String json, ZResponse<T> response) {
        return postJsonWithHeader(url, null, json, response);
    }

    public static <T extends ZReply> String postJson(String url, Object jsonObj, ZResponse<T> response) {
        return postJsonWithHeader(url, null, GsonUtil.beanToString(jsonObj), response);
    }

    public static <T extends ZReply> String postJsonWithHeader(String url, LinkedHashMap<String, String> headerParams, Object jsonObj, ZResponse<T> response) {
        return postJsonWithHeader(url, headerParams, GsonUtil.beanToString(jsonObj), response);
    }

    public static <T extends ZReply> String postJsonWithHeader(String url, LinkedHashMap<String, String> headerParams, String json, ZResponse<T> response) {
        String cancelTag = UUID.randomUUID().toString();
        String mimeType = "application/json; charset=utf-8";
        if (REQUESTINTERCEPT == null || !REQUESTINTERCEPT.onRequest(url, headerParams, json, response)) {
            OkHttpUtils.postString().url(url).headers(headerParams).mimeType(mimeType).content(json).tag(cancelTag).build().execute(response.generatedProxy());
        }
        return cancelTag;
    }
    //--------------------------------------------------PostString end---------------------------------------------------


    //--------------------------------------------------ZStringResponse Start----------------------------------------------------

    public static String get(String url, ZStringResponse response) {
        return get(url, null, response);
    }

    public static String get(String url, Object contentParams, ZStringResponse response) {
        return get(url, beanToMap(contentParams), response);
    }

    public static String get(String url, Map<String, String> contentParams, ZStringResponse response) {
        return getWithHeader(url, null, contentParams, response);
    }

    public static String getWithHeader(String url, LinkedHashMap<String, String> headerParams, ZStringResponse response) {
        return getWithHeader(url, headerParams, null, response);
    }

    public static String getWithHeader(String url, LinkedHashMap<String, String> headerParams, Object contentParams, ZStringResponse response) {
        return getWithHeader(url, headerParams, beanToMap(contentParams), response);
    }

    public static String getWithHeader(String url, LinkedHashMap<String, String> headerParams, Map<String, String> contentParams, ZStringResponse response) {
        String cancelTag = UUID.randomUUID().toString();
        removeNullValue(contentParams);
        if (REQUESTINTERCEPT == null || !REQUESTINTERCEPT.onRequest(url, headerParams, contentParams, response)) {
            OkHttpUtils.get().url(url).headers(headerParams).params(contentParams).tag(cancelTag).build().execute(response);
        }
        return cancelTag;
    }

    public static String post(String url, Map<String, String> contentParams, ZStringResponse response) {
        return postWithHeader(url, null, contentParams, response);
    }

    public static String post(String url, Object contentParams, ZStringResponse response) {
        return post(url, beanToMap(contentParams), response);
    }

    public static String postWithHeader(String url, LinkedHashMap<String, String> headerParams, Object contentParams, ZStringResponse response) {
        return postWithHeader(url, headerParams, beanToMap(contentParams), response);
    }

    public static String postWithHeader(String url, LinkedHashMap<String, String> headerParams, Map<String, String> contentParams, ZStringResponse response) {
        String cancelTag = UUID.randomUUID().toString();
        removeNullValue(contentParams);
        if (REQUESTINTERCEPT == null || !REQUESTINTERCEPT.onRequest(url, headerParams, contentParams, response)) {
            OkHttpUtils.post().url(url).headers(headerParams).params(contentParams).tag(cancelTag).build().execute(response);
        }
        return cancelTag;
    }

    public static <T extends ZReply> String postJson(String url, String json, ZStringResponse response) {
        return postJsonWithHeader(url, null, json, response);
    }

    public static <T extends ZReply> String postJson(String url, Object jsonObj, ZStringResponse response) {
        return postJson(url, GsonUtil.beanToString(jsonObj), response);
    }

    public static <T extends ZReply> String postJsonWithHeader(String url, LinkedHashMap<String, String> headerParams, Object jsonObj,
            ZStringResponse response) {
        return postJsonWithHeader(url, headerParams, GsonUtil.beanToString(jsonObj), response);
    }

    public static <T extends ZReply> String postJsonWithHeader(String url, LinkedHashMap<String, String> headerParams, String json, ZStringResponse response) {
        String cancelTag = UUID.randomUUID().toString();
        if (REQUESTINTERCEPT == null || !REQUESTINTERCEPT.onRequest(url, headerParams, json, response)) {
            OkHttpUtils.postString()
                       .url(url)
                       .headers(headerParams)
                       .mimeType("application/json; charset=utf-8")
                       .content(json)
                       .tag(cancelTag)
                       .build()
                       .execute(response);
        }
        return cancelTag;
    }

    //--------------------------------------------------ZStringResponse END----------------------------------------------------

    public static String uploadFile(String url, Map<String, File> fileParams, ZStringResponse response) {
        return uploadFile(url, null, fileParams, response);
    }

    public static String uploadFile(String url, Map<String, String> contentParams, Map<String, File> fileParams, ZStringResponse response) {
        return uploadFileWithHeader(url, null, contentParams, fileParams, response);
    }

    public static String uploadFile(String url, Object contentParams, Map<String, File> fileParams, ZStringResponse response) {
        return uploadFileWithHeader(url, null, contentParams, fileParams, response);
    }

    public static String uploadFileWithHeader(String url, LinkedHashMap<String, String> headers, Map<String, File> fileParams, ZStringResponse response) {
        return uploadFileWithHeader(url, headers, null, fileParams, response);
    }

    public static String uploadFileWithHeader(String url, LinkedHashMap<String, String> headers, Object contentParams, Map<String, File> fileParams,
            ZStringResponse response) {
        return uploadFileWithHeader(url, headers, beanToMap(contentParams), fileParams, response);
    }

    public static String uploadFileWithHeader(String url, LinkedHashMap<String, String> headers, Map<String, String> contentParams,
            Map<String, File> fileParams, ZStringResponse response) {
        String cancelTag = UUID.randomUUID().toString();
        removeNullValue(contentParams);
        if (REQUESTINTERCEPT == null || !REQUESTINTERCEPT.onRequest(url, headers, contentParams, response)) {
            OkHttpUtils.post().url(url).headers(headers).files(fileParams).params(contentParams).tag(cancelTag).build().execute(response);
        }
        return cancelTag;
    }

    public static <T extends ZReply> String uploadFile(String url, Map<String, File> fileParams, ZResponse<T> response) {
        return uploadFile(url, null, fileParams, response);
    }

    public static <T extends ZReply> String uploadFile(String url, Map<String, String> contentParams, Map<String, File> fileParams, ZResponse<T> response) {
        return uploadFileWithHeader(url, null, contentParams, fileParams, response);
    }

    public static <T extends ZReply> String uploadFile(String url, Object contentParams, Map<String, File> fileParams, ZResponse<T> response) {
        return uploadFileWithHeader(url, null, contentParams, fileParams, response);
    }

    public static <T extends ZReply> String uploadFile(String url, String fileKey, File[] fileValue, ZResponse<T> response) {
        String cancelTag = UUID.randomUUID().toString();
        if (REQUESTINTERCEPT == null || !REQUESTINTERCEPT.onRequest(url, null, fileValue, response)) {
            OkHttpUtils.post().url(url).files(fileKey, fileValue).tag(cancelTag).build().execute(response.generatedProxy());
        }
        return cancelTag;
    }

    public static <T extends ZReply> String uploadFile(String url, Map<String, String> contentParams, String fileKey, File[] fileValue, ZResponse<T> response) {
        String cancelTag = UUID.randomUUID().toString();
        removeNullValue(contentParams);
        if (REQUESTINTERCEPT == null || !REQUESTINTERCEPT.onRequest(url, null, fileValue, response)) {
            OkHttpUtils.post().url(url).params(contentParams).files(fileKey, fileValue).tag(cancelTag).build().execute(response.generatedProxy());
        }
        return cancelTag;
    }

    public static <T extends ZReply> String uploadFileWithHeader(String url, LinkedHashMap<String, String> headers, Map<String, File> fileParams,
            ZResponse<T> response) {
        return uploadFileWithHeader(url, headers, null, fileParams, response);
    }

    public static <T extends ZReply> String uploadFileWithHeader(String url, LinkedHashMap<String, String> headers, Object contentParams,
            Map<String, File> fileParams, ZResponse<T> response) {
        return uploadFileWithHeader(url, headers, beanToMap(contentParams), fileParams, response);
    }

    public static <T extends ZReply> String uploadFileWithHeader(String url, LinkedHashMap<String, String> headers, Map<String, String> contentParams,
            Map<String, File> fileParams, ZResponse<T> response) {
        String cancelTag = UUID.randomUUID().toString();
        removeNullValue(contentParams);
        if (REQUESTINTERCEPT == null || !REQUESTINTERCEPT.onRequest(url, headers, fileParams, response)) {
            OkHttpUtils.post().url(url).headers(headers).files(fileParams).params(contentParams).tag(cancelTag).build().execute(response.generatedProxy());
        }
        return cancelTag;
    }

    public static String downLoadFile(String url, ZFileResponse response) {
        return downLoadFile(url, null, response);
    }

    public static String downLoadFile(String url, Map<String, String> contentParams, ZFileResponse response) {
        return downLoadFileWithHeader(url, null, contentParams, response);
    }

    public static String downLoadFileWithHeader(String url, LinkedHashMap<String, String> headers, ZFileResponse response) {
        return downLoadFileWithHeader(url, headers, null, response);
    }

    public static String downLoadFileWithHeader(String url, LinkedHashMap<String, String> headers, Map<String, String> contentParams, ZFileResponse response) {
        String cancelTag = UUID.randomUUID().toString();
        removeNullValue(contentParams);
        if (REQUESTINTERCEPT == null || !REQUESTINTERCEPT.onRequest(url, headers, contentParams, response)) {
            OkHttpUtils.get().url(url).headers(headers).params(contentParams).tag(cancelTag).build().execute(response);
        }
        return cancelTag;
    }

    /**
     * 取消请求
     */
    public static void cancelRequest(String... tag) {
        if (tag != null && tag.length > 0) {
            for (String s : tag) {
                if (s != null) {
                    OkHttpUtils.getInstance().cancelTag(s);
                }
            }
        }
    }

    public static GetBuilder get() {
        return OkHttpUtils.get();
    }

    public static PostFormBuilder post() {
        return OkHttpUtils.post();
    }

    public static OtherRequestBuilder delete() {
        return OkHttpUtils.delete();
    }

    public static OtherRequestBuilder put() {
        return OkHttpUtils.put();
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

        Map<String, String> result = new HashMap<>();
        Field[] fields = bean.getClass().getFields();
        if (fields == null || fields.length == 0) {
            return result;
        }

        for (Field field : fields) {
            //获取属性名称及值存入Map  
            String key = field.getName();
            try {
                Object object = field.get(bean);
                if (object != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    //如果是list，转为，分割的字符串
                    if (object instanceof Collection) {
                        Collection list = ((Collection) object);
                        for (Object o : list) {
                            stringBuilder.append(String.valueOf(list)).append(",");
                        }

                        if (stringBuilder.length() > 0) {
                            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
                        }
                    } else {
                        stringBuilder.append(String.valueOf(field.get(bean)));
                    }
                    result.put(key, stringBuilder.toString());
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        //获取父类属性  
        fields = bean.getClass().getSuperclass().getFields();
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

    public static void removeNullValue(Map<String, String> hashMap) {
        if (!IS_CLEAR_EMPTY_VALUE && !IS_CLEAR_NULL_VALUE) {
            return;
        }

        for (Iterator<Map.Entry<String, String>> it = hashMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> item = it.next();
            if (item.getValue() == null && (IS_CLEAR_EMPTY_VALUE || IS_CLEAR_NULL_VALUE)) {
                it.remove();
                continue;
            }

            if (StringUtil.isEmpty(item.getValue()) && IS_CLEAR_EMPTY_VALUE) {
                it.remove();
            }
        }
    }
}
