package com.jarorwar.demo.c10.kuwo.kuwo.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marvinmin on 10/18/16.
 */

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
    public static String CREATE_CITY_TABLE = "create table city (" +
            "    id integer primary key autoincrement," +
            "     city text," +
            "     city_en text," +
            "     city_id text," +
            "     district text," +
            "     province text)";

    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
