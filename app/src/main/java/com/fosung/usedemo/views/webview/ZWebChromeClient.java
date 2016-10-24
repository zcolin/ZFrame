/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午11:26
 **********************************************************/
package com.fosung.usedemo.views.webview;

import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.fosung.usedemo.views.ZAlert;
import com.fosung.usedemo.views.ZDlg;
import com.fosung.usedemo.views.ZDlgComm;

/**
 * WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等比如
 */
public class ZWebChromeClient extends WebChromeClient {

    //onCloseWindow(关闭WebView) 
    //onCreateWindow() 
    //onJsPrompt 
    //onJsConfirm 
    //onProgressChanged 
    //onReceivedIcon 
    //onReceivedTitle
    //onJsAlert
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        new ZAlert(view.getContext()).setMessage(message)
                                     .addSubmitListener(new ZDlg.ZDialogSubmitInterface() {
                                         @Override
                                         public boolean submit() {
                                             result.confirm();
                                             return true;
                                         }
                                     })
                                     .show();
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        new ZDlgComm(view.getContext()).setMessage(message)
                                       .addSubmitListener(new ZDlg.ZDialogSubmitInterface() {
                                           @Override
                                           public boolean submit() {
                                               result.confirm();
                                               return true;
                                           }
                                       })
                                       .addCancelListener(new ZDlg.ZDialogCancelInterface() {
                                           @Override
                                           public boolean cancel() {
                                               result.cancel();
                                               return true;
                                           }
                                       })
                                       .show();
        return true;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }
}