package com.example.tommy.androidwear.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Tommy on 2017/9/29.
 */

public class DBHelper extends SQLiteOpenHelper {
    private final String TAG = "DBHelper";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database";
    public static final String TABLE_NAME = "listData";
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + TABLE_NAME + " (Acceleration Text )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public void insertData(SQLiteDatabase db, float data){
        db.execSQL("INSERT INTO " + TABLE_NAME + "(Acceleration) VALUES( " + data+"" + ")");
    }


    public void searchAll(SQLiteDatabase db){
        Cursor cursor=db.rawQuery(
                "SELECT * FROM listdata ", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String data = cursor.getString(0);
            Log.i(TAG, "searchAll: " + data);
            cursor.moveToNext();
        }
        cursor.close();
    }

}
