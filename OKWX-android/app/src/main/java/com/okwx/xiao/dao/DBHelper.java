package com.okwx.xiao.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.okwx.xiao.entity.User;

//库
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "addFirend.db";

    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DBDao.SQL_CREATE_TABLE);
        database.execSQL(UserDao.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }
}
