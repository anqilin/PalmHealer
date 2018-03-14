package com.moxi.palmhealer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.moxi.palmhealer.utils.LogUtils;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mysqlitedb";
    /**
     * 游标
     ***/
    private Cursor c = null;
    /**
     * 建立表的语句
     **/
    private static final String CREATE_TAB = "create table " + "deviceList(_id integer primary key autoincrement,device_mac text,device_name text,device_name_EN text,deviceName_CN text,device_Pic text,latest text)";
    /**
     * 列名
     ***/
    private static final String TAB_NAME = "deviceList";
    /**
     * 数据库
     ***/
    private SQLiteDatabase db = null;

    private static DBHelper instance = null;

    public static DBHelper getInstance(Context context) {

        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    /***
     * 构造函数
     **/
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /***
     * 构造一个数据库，如果没有就创建一个数据库
     ***/
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_TAB);
    }

    /**
     * 插入数据
     **/
    public void insert(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TAB_NAME, null, values);
//        db.close();
    }

    /***
     * 更新数据
     */
    public void update(ContentValues values, String mac) {
        SQLiteDatabase db = getWritableDatabase();
        db.update(TAB_NAME, values, "device_mac= " + "'" + mac + "'", null);
//        db.close();
    }

    /**
     * 删除数据
     */
    public void delete(String mac) {
        if (db == null) {
            db = getWritableDatabase();
        }
        int num = db.delete(TAB_NAME, "device_mac=?", new String[]{mac.toString()});
        LogUtils.debug("删除了" + num + "条数据");
    }

    /***
     * 查找数据
     */
    public Cursor query(String mac) {
        SQLiteDatabase db = getReadableDatabase();
        c = db.query(TAB_NAME, null, "device_mac=?", new String[]{mac.toString()}, null, null, null);
//        db.close();
        return c;
    }


    /***
     * 按时间降序查询
     **/
    public Cursor queryRecently() {
        SQLiteDatabase db = getReadableDatabase();
        c = db.query(TAB_NAME, null, null, null, null, null, "latest desc");
        return c;
    }

    /***
     * 关闭数据库
     ***/
    public void close() {
        if (db != null) {
            db.close();
            db = null;
        }
        if (c != null) {
            c.close();
            c = null;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

    }

}
