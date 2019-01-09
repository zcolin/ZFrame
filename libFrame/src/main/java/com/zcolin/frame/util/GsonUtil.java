/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */

package com.zcolin.frame.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Gson序列化操作工具类
 */
public class GsonUtil {
    private static Gson gson = new Gson();
    private static Gson gsonExclude;

    static {

        /*
         * 字段排除策略，如果想不解析某个字段，以“__”开头就可以了
         */
        ExclusionStrategy myExclusionStrategy = new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes fa) {
                return fa.getName().startsWith("__");
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        };
        gsonExclude = new GsonBuilder().setExclusionStrategies(myExclusionStrategy).create();
    }

    private GsonUtil() {
    }

    /**
     * 转成json
     */
    public static String beanToString(Object object) {
        String gsonString = null;
        if (gson != null && object != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }


    /**
     * 转成json
     */
    public static String beanToStringWithExcluded(Object object) {
        String gsonString = null;
        if (gsonExclude != null && object != null) {
            gsonString = gsonExclude.toJson(object);
        }
        return gsonString;
    }

    /**
     * 转成bean
     */
    public static <T> T stringToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            try {
                t = gson.fromJson(gsonString, cls);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    /**
     * 转成list
     */
    public static <T> List<T> stringToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        try {
            JsonArray array = new JsonParser().parse(gsonString).getAsJsonArray();
            list = new ArrayList<>();
            for (final JsonElement elem : array) {
                list.add(new Gson().fromJson(elem, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 转成list中有map的
     */
    public static <T> List<Map<String, T>> stringToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        try {
            if (gson != null) {
                list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
                }.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 转成map的
     */
    public static <T> Map<String, T> stringToMaps(String gsonString) {
        Map<String, T> map = null;
        try {
            if (gson != null) {
                map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
                }.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 解析为JsonElement对象
     */
    public static JsonElement parse(String str) {
        try {
            return new JsonParser().parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
