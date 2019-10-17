package com.okwx.xiao.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.okwx.xiao.okwx.App;
import com.okwx.xiao.entity.Key;

import java.util.ArrayList;
import java.util.List;

//   好友记录表
public class DBDao {

    public static final String TABLE_NAME = "key";//表名

    private static final String ID = "id";//id自增长
    private static final String NAME = "stu_name";//姓名

    private DBHelper dbHelper;

    //创建表结构
    public static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            ID + " integer primary key autoincrement," +
            NAME + " text" +
            ")";


    private DBDao() {
        dbHelper = new DBHelper(App.getContext());
    }

    public static DBDao getInstance() {
        return InnerDB.instance;
    }

    private static class InnerDB {
        private static DBDao instance = new DBDao();
    }

    /**
     * 数据库插入数据
     *
     * @param bean 实体类
     * @param <T>  T
     */
    public synchronized <T> void insert(T bean) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (bean != null && bean instanceof Key) {
                Key student = (Key) bean;
                ContentValues cv = new ContentValues();
                cv.put(NAME, student.getName());
                db.insert(TABLE_NAME, null, cv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    /**
     * 删除表中所有的数据
     */
    public synchronized void clearAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "delete from " + TABLE_NAME;

        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    /**
     * 查询数据
     *
     * @return List
     */
    public synchronized <T> List<T> query() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<T> list = new ArrayList<>();
        String querySql = "select * from " + TABLE_NAME;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(querySql, null);
            while (cursor.moveToNext()) {
                Key student = new Key();
                student.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                list.add((T) student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }

    public long getCount(String name)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String querySql = "select count(*) from " + TABLE_NAME+" where stu_name = '"+name+"' ";
        Cursor cursor = null;
        try {
            Log.i(TABLE_NAME,querySql);
            Cursor cur = db.rawQuery(querySql, null);
            cur.moveToFirst();
            long result = cur.getLong(0);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return 0;
    }

}
