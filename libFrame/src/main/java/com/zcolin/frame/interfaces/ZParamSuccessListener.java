package com.zcolin.frame.interfaces;

/**
 * 成功回调接口
 */
public interface ZParamSuccessListener<E> {
    boolean submit(E t);
}