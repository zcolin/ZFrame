package com.zcolin.frame.demo.db;


import android.content.Context;

import com.zcolin.frame.app.BaseApp;
import com.zcolin.frame.db.DaoHelper;
import com.zcolin.frame.demo.db.entity.DaoMaster;
import com.zcolin.frame.demo.db.entity.DaoSession;


/**
 * 数据库管理类，管理session,helper,master对象
 */
public class DaoManager {

    private static DaoOpenHelper         DAO_OPENHELPER;
    private static DaoHelper<DaoSession> DAO_HELPER;
    private static DaoSession            DAO_SESSION;
    private static DaoMaster             DAO_MASTER;

    /**
     * 得到数据库管理者
     */
    public static DaoMaster getDaoMaster() {
        if (DAO_MASTER == null) {
            DAO_MASTER = new DaoMaster(getDaoOpenHelper(BaseApp.APP_CONTEXT, "default").getWritableDatabase());
        }
        return DAO_MASTER;
    }

    /**
     * 得到数据库，传入路径
     *
     * @param context 提供目录重写
     * @param name    数据库名
     */
    public static DaoMaster getDaoMaster(Context context, String name) {
        if (DAO_MASTER == null) {
            DAO_MASTER = new DaoMaster(getDaoOpenHelper(context, name).getWritableDatabase());
        }
        return DAO_MASTER;
    }

    /**
     * 得到daoSession，可以执行增删改查操作
     */
    public static DaoSession getDaoSession() {
        if (DAO_SESSION == null) {
            DAO_SESSION = getDaoMaster().newSession();
        }
        return DAO_SESSION;
    }

    /**
     * 得到daoSession，可以执行增删改查操作
     */
    public static DaoSession getDaoSession(Context context, String name) {
        if (DAO_SESSION == null) {
            DAO_SESSION = getDaoMaster(context, name).newSession();
        }
        return DAO_SESSION;
    }

    /**
     * 获取DaoHelper
     */
    public static DaoHelper<DaoSession> getDaoHelper(Context context, String name) {
        if (DAO_HELPER == null) {
            DAO_HELPER = new DaoHelper<>(getDaoSession(context, name));
        }
        return DAO_HELPER;
    }

    /**
     * 获取DaoHelper
     */
    public static DaoHelper<DaoSession> getDaoHelper() {
        if (DAO_HELPER == null) {
            DAO_HELPER = new DaoHelper<>(getDaoSession());
        }
        return DAO_HELPER;
    }

    /**
     * 获取DaoOpenHelper
     */
    private static DaoOpenHelper getDaoOpenHelper(Context context, String name) {
        if (DAO_OPENHELPER == null) {
            DAO_OPENHELPER = new DaoOpenHelper(context, name, null);
        }
        return DAO_OPENHELPER;
    }

    /**
     * 关闭数据库
     */
    public static void closeDataBase() {
        if (DAO_OPENHELPER != null) {
            DAO_OPENHELPER.close();
            DAO_OPENHELPER = null;
        }
        if (null != DAO_SESSION) {
            DAO_SESSION.clear();
            DAO_SESSION = null;
        }
    }
}
