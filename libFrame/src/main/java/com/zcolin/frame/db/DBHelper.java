/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
 * ********************************************************
 */

package com.zcolin.frame.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zcolin.frame.util.LogUtil;

import java.util.List;

/**
 * SQLiteOpenHelper基类
 * <p>
 *
 * @deprecated 使用GreenDao，此类不再使用，{@link DaoHelper}
 */
public class DBHelper extends SQLiteOpenHelper {
    protected SQLiteDatabase dataBase;

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    public DBHelper(Context context, String name, int version) {
        super(context, name, null, version);
        dataBase = getWritableDatabase();
    }

    /**
     * 执行Sql语句
     *
     * @param sql sql语句
     * @return true if success
     */
    public synchronized boolean execSQL(String sql) {
        return execSQL(sql, null);
    }

    /**
     * 执行Sql语句
     *
     * @param sql    sql语句
     * @param arrObj 参数数组
     * @return true if success
     */
    public synchronized boolean execSQL(String sql, Object[] arrObj) {
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
    public synchronized boolean execSQLWithTransaction(String[] arrStrSql) {
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
    public synchronized long insert(String table, String nullColumnHack, ContentValues values) {
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
    public synchronized boolean insertWithTransaction(String table, String nullColumnHack, List<ContentValues> listValues) {
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
    public synchronized long update(String table, ContentValues values, String whereCaseValue, String[] whereArgs) {
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
    public synchronized long replace(String table, String nullColumnHack, ContentValues values) {
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
    public synchronized int delete(String table, String whereClause, String[] whereArgs) {
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
    public synchronized Cursor execQuery(String strSql) {
        return execQuery(strSql, null);
    }

    /**
     * 执行Sql查询语句
     *
     * @param strSql sql语句
     * @param strArr 参数数组
     * @return true if success
     */
    public synchronized Cursor execQuery(String strSql, String[] strArr) {
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


}
