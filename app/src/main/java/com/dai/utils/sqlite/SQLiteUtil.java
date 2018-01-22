package com.dai.utils.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by dai on 2017/9/14.
 */

public class SQLiteUtil {

    private static final String TAG = "SQLiteUtil";

    private SQLiteHelper sqLiteHelper;

    // 列定义
    private static final String[] COLUMNS = new String[]{"id", "name", "age", "sex"};

    public SQLiteUtil(DatabaseContext context) {

        sqLiteHelper = new SQLiteHelper(context);
    }

    //查找所有数据
    public ArrayList<UserInfo> getAllData() {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = sqLiteHelper.getWritableDatabase();
            cursor = database.query(SQLiteHelper.TABLE_NAME, COLUMNS, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                ArrayList<UserInfo> userInfos = new ArrayList<>();
                while (cursor.moveToNext()) {
                    userInfos.add(parseUserInfo(cursor));
                }
                return userInfos;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }
        return null;
    }

    // 插入一条数据
    public boolean insertData(UserInfo userInfo) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = sqLiteHelper.getWritableDatabase();
            database.beginTransaction();
            ContentValues cv = new ContentValues();
//            cv.put(SQLiteHelper.ID, userInfo.getId());
            cv.put(SQLiteHelper.NAME, userInfo.getName());
            cv.put(SQLiteHelper.AGE, userInfo.getAge());
            cv.put(SQLiteHelper.SEX, userInfo.getSex());
            database.insert(SQLiteHelper.TABLE_NAME, null, cv);
            database.setTransactionSuccessful();
            return true;
        } catch (SQLiteConstraintException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (database != null) {
                database.endTransaction();
                database.close();
            }
        }
        return false;
    }

    //修改某一条数据
    public boolean updateUserInfo(UserInfo userInfo, String name) {
        SQLiteDatabase database = null;
        try {
            database = sqLiteHelper.getWritableDatabase();
            database.beginTransaction();
            // update userInfo set name = ? where name = getname
            ContentValues cv = new ContentValues();
            cv.put(SQLiteHelper.NAME, userInfo.getName());
            cv.put(SQLiteHelper.AGE, userInfo.getAge());
            cv.put(SQLiteHelper.SEX, userInfo.getSex());
            database.update(SQLiteHelper.TABLE_NAME, cv, SQLiteHelper.NAME + "= ?", new String[]{String.valueOf(name)});
            database.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (database != null) {
                database.endTransaction();
                database.close();
            }
        }
        return false;
    }

    //删除一条数据
    public boolean deleteData(int id) {
        SQLiteDatabase database = null;
        try {
            database = sqLiteHelper.getWritableDatabase();
            database.beginTransaction();
            database.delete(SQLiteHelper.TABLE_NAME, SQLiteHelper.ID + "= ?", new String[]{String.valueOf(id)});
            database.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (database != null) {
                database.endTransaction();
                database.close();
            }
        }
        return false;
    }

    // 查找某几条数据
    public ArrayList<UserInfo> findUserInfo(String name) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = sqLiteHelper.getWritableDatabase();
            // select * from Orders where CustomName = 'Bor'
            cursor = database.query(SQLiteHelper.TABLE_NAME, COLUMNS, SQLiteHelper.NAME + "=?", new String[]{name}, null, null, null);
            ArrayList<UserInfo> userInfos = new ArrayList<>();
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    userInfos.add(parseUserInfo(cursor));
                }
            }
            return userInfos;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }
        return null;
    }

    // 统计查询name 为（name）的用户总数
    public int getNameCount(String name) {
        int count = 0;
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = sqLiteHelper.getReadableDatabase();
            // select count(Id) from Orders where Country = 'China'
            cursor = database.query(SQLiteHelper.TABLE_NAME, new String[]{"COUNT(id)"}, "name = ?", new String[]{name}, null, null, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }
        return count;
    }

    //比较查询 此处查询比较年龄最小的用户
    public UserInfo getMinAgeUser() {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = sqLiteHelper.getReadableDatabase();
            // select Id, CustomName, Max(OrderPrice) as OrderPrice, Country from Orders
            cursor = database.query(SQLiteHelper.TABLE_NAME, new String[]{"id", "name", "Max(age) as age", "sex"}, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    System.out.println("SQLiteUtil.getMinAgeUser");
                    return parseUserInfo(cursor);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }
        System.out.println("SQLiteUtil.getMinAgeUser1111");
        return null;
    }

    //解析游标获取 UserInfo实例
    private UserInfo parseUserInfo(Cursor cursor) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.ID)));
        userInfo.setName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.NAME)));
        userInfo.setAge(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.AGE)));
        userInfo.setSex(cursor.getString(cursor.getColumnIndex(SQLiteHelper.SEX)));
        return userInfo;
    }
}
