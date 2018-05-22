package com.dai.utils.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dai on 2017/9/14.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "mytest.db";
    public static final String TABLE_NAME = "table1";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String SEX = "sex";


    private static final String CREATE_TABLE = "create table if not exists "
            + TABLE_NAME
            + "(" + ID + " integer primary key AUTOINCREMENT,"
            + NAME + " text,"
            + AGE + " integer,"
            + SEX + " text)";

    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    SQLiteHelper(Context context) {
        super(context,DB_NAME, null, DB_VERSION);
    }

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE);
    }

}
