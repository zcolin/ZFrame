package com.zcolin.frame.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 实体转换类
 */
public class BeanUtil {

    /**
     * 所有属性必须为public
     *
     * @return 将某个类及其继承属性全部添加到Map中
     */
    public static Map<String, String> beanToMap(Object bean) {
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
                if (object == null) {
                    continue;
                }

                StringBuilder stringBuilder = new StringBuilder();
                //如果是list，转为，分割的字符串
                if (object instanceof Collection) {
                    Collection list = ((Collection) object);
                    for (Object o : list) {
                        stringBuilder.append(String.valueOf(o)).append(",");
                    }

                    if (stringBuilder.length() > 0) {
                        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
                    }
                } else {
                    stringBuilder.append(String.valueOf(field.get(bean)));
                }
                result.put(key, stringBuilder.toString());
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
                if (object == null) {
                    continue;
                }

                String value = String.valueOf(field.get(bean));
                result.put(key, value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
