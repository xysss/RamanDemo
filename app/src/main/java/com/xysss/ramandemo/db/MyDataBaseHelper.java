package com.xysss.ramandemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDataBaseHelper extends SQLiteOpenHelper {
    private static MyDataBaseHelper mInstance = null;
    public static final int DATABASE_VERSION  = 1;  //数据库版本
    public static final String DATABASE_NAME = "Raman.db";
    public static final String TABLE_CUSTOM_NAME = "RamanLibrary";  //自建库数据表
    public static final String DropCustom="drop table if exists "+TABLE_CUSTOM_NAME;

    public MyDataBaseHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION );
    }

    /**单例模式**/
    public static synchronized MyDataBaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyDataBaseHelper(context);
        }
        return mInstance;
    }

    /**
     * 删除数据库
     * @param context
     *  @return
     */
    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(DATABASE_NAME);
    }
    public static final String CREATE_CUSTOM_LIBS = "create table "+TABLE_CUSTOM_NAME+"(" +
            DBInfo.DB_SELF_MATERIAL_ID+" integer primary key autoincrement," +
            DBInfo.DB_SELF_MATERIAL_URL+" text," +
            DBInfo.DB_SELF_MATERIAL_NAME+" text)";
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CUSTOM_LIBS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DropCustom);
        onCreate(db);
        }
}
