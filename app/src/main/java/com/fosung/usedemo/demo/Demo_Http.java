/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午9:54
 **********************************************************/

package com.fosung.usedemo.demo;

import android.widget.TextView;

import com.fosung.frame.app.BaseFrameActivity;
import com.fosung.frame.http.response.StringResponse;
import com.fosung.frame.utils.LogUtil;
import com.fosung.usedemo.http.HttpUrl;
import com.fosung.usedemo.http.HttpUtil;
import com.fosung.usedemo.http.entity.HttpBaseReplyBean;
import com.fosung.usedemo.http.response.ZResponse;

import java.util.HashMap;

import okhttp3.Response;

/**
 * 此类在Mvp中使用为Model层，{@link com.fosung.usedemo.demo.demo_mvp.model.BaiduStringModel}
 * <p/>
 * 网络请求DEMO,具体使用方法可以参照
 */
public class Demo_Http {

    /**
     * GET请求， 返回String字符串
     * <p/>
     * context不为null则显示progressbar
     */
    public static void commonHttpGetWithStringResponse(BaseFrameActivity context, final TextView tv) {
        HttpUtil.get(HttpUrl.URL_BAIDU_TEST, new StringResponse(context) {

            @Override
            public void onSuccess(Response response, String httpString) {
                tv.setText(httpString);
            }

            @Override
            public void onError(int code, okhttp3.Call call, Exception e) {
                tv.setText(LogUtil.ExceptionToString(e));
            }
        });
    }

    /**
     * GET请求， 返回Gson序列化的对象
     */
    public static void commonHttpGetWithGsonResponse(final TextView tv) {
        HttpUtil.get(HttpUrl.URL_BAIDU_TEST, new ZResponse<DemoBean>(DemoBean.class) {
            @Override
            public void onSuccess(Response response, DemoBean httpBaseBean) {
                tv.setText(String.valueOf(httpBaseBean));
            }

            @Override
            public void onError(int code, String error) {
                tv.setText(error);
            }
        });
    }

    /**
     * GET请求， 返回Gson序列化的对象
     * <p/>
     * 带有progressBar
     */
    public static void getWithGsonBarResponse(BaseFrameActivity context, final TextView tv) {
        HttpUtil.get(HttpUrl.URL_BAIDU_TEST, new ZResponse<DemoBean>(DemoBean.class, context, "正在加载……") {
            @Override
            public void onSuccess(Response response, DemoBean httpBaseBean) {
                tv.setText(String.valueOf(httpBaseBean));
            }

            @Override
            public void onError(int code, String error) {
                tv.setText(error);
            }
        });
    }

    /**
     * POST请求， 返回Gson序列化的对象
     * <p/>
     * 带有progressBar
     */
    public static void postWithGsonBarResponse(BaseFrameActivity activity, final TextView tv) {
        HashMap<String, String> params = new HashMap<>();
        params.put("param1", "sss");
        HttpUtil.post(HttpUrl.URL_BAIDU_TEST, params, new ZResponse<DemoBean>(DemoBean.class, activity, "正在加载……") {
            @Override
            public void onSuccess(Response response, DemoBean httpBaseBean) {
                tv.setText(String.valueOf(httpBaseBean));
            }

            @Override
            public void onError(int code, String error) {
                tv.setText(error);
            }
        });
    }


    public static class DemoBean extends HttpBaseReplyBean {

    }
}
