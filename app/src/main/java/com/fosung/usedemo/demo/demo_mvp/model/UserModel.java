/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-18 下午4:49
 **********************************************************/
package com.fosung.usedemo.demo.demo_mvp.model;

import com.fosung.usedemo.demo.demo_mvp.model.entity.User;
import com.fosung.usedemo.http.HttpUrl;
import com.fosung.usedemo.http.HttpUtil;
import com.fosung.usedemo.http.response.ZResponse;

/**
 * Model层为数据处理层， 处理网络操作和数据库操作的信息获取和提交
 * <p>
 * 处理特定网络请求，此处Demo为User信息获取(只做代码展示，运行无效果)
 */
public class UserModel {

    public void getUser(ZResponse<User> response) {
        HttpUtil.get(HttpUrl.URL_BAIDU_TEST, response);
    }
}
