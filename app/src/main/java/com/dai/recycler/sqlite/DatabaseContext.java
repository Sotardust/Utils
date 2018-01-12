package com.dai.recycler.sqlite;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.dai.recycler.BuildConfig;

import java.io.File;
import java.io.IOException;

/**
 * 用于支持对存储在SD卡上的数据库的访问
 **/
public class DatabaseContext extends ContextWrapper {

    /**
     * 构造函数
     *
     * @param base 上下文环境
     */
    public DatabaseContext(Context base) {
        super(base);
    }

    /**
     * 获得数据库路径，如果不存在，则创建对象对象
     *
     * @param name
     */
    @Override
    public File getDatabasePath(String name) {
        //判断是否存在sd卡
        boolean sdExist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        if (!sdExist) {//如果不存在,
            Log.e("SD卡管理：", "SD卡不存在，请加载SD卡");
            return null;
        } else {//如果存在
            //获取sd卡路径

            String dbDir = Environment.getExternalStorageDirectory() + "/" + BuildConfig.APPLICATION_ID + "/database";
            String dbPath = dbDir + "/" + name;//数据库路径
            //判断目录是否存在，不存在则创建该目录

            File dirFile = new File(dbDir.trim());
            if (!dirFile.exists())
                dirFile.mkdirs();
            //数据库文件是否创建成功
            boolean isFileCreateSuccess = false;
            //判断文件是否存在，不存在则创建该文件
            File dbFile = new File(dbPath.trim());
            if (!dbFile.exists())
                try {
                    dbFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            //返回数据库文件对象
            return dbFile;
        }
    }

    /**
     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
     *
     * @param name
     * @param mode
     * @param factory
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory) {
        System.out.println("openOrCreateDatabase name = " + name);
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }

    /**
     * Android 4.0会调用此方法获取数据库。
     *
     * @param name
     * @param mode
     * @param factory
     * @param errorHandler
     * @see android.content.ContextWrapper#openOrCreateDatabase(java.lang.String, int,
     * android.database.sqlite.SQLiteDatabase.CursorFactory,
     * android.database.DatabaseErrorHandler)
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory,
                                               DatabaseErrorHandler errorHandler) {
        System.out.println("openOrCreateDatabase11 name = " + name);
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }
} 