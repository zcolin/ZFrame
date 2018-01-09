/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */

package com.zcolin.frame.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zcolin.frame.app.BaseApp;
import com.zcolin.frame.util.LogUtil;

import java.util.List;

/**
 * SQLiteOpenHelper基类
 * <p>
 *
 * @deprecated 使用GreenDao，此类不再使用，{@link DaoHelper}
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int      VERSION   = 1;
    private static       DBHelper mInstance = null;
    protected SQLiteDatabase dataBase;

    public DBHelper(String name) {
        super(BaseApp.APP_CONTEXT, name, null, VERSION);
        dataBase = getWritableDatabase();
    }

    private static DBHelper getInstance() {
        //        String uid = TextUtils.isEmpty(UserManager.getInstance()
        //                                                  .getCurUid()) ? null : UserManager.getInstance()
        //                                                                                    .getCurUid();
        //TODO 动态获取UID
        String uid = null;
        if (uid == null && (mInstance == null || !"error.db".equals(mInstance.getDatabaseName()))) {
            synchronized (DBHelper.class) {
                if (mInstance != null) {
                    mInstance.close();
                }
                mInstance = new DBHelper("error.db");
            }
        } else if (uid != null && (mInstance == null || !(uid + ".db").equals(mInstance.getDatabaseName()))) {
            //如果切换用户,需要切换数据库
            synchronized (DBHelper.class) {
                if (mInstance != null) {
                    mInstance.close();
                }
                mInstance = new DBHelper(uid + ".db");
            }
        }
        return mInstance;
    }

    /**
     * 执行Sql语句
     *
     * @param sql sql语句
     * @return true if success
     */
    public static synchronized boolean execSQL(String sql) {
        return execSQL(sql, null);
    }

    /**
     * 执行Sql语句
     *
     * @param sql    sql语句
     * @param arrObj 参数数组
     * @return true if success
     */
    public static synchronized boolean execSQL(String sql, Object[] arrObj) {
        SQLiteDatabase dataBase = DBHelper.getInstance().dataBase;
        if (dataBase == null) {
            return false;
        }
        boolean flag = true;
        try {
            if (arrObj != null) {
                dataBase.execSQL(sql, arrObj);
            } else {
                dataBase.execSQL(sql);
            }
        } catch (Exception e) {
            flag = false;
            LogUtil.w(sql, LogUtil.ExceptionToString(e));
        }
        return flag;
    }

    /**
     * 执行Sql语句,使用事务
     *
     * @param arrStrSql sql数组
     * @return true if success
     */
    public static synchronized boolean execSQLWithTransaction(String[] arrStrSql) {
        SQLiteDatabase dataBase = DBHelper.getInstance().dataBase;
        if (dataBase == null || arrStrSql == null || arrStrSql.length == 0) {
            return false;
        }

        boolean flag = true;
        try {
            dataBase.beginTransaction();
            for (String strSql : arrStrSql) {
                dataBase.execSQL(strSql);
            }
            dataBase.setTransactionSuccessful();
        } catch (Exception e) {
            flag = false;
            LogUtil.w("execSQLWithTransaction", LogUtil.ExceptionToString(e));
        } finally {
            dataBase.endTransaction();
        }
        return flag;
    }

    /**
     * 插入数据
     *
     * @return true if success
     */
    public static synchronized long insert(String table, String nullColumnHack, ContentValues values) {
        SQLiteDatabase dataBase = DBHelper.getInstance().dataBase;
        if (dataBase == null) {
            return -1;
        }

        long flag = -1;
        try {
            flag = dataBase.insert(table, nullColumnHack, values);
        } catch (Exception e) {
            LogUtil.w("insert:table", LogUtil.ExceptionToString(e));
        }
        return flag;
    }

    /**
     * 插入数据，使用事务
     *
     * @return true if success
     */
    public static synchronized boolean insertWithTransaction(String table, String nullColumnHack, List<ContentValues> listValues) {
        SQLiteDatabase dataBase = DBHelper.getInstance().dataBase;
        if (dataBase == null || listValues == null || listValues.size() == 0) {
            return false;
        }

        boolean flag = true;
        try {
            dataBase.beginTransaction();
            for (ContentValues contentValues : listValues) {
                dataBase.insert(table, nullColumnHack, contentValues);
            }
            dataBase.setTransactionSuccessful();
        } catch (Exception e) {
            flag = false;
            LogUtil.w("insertWithTransaction:table", LogUtil.ExceptionToString(e));
        } finally {
            dataBase.endTransaction();
        }
        return flag;
    }

    /**
     * 插入数据
     *
     * @return true if success
     */
    public static synchronized long update(String table, ContentValues values, String whereCaseValue, String[] whereArgs) {
        SQLiteDatabase dataBase = DBHelper.getInstance().dataBase;
        if (dataBase == null) {
            return -1;
        }

        long flag = -1;
        try {
            flag = dataBase.update(table, values, whereCaseValue, whereArgs);
        } catch (Exception e) {
            LogUtil.w("insert:table", LogUtil.ExceptionToString(e));
        }
        return flag;
    }

    /**
     * 替换数据
     *
     * @return true if success
     */
    public static synchronized long replace(String table, String nullColumnHack, ContentValues values) {
        SQLiteDatabase dataBase = DBHelper.getInstance().dataBase;
        if (dataBase == null) {
            return -1;
        }

        long flag = -1;
        try {
            flag = dataBase.replace(table, nullColumnHack, values);
        } catch (Exception e) {
            LogUtil.w("replace:table", LogUtil.ExceptionToString(e));
        }
        return flag;
    }

    /**
     * 删除数据
     */
    public static synchronized int delete(String table, String whereClause, String[] whereArgs) {
        SQLiteDatabase dataBase = DBHelper.getInstance().dataBase;
        if (dataBase == null) {
            return -1;
        }

        int flag = -1;
        try {
            flag = dataBase.delete(table, whereClause, whereArgs);
        } catch (Exception e) {
            LogUtil.w("replace:table", LogUtil.ExceptionToString(e));
        }
        return flag;
    }

    /**
     * 执行Sql查询语句
     *
     * @param strSql sql语句
     * @return true if success
     */
    public static synchronized Cursor execQuery(String strSql) {
        return execQuery(strSql, null);
    }

    /**
     * 执行Sql查询语句
     *
     * @param strSql sql语句
     * @param strArr 参数数组
     * @return true if success
     */
    public static synchronized Cursor execQuery(String strSql, String[] strArr) {
        SQLiteDatabase dataBase = DBHelper.getInstance().dataBase;
        if (dataBase == null) {
            return null;
        }
        Cursor cursor = null;

        try {
            cursor = dataBase.rawQuery(strSql, strArr);
        } catch (Exception e) {
            LogUtil.w("strSql", LogUtil.ExceptionToString(e));
        }
        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
