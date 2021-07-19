package com.zcolin.frame.demo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zcolin.frame.demo.db.entity.DaoMaster;

import org.greenrobot.greendao.database.Database;

import static com.zcolin.frame.demo.db.entity.DaoMaster.dropAllTables;


/**
 * 默认的DaoMaster.OpenHelper在碰到数据库升级的时候会删除旧的表来创建新的表，
 * 这样就会导致旧表的数据全部丢失了，所以要封装DaoMaster.OpenHelper来实现数据库升级
 */
public class DaoOpenHelper extends DaoMaster.OpenHelper {

    public DaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        dropAllTables(db, true);
        onCreate(db);

        //TODO 如果需要保存数据， 数据库升级时需要在此写升级语句
        switch (oldVersion) {
            case 1:
                //创建新表，注意createTable()是静态方法
                // SchoolDao.createTable(db, true);     

                // 加入新字段
                // db.execSQL("ALTER TABLE 'moments' ADD 'audio_path' TEXT;");  
                break;
            default:
                break;
        }
    }


}