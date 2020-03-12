/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */
package com.zcolin.frame.util;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射辅助类
 */
public class ReflectionUtil {

    /**
     * 设置字段值
     *
     * @param source 需要设置的对象
     * @param name   字段名字
     */
    public static void setField(Object source, String name, Object obj) {
        Field field;
        try {
            field = source.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(source, obj);
            field.setAccessible(false);
        } catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * reflect super class's field, then set new value
     */
    public static void setSuperField(Object source, String name, Object obj) {
        Field field;
        try {
            field = source.getClass().getSuperclass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(source, obj);
            field.setAccessible(false);
        } catch (SecurityException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * get super class's reflect field
     */
    public static Object getField(Object source, String name) {
        Field field;
        Object obj = null;
        try {
            field = source.getClass().getDeclaredField(name);
            field.setAccessible(true);
            obj = field.get(source);
            field.setAccessible(false);
        } catch (SecurityException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return obj;
    }


    /**
     * get reflect field
     */
    public static Object getSuperField(Object source, String name) {
        Field field;
        Object obj = null;
        try {
            field = source.getClass().getSuperclass().getDeclaredField(name);
            field.setAccessible(true);
            obj = field.get(source);
            field.setAccessible(false);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return obj;
    }


    /**
     * invoke reflect method
     */
    public static Object invokeMethod(Object source, String name) {
        Method method;
        Object result = null;
        try {
            method = source.getClass().getDeclaredMethod(name);
            method.setAccessible(true);
            result = method.invoke(source);
            method.setAccessible(false);
        } catch (SecurityException | InvocationTargetException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return result;

    }


    /**
     * invoke reflect method
     */
    public static Object invokeMethod(Object source, String name, Object[] obj, Class<?>... cls) {
        Method method;
        Object result = null;
        try {
            method = source.getClass().getDeclaredMethod(name, cls);
            method.setAccessible(true);
            result = method.invoke(source, obj);
            method.setAccessible(false);
        } catch (SecurityException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * invoke reflect super class's method
     */
    public static Object invokeSuperMethod(Object source, String name) {
        Method method;
        Object result = null;
        try {
            method = source.getClass().getSuperclass().getDeclaredMethod(name);
            method.setAccessible(true);
            result = method.invoke(source);
            method.setAccessible(false);
        } catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * invoke reflect super class's method
     */
    public static Object invokeSuperMethod(Object source, String name, Object[] obj, Class<?>... cls) {
        Method method;
        Object result = null;
        try {
            method = source.getClass().getSuperclass().getDeclaredMethod(name, cls);
            method.setAccessible(true);
            result = method.invoke(source, obj);
            method.setAccessible(false);
        } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }
}