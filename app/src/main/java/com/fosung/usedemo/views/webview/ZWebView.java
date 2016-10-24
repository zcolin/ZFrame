/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午11:26
 **********************************************************/
package com.fosung.usedemo.views.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fosung.frame.jsbridge.BridgeHandler;
import com.fosung.frame.jsbridge.BridgeWebView;
import com.fosung.frame.jsbridge.CallBackFunction;
import com.fosung.frame.utils.LogUtil;


/**
 * 封装的Webview的控件
 */
public class ZWebView extends BridgeWebView {

    private WebViewClient   webViewClient;
    private WebChromeClient webChromeClient;
    private Context         context;

    public ZWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initWebView();
    }

    //网页属性设置
    @SuppressLint({"SetJavaScriptEnabled"})
    private void initWebView() {
        webViewClient = new ZWebViewClient(this);
        webChromeClient = new ZWebChromeClient();
        setWebViewClient(webViewClient);
        setWebChromeClient(webChromeClient);
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        setHorizontalScrollBarEnabled(false);
        //webView.addJavascriptInterface(new JsInterUtil(), "javautil");
        setHorizontalScrollbarOverlay(true);
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
    }

    public WebViewClient getWebViewClient() {
        return webViewClient;
    }

    @Override
    public void setWebViewClient(WebViewClient webViewClient) {
        super.setWebViewClient(webViewClient);
        this.webViewClient = webViewClient;
    }

    public WebChromeClient getWebChromeClient() {
        return webChromeClient;
    }

    public void setWebChromeClient(WebChromeClient webChromeClient) {
        super.setWebChromeClient(webChromeClient);
        this.webChromeClient = webChromeClient;
    }

    /**
     * 注册启动Activity的web交互
     */
    public void registerStartActivity(final Activity activity) {
        registerHandler("startActivity", new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                try {
                    Intent intent = new Intent();
                    ComponentName componentName = new ComponentName(activity.getPackageName(), activity.getPackageName() + "." + data);
                    intent.setComponent(componentName);
                    activity.startActivity(intent);
                } catch (Exception e) {
                    LogUtil.e("ZWebView.startActivity.handler", e);
                }
            }
        });
    }

    /**
     * 注册启动Activity的web交互
     */
    public void registerFinishActivity(final Activity activity) {
        registerHandler("finishActivity", new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                activity.finish();
            }
        });
    }
}
