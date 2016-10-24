/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午11:26
 **********************************************************/

package com.fosung.usedemo.views.webview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.fosung.usedemo.R;
import com.fosung.usedemo.views.ZSwipeRefreshLayout;

import java.util.Map;

/**
 * 封装的Webview的控件
 * 1 下拉刷新
 * 2 加载进度条
 */
public class ZRefreshWebView extends RelativeLayout {

    private ZWebView            webView;
    private ProgressBar         proBar;            //加载進度条
    private ZSwipeRefreshLayout swipeLay;
    private Context             context;

    public ZRefreshWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initRes();
    }

    private void initRes() {
        LayoutInflater.from(context)
                      .inflate(R.layout.view_swiperefresh_webview, this);
        webView = (ZWebView) findViewById(R.id.webview);
        proBar = (ProgressBar) findViewById(R.id.progressBar);
        swipeLay = (ZSwipeRefreshLayout) findViewById(R.id.swipelayout);
        

        swipeLay.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                webView.reload();
                swipeLay.setRefreshing(false);
            }
        });
    }


    public void loadUrl(String url) {
        webView.loadUrl(url);
    }

    public void loadData(String url, Map<String, String> additionalHttpHeaders) {
        webView.loadUrl(url, additionalHttpHeaders);
    }

    public void loadData(String data, String mimeType, String encoding) {
        webView.loadData(data, mimeType, encoding);
    }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String failUrl) {
        webView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, failUrl);
    }

    public boolean canGoBack() {
        return webView.canGoBack();
    }

    public void goBack() {
        webView.goBack();
    }

    public WebView getWebView() {
        return webView;
    }

    public WebViewClient getWebViewClient() {
        return webView.getWebViewClient();
    }

    public WebChromeClient getWebChromeClient() {
        return webView.getWebChromeClient();
    }

    public void setProgressBarVisibility(int visibility) {
        proBar.setVisibility(visibility);
    }
}
