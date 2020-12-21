package com.kashisol.fonediagnosis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLHelper extends SQLiteOpenHelper {

    private static final String dbname = "MobTestdb";
    private static final int version = 1;

    public MySQLHelper(Context context) {
        super(context, dbname, null, version);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table Test(_id integer primary key autoincrement, Name text, Value text, Result text)";
        db.execSQL(sql);
    }

    public void insertData(String name, String value, String res, SQLiteDatabase database) {

        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Value", value);
        values.put("Result", res);
        database.insert("Test", null, values);
    }

    public int searchName(String name, SQLiteDatabase database) {
        Cursor cursor = database.rawQuery("select * from Test where Name=?", new String[]{name});

        try {
            if (cursor != null) {

                cursor.moveToFirst();

                return cursor.getInt(0);


            }
        } catch (Exception e) {
            return 0;
        }

        return 0;
    }

    public void updateData(int id, String value, String res, SQLiteDatabase database) {

        ContentValues values = new ContentValues();
        values.put("Value", value);
        values.put("Result", res);
        database.update("Test", values, "_id=?", new String[]{String.valueOf(id)});
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
