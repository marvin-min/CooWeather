package com.jarorwar.demo.c10.kuwo.kuwo.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marvinmin on 10/18/16.
 */

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
    public static final String CREATE_PROVINCES_TABLE = "create table provinces(" +
            "    id integer primary key autoincrement," +
            "    name text," +
            "    code text" +
            ")";
    public static final String CREATE_CITIES_TABLE = "create table cities(\n" +
            "    id integer primary key autoincrement,\n" +
            "    name text,\n" +
            "    code text,\n" +
            "    province_code\n" +
            ")";

    public static final String CREATE_DISTRICTS_TABLE = "create table districts(\n" +
            "    id integer primary key autoincrement,\n" +
            "    name text,\n" +
            "    code text,\n" +
            "    province_code,\n" +
            "    city_code,\n" +
            "    city_en\n" +
            ")";


    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCES_TABLE);
        db.execSQL(CREATE_CITIES_TABLE);
        db.execSQL(CREATE_DISTRICTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
