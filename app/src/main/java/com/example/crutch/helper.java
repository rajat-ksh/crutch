package com.example.crutch;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class helper  extends SQLiteOpenHelper {
    private static String dbname="Userdb";
    private static int version=1;
    public helper(Context context){
        super(context,dbname,null,version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="Create Table users( Id INTEGER Primary key AutoIncrement, mob_no text,password text)";
        db.execSQL(sql);
        ContentValues values=new ContentValues();
        values.put("mob_no","98889888");
        values.put("password","12345");
        db.insert("users",null ,values);
    }
    public static void insert(String mobno, String password){
        ContentValues values=new ContentValues();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
