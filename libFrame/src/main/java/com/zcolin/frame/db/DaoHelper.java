/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.zcolin.frame.db;


import android.database.Cursor;

import com.zcolin.frame.util.LogUtil;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;


/**
 * 将所有创建的表格相同的部分封装到这个BaseDao中
 * <p/>
 * T 生成的实体对象
 * T1 DaoSession
 */
public class DaoHelper<T1 extends AbstractDaoSession> {
    public static final String TAG = DaoHelper.class.getSimpleName();
    public T1 daoSession;

    public DaoHelper(T1 daoSession) {
        this.daoSession = daoSession;
    }

    /*********************************** 增  start  **********************************************/
    /**
     * 插入单个对象
     */
    public <T> boolean insertObject(T object) {
        boolean flag = false;
        try {
            flag = daoSession.insert(object) != -1;
        } catch (Exception e) {
            LogUtil.e("BaseDaoBiz.insertObject", LogUtil.ExceptionToString(e));
        }
        return flag;
    }


    /**
     * 插入或者替换单个对象
     */
    public <T> boolean insertOrReplaceObject(T object) {
        boolean flag = false;
        try {
            flag = daoSession.insertOrReplace(object) != -1;
        } catch (Exception e) {
            LogUtil.e("BaseDaoBiz.insertOrReplaceObject", LogUtil.ExceptionToString(e));
        }
        return flag;
    }

    /**
     * 插入多个对象，并开启新的线程
     */
    public <T> boolean insertMultObject(final List<T> objects) {
        boolean flag = false;
        if (null == objects || objects.isEmpty()) {
            return false;
        }

        try {
            daoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        daoSession.insertOrReplace(object);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            LogUtil.e("BaseDaoBiz.insertMultObject", LogUtil.ExceptionToString(e));
        }
        return flag;
    }
    /*********************************** 增  end  **********************************************/


    /*********************************** 改  start  **********************************************/
    /**
     * 以对象形式进行数据修改
     * 其中必须要知道对象的主键ID
     */
    public <T> void updateObject(T object) {
        if (null == object) {
            return;
        }
        try {
            daoSession.update(object);
        } catch (Exception e) {
            LogUtil.e("BaseDaoBiz.updateObject", LogUtil.ExceptionToString(e));
        }
    }

    /**
     * 批量更新数据
     */
    public <T> void updateMultObject(final List<T> objects, Class cls) {
        if (null == objects || objects.isEmpty()) {
            return;
        }
        try {
            daoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        daoSession.update(object);
                    }
                }
            });
        } catch (Exception e) {
            LogUtil.e("BaseDaoBiz.updateMultObject", LogUtil.ExceptionToString(e));
        }
    }
    /*********************************** 改  end  **********************************************/


    /*********************************** 删  start **********************************************/
    /**
     * 删除某个数据库表
     */
    public <T> boolean deleteAll(Class clss) {
        boolean flag = false;
        try {
            daoSession.deleteAll(clss);
            flag = true;
        } catch (Exception e) {
            LogUtil.e("BaseDaoBiz.deleteAll", LogUtil.ExceptionToString(e));
        }
        return flag;
    }

    /**
     * 删除某个对象
     */
    public <T> void deleteObject(T object) {
        try {
            daoSession.delete(object);
        } catch (Exception e) {
            LogUtil.e("BaseDaoBiz.deleteObject", LogUtil.ExceptionToString(e));
        }
    }

    /**
     * 异步批量删除数据
     */
    public <T> boolean deleteMultObject(final List<T> objects, Class clss) {
        boolean flag = false;
        if (null == objects || objects.isEmpty()) {
            return false;
        }

        try {
            daoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        daoSession.delete(object);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            LogUtil.e("BaseDaoBiz.deleteMultObject", LogUtil.ExceptionToString(e));
        }
        return flag;
    }

    /**
     * 根据ID进行数据库的删除操作
     */
    private <T, K> void deleteById(Class<T> cls, K id) {
        @SuppressWarnings("unchecked")
        AbstractDao<T, K> dao = (AbstractDao<T, K>) daoSession.getDao(cls);
        dao.deleteByKey(id);
    }


    /**
     * 根据ID同步删除数据库操作
     */
    private <T, K> void deleteByIds(Class<T> cls, List<K> ids) {
        @SuppressWarnings("unchecked")
        AbstractDao<T, K> dao = (AbstractDao<T, K>) daoSession.getDao(cls);
        dao.deleteByKeyInTx(ids);
    }
    /*********************************** 改  end  **********************************************/


    /*********************************** 查  start  **********************************************/
    /**
     * 获得某个表名
     */
    public <T> String getTableName(Class<T> object) {
        return daoSession.getDao(object)
                         .getTablename();
    }

    /**
     * 查询某个ID的对象是否存在
     *
     * @param condition example NoteDao.Properties.Id.eq(id)
     */
    public <T> boolean isExitObject(Class<T> object, WhereCondition condition) {
        QueryBuilder<T> qb = daoSession.queryBuilder(object);
        qb.where(condition);
        long length = qb.buildCount()
                        .count();
        return length > 0;
    }

    /**
     * 根据Id查询某个Object
     */
    public <T, K> T queryObject(Class<T> cls, K id) {
        return daoSession.load(cls, id);
    }

    /**
     * 查询某条件下的对象
     * <p/>
     * <p/>
     * query list with where clause
     * ex: begin_date_time >= ? AND end_date_time <= ?
     *
     * @param where  where clause, include 'where' word
     * @param params query parameters
     */
    public <T> List<T> queryObjects(Class<T> object, String where, String... params) {
        List<T> objects = null;
        try {
            objects = daoSession.queryRaw(object, where, params);
        } catch (Exception e) {
            LogUtil.e("BaseDaoBiz.queryObjects", LogUtil.ExceptionToString(e));
        }

        return objects;
    }

    /**
     * 查询某条件下的对象
     */
    public <T> List<T> queryObjects(Class<T> object, WhereCondition where) {
        return queryObjects(object, 0, 0, where);
    }

    /**
     * 查询某条件下的对象
     */
    public <T> List<T> queryObjects(Class<T> object, int limit, int offSet, WhereCondition where, WhereCondition... condMore) {
        QueryBuilder<T> queryBuilder = daoSession.queryBuilder(object);
        queryBuilder.where(where, condMore);
        if (limit > 0) {
            queryBuilder.limit(limit);
        }
        if (offSet > 0) {
            queryBuilder.offset(offSet);
        }

        return queryObjects(queryBuilder);
    }

    /**
     * 查询某条件下的对象
     */
    public <T> List<T> queryObjects(QueryBuilder<T> queryBuilder) {
        return queryBuilder.list();
    }

    /**
     * 查询所有对象
     */
    public <T> List<T> queryAll(Class<T> object) {
        List<T> objects = null;
        try {
            objects = daoSession.loadAll(object);
        } catch (Exception e) {
            LogUtil.e("BaseDaoBiz.queryAll", e);
        }
        return objects;
    }

    /*********************************** 查  end  **********************************************/

    /***********************************其他 start**********************************************/

    /**
     * 获取QueryBuilder
     */
    public <T> QueryBuilder<T> getQueryBuilder(Class<T> object) {
        return daoSession.queryBuilder(object);
    }

    /**
     * 执行Sql查询语句
     */
    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return daoSession.getDatabase()
                         .rawQuery(sql, selectionArgs);
    }

    /**
     * 执行Sql语句
     */
    public void rawQuery(String sql) {
        daoSession.getDatabase()
                  .execSQL(sql);
    }


    /**
     * 执行Sql语句
     */
    public void rawQuery(String sql, Object[] bindArgs) {
        daoSession.getDatabase()
                  .execSQL(sql, bindArgs);
    }
}