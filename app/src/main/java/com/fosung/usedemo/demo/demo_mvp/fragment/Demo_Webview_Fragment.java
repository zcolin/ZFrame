/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-18 下午5:24
 **********************************************************/
package com.fosung.usedemo.demo.demo_mvp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fosung.frame.app.BaseFrameLazyLoadFrag;
import com.fosung.frame.jsbridge.BridgeHandler;
import com.fosung.frame.jsbridge.CallBackFunction;
import com.fosung.frame.jsbridge.DefaultHandler;
import com.fosung.usedemo.R;
import com.fosung.usedemo.demo.demo_mvp.contract.DemoWebViewContract;
import com.fosung.usedemo.demo.demo_mvp.presenter.DemoWebViewPresenter;
import com.fosung.usedemo.views.ZAlert;
import com.fosung.usedemo.views.ZDlg;
import com.fosung.usedemo.views.webview.ZWebChooseFileChromeClient;
import com.fosung.usedemo.views.webview.ZWebView;

/**
 * 带JsBridge的webview的Demo
 */
public class Demo_Webview_Fragment extends BaseFrameLazyLoadFrag implements OnClickListener, DemoWebViewContract.View {
    private ZWebView                      webView;
    private Button                        button;
    private ZWebChooseFileChromeClient    webChromeClient;
    private DemoWebViewContract.Presenter presenter;

    public static Demo_Webview_Fragment newInstance() {
        Bundle args = new Bundle();
        Demo_Webview_Fragment fragment = new Demo_Webview_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootViewLayId() {
        return R.layout.a_demo_webview;
    }

    @Override
    protected void lazyLoad() {
        webView = (ZWebView) getView(R.id.webView);
        button = (Button) getView(R.id.button);
        button.setOnClickListener(this);

        initWebView();
        loadUrl();

        presenter = new DemoWebViewPresenter(this);
        presenter.getUserDataFrom_xx();
    }

    public void initWebView() {
        webChromeClient = new ZWebChooseFileChromeClient(this);
        webView.setWebChromeClient(webChromeClient);

        webView.setDefaultHandler(new DefaultHandler());//如果JS调用send方法，会走到DefaultHandler里
        webView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                new ZAlert(mActivity).setMessage("监听到网页传入数据：" + data)
                                     .addSubmitListener(new ZDlg.ZDialogSubmitInterface() {
                                         @Override
                                         public boolean submit() {
                                             function.onCallBack("java 返回数据！！！");
                                             return true;
                                         }
                                     })
                                     .show();
            }
        });
        webView.registerStartActivity(mActivity);
        webView.registerFinishActivity(mActivity);
    }

    public void loadUrl() {
        webView.loadUrl("file:///android_asset/demo.html");
    }

    @Override
    public void callJsFunc(String funcName, String strParam) {
        webView.callHandler(funcName, strParam, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                new ZAlert(mActivity).setMessage("网页返回数据：" + data)
                                     .show();
            }
        });
        //webView.send("hello");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        webChromeClient.processResult(requestCode, resultCode, intent);
    }

    @Override
    public void onClick(View v) {
        if (button.equals(v)) {
            callJsFunc("functionInJs", "java 调用传入数据");
        }
    }
}
