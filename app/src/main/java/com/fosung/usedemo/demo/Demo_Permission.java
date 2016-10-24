/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午9:54
 **********************************************************/
package com.fosung.usedemo.demo;

import android.app.Activity;

import com.fosung.frame.permission.PermissionHelper;
import com.fosung.frame.permission.PermissionsResultAction;

/**
 * 此类应该为MVP中的Presenter层，处理权限信息
 */
public class Demo_Permission {
    public void requestWriteSdCard(Activity activity) {
        PermissionHelper.requestWriteSdCardPermission(activity, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                System.out.println("获取写SD卡权限");
            }

            @Override
            public void onDenied(String permission) {
                System.out.println("拒绝获取写SD卡权限");
            }
        });
    }

    public void requestCamera(Activity activity) {
        PermissionHelper.requestCameraPermission(activity, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                System.out.println("获取写SD卡权限");
            }

            @Override
            public void onDenied(String permission) {
                System.out.println("拒绝获取写SD卡权限");
            }
        });
    }
}
