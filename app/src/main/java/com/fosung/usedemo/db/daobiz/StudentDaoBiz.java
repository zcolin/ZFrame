/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午9:54
 **********************************************************/

package com.fosung.usedemo.db.daobiz;


import com.fosung.frame.db.BaseDaoBiz;
import com.fosung.usedemo.db.DaoManager;
import com.fosung.usedemo.greendao.dao.DaoSession;
import com.fosung.usedemo.greendao.entity.Student;

/**
 * 示例
 */
public class StudentDaoBiz extends BaseDaoBiz<Student, DaoSession> {

    @Override
    public DaoSession getDaoSession() {
        return DaoManager.getDaoSession();
    }
}
