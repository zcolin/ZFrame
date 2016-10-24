/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-18 下午2:02
 **********************************************************/
package com.fosung.usedemo.demo.demo_mvp.model;

import com.fosung.frame.http.response.StringResponse;
import com.fosung.usedemo.http.HttpUrl;
import com.fosung.usedemo.http.HttpUtil;

/**
 * Model层为数据处理层， 处理网络操作和数据库操作的信息获取和提交
 * <p>
 * 处理特定网络请求，此处Demo为百度首页信息获取
 */
public class BaiduStringModel {

    public void getBaiduString(StringResponse response) {
        HttpUtil.get(HttpUrl.URL_BAIDU_TEST, response);
    }
}
