package com.okwx.xiao.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.okwx.xiao.entity.User;
import com.okwx.xiao.okwx.App;

import java.util.ArrayList;
import java.util.List;

//用户表
public class UserDao {
    public static final String TABLE_NAME = "user";//表名

    private static final String ID = "id";//id自增长
    private static final String NAME = "user_no";
    private static final String PHONE = "cellphone";
    private static final String FANSSUM = "fans_sum";

    private DBHelper dbHelper;

    //创建表结构
    public static final String SQL_CREATE_TABLE = "create table if not exists " + TABLE_NAME + "(" +
            ID + " integer primary key autoincrement," +
            NAME + " text," +
            PHONE + " text," +
            FANSSUM + " integer" +
            ")";

    private UserDao() {
        dbHelper = new DBHelper(App.getContext());
    }

    public static UserDao getInstance() {
        return UserDao.InnerDB.instance;
    }

    private static class InnerDB {
        private static UserDao instance = new UserDao();
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
            if (bean != null && bean instanceof User) {
                User user = (User) bean;
                ContentValues cv = new ContentValues();
                cv.put(NAME, user.getWxNo());
                cv.put(PHONE, user.getCellphone());
                cv.put(FANSSUM, user.getFansSum());
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
                User user = new User();
                user.setCellphone(cursor.getString(cursor.getColumnIndex(PHONE)));
                user.setWxNo(cursor.getString(cursor.getColumnIndex(NAME)));
                user.setFansSum(cursor.getInt(cursor.getColumnIndex(FANSSUM)));
                list.add((T) user);
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

    /**
     * 修改粉丝量
     */
    public synchronized void updateFansum(Integer fansSum) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "update " + TABLE_NAME+" set "+FANSSUM+"= "+fansSum;
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public long getCount()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String querySql = "select count(*) from " + TABLE_NAME;
        Cursor cursor = null;
        try {
            System.out.println(querySql);
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

