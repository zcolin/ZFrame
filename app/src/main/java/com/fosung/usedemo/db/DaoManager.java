/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午9:54
 **********************************************************/

package com.fosung.usedemo.db;


import com.fosung.frame.app.BaseApp;
import com.fosung.usedemo.greendao.dao.DaoMaster;
import com.fosung.usedemo.greendao.dao.DaoSession;

/**
 * 数据库管理类，管理session,helper,master对象
 */
public class DaoManager {

    private static DaoSession    DAO_SESSION;
    private static DaoOpenHelper DAO_HELPER;
    private static DaoMaster     DAO_MASTER;

    /**
     * 得到数据库管理者
     */
    public static DaoMaster getDaoMaster() {
        if (DAO_MASTER == null) {
            DAO_MASTER = new DaoMaster(getDaoHelper().getWritableDatabase());
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
    public static DaoOpenHelper getDaoHelper() {
        if (DAO_HELPER == null) {
            DAO_HELPER = new DaoOpenHelper(BaseApp.APP_CONTEXT, "test", null);
        }
        return DAO_HELPER;
    }

    /**
     * 关闭数据库
     */
    public static void closeDataBase() {
        if (DAO_HELPER != null) {
            DAO_HELPER.close();
            DAO_HELPER = null;
        }
        if (null != DAO_SESSION) {
            DAO_SESSION.clear();
            DAO_SESSION = null;
        }
    }
}
