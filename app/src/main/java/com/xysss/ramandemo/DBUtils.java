package com.xysss.ramandemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


public class DBUtils {

    //查找custom表前几条数据
    public static ArrayList<CustomLibs> getCustormLimitDatas(MyDataBaseHelper myDataBaseHelper, int number, int startIndex) {

        String sql = "SELECT * FROM "+MyDataBaseHelper.TABLE_CUSTOM_NAME+" LIMIT ? OFFSET ?";
        SQLiteDatabase database = myDataBaseHelper.getReadableDatabase();
        ArrayList<CustomLibs> mResults = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(number), String.valueOf(startIndex)}, null);
        if (cursor.moveToFirst()) {
            do {
                int code = cursor.getInt(cursor.getColumnIndex(DBInfo.DB_SELF_MATERIAL_ID));
                String name = cursor.getString(cursor.getColumnIndex(DBInfo.DB_SELF_MATERIAL_NAME));
                String saveUrl = cursor.getString(cursor.getColumnIndex(DBInfo.DB_SELF_MATERIAL_URL));
                CustomLibs customLibs = new CustomLibs(code, name, saveUrl);
                mResults.add(customLibs);
            } while (cursor.moveToNext());
            cursor.close();
            return mResults;
        } else {
            cursor.close();
            return mResults;
        }
    }


    /**
     * 查询自建库中的对应数据；
     *
     * @param mDBHelper
     * @param code
     * @return
     */
    public static CustomLibs getCustomLibData(MyDataBaseHelper mDBHelper, int code) {
        CustomLibs customLibs;
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String flag = code + "";
        Cursor cursor = db.query(MyDataBaseHelper.TABLE_CUSTOM_NAME, null, DBInfo.DB_SELF_MATERIAL_ID+"=?",
                new String[]{flag}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int codeId = cursor.getInt(cursor.getColumnIndex(DBInfo.DB_SELF_MATERIAL_ID));
                String name = cursor.getString(cursor.getColumnIndex(DBInfo.DB_SELF_MATERIAL_NAME));
                String saveUrl = cursor.getString(cursor.getColumnIndex(DBInfo.DB_SELF_MATERIAL_URL));
                customLibs = new CustomLibs(codeId, name, saveUrl);
            } while (cursor.moveToNext());
            cursor.close();
            return customLibs;
        } else {
            cursor.close();
            return null;
        }
    }

    //查询所有自建库中数据
    public static ArrayList<CustomLibs> getAllLibraryDate(MyDataBaseHelper mDBHelper) {
        ArrayList<CustomLibs> mResults = new ArrayList<>();
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        //查询user表中所有数据
        Cursor cursor = db.query(MyDataBaseHelper.TABLE_CUSTOM_NAME, null, null, null, DBInfo.DB_SELF_MATERIAL_ID,
                null, DBInfo.DB_SELF_MATERIAL_ID);
        if (cursor.moveToFirst()) {
            do {
                int code = cursor.getInt(cursor.getColumnIndex(DBInfo.DB_SELF_MATERIAL_ID));
                String name = cursor.getString(cursor.getColumnIndex(DBInfo.DB_SELF_MATERIAL_NAME));
                String saveUrl = cursor.getString(cursor.getColumnIndex(DBInfo.DB_SELF_MATERIAL_URL));
                CustomLibs customLibs = new CustomLibs(code, name, saveUrl);
                mResults.add(customLibs);
            } while (cursor.moveToNext());
            cursor.close();
            return mResults;
        } else {
            cursor.close();
            return mResults;
        }
    }

    /**
     * 修改名称
     *
     * @param mDBHelper
     * @param speatrumsID
     * @param name
     */
    public static void upSelfLibraryName(MyDataBaseHelper mDBHelper, int speatrumsID, String name) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBInfo.DB_SELF_MATERIAL_NAME, name);
        String s = speatrumsID + "";
        db.update(MyDataBaseHelper.TABLE_CUSTOM_NAME, values, DBInfo.DB_SELF_MATERIAL_ID+"=?", new String[]{s});

    }

    /**
     * 查询数据库custom表中的总条数.
     *
     * @return
     */
    public static int getCustomNum(MyDataBaseHelper mDBHelper) {
        String sql = "select count(*) from "+MyDataBaseHelper.TABLE_CUSTOM_NAME;
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        //查询user表中所有数据
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0) + 1;
        cursor.close();
        return (int) count;
    }


    //删除指定id的自建库数据
    public static boolean deleteLibraryItem(MyDataBaseHelper mDBHelper, int codeId) {
        String sql1 = "delete from "+MyDataBaseHelper.TABLE_CUSTOM_NAME+" where "+DBInfo.DB_SELF_MATERIAL_ID+" ='" + codeId + "'";
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql1, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    /**
     * 设置数据到自建库的数据库
     *
     * @param mDBHelper
     * @param cLib
     * @return
     */
    public static boolean setCustomToDB(MyDataBaseHelper mDBHelper, CustomLibs cLib) {
        if (!isExist(mDBHelper, cLib.getCode())) {
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            //values.put(DBInfo.DB_SELF_MATERIAL_ID, cLib.getCode());
            values.put(DBInfo.DB_SELF_MATERIAL_NAME, cLib.getName());
            values.put(DBInfo.DB_SELF_MATERIAL_URL, cLib.getSaveUrl());
            db.insert(MyDataBaseHelper.TABLE_CUSTOM_NAME, null, values);
            values.clear();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询custom表中是否有当前的codeId;
     *
     * @param mDBHelper
     * @param codeId
     * @return
     */
    public static boolean isExist(MyDataBaseHelper mDBHelper, int codeId) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        //查询user表中是否存在有数据
        String ss = codeId + "";
        Cursor cursor = db.query(MyDataBaseHelper.TABLE_CUSTOM_NAME, null, DBInfo.DB_SELF_MATERIAL_ID+"=?",
                new String[]{ss}, null, null, null);
        if (false == cursor.moveToFirst()) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }
}
