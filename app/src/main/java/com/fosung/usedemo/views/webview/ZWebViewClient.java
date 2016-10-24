/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午11:26
 **********************************************************/
package com.fosung.usedemo.views.webview;

import android.graphics.Bitmap;
import android.webkit.WebView;

import com.fosung.frame.jsbridge.BridgeWebView;
import com.fosung.frame.jsbridge.BridgeWebViewClient;
import com.fosung.frame.utils.ToastUtil;

/**
 * WebViewClient主要帮助WebView处理各种通知、请求事件的.
 */
public class ZWebViewClient extends BridgeWebViewClient {

    public ZWebViewClient(BridgeWebView webView) {
        super();
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        ToastUtil.toastLong("ErrorCode:" + errorCode + "  Description:" + description + "  FailingUrl" + failingUrl);
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }
}