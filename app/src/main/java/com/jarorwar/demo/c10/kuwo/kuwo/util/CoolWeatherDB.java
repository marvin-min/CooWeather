package com.jarorwar.demo.c10.kuwo.kuwo.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jarorwar.demo.c10.kuwo.kuwo.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marvinmin on 10/18/16.
 */

public class CoolWeatherDB {
    private static final String DBNAME = "cool_weather";
    private static final String CITY_TABLE = "city";

    public static final int VERSION = 1;
    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DBNAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static CoolWeatherDB getInstance(Context context) {
        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    public void saveCity(City city) {
        if(city != null){
            ContentValues values = new ContentValues();
            values.put("city",city.getCity());
            values.put("city_en",city.getCityEn());
            values.put("city_id",city.getCityID());
            values.put("district",city.getDistrict());
            values.put("province",city.getProvince());
            db.insert(CITY_TABLE,null,values);
        }
    }

    public List<City> getAllProvinces(){
        Cursor cursor = db.query(CITY_TABLE,null,null,null,"province",null,"city_id asc",null);
        return buildCityListFromCursor(cursor);
    }


    public List<City> getAllCityByProvince(String province) {
        Cursor cursor = db.query(CITY_TABLE,null,"province = ?",new String[]{province},null,null,"city_id asc",null);
        return buildCityListFromCursor(cursor);
    }

    public List<City> getDistrictByProvinceAndCity(String province, String city) {
        Cursor cursor = db.query(CITY_TABLE,null,"province = ? and city = ?",new String[]{province,city},null,null,"city_id asc",null);
        return buildCityListFromCursor(cursor);
    }

    public City getDistrictByCityID(String cityID){
        Cursor cursor = db.query(CITY_TABLE,null,"city_id ?",new String[]{cityID},null,null,"city_id asc",null);
        return buildCityFromCursor(cursor);
    }

    private List<City> buildCityListFromCursor(Cursor cursor) {
        List<City> cities = new ArrayList<>();
        if(cursor.moveToFirst()){
            City city = buildCityFromCursor(cursor);
            cities.add(city);
        }
        return cities;
    }

    private City buildCityFromCursor(Cursor cursor) {
        City city = new City();
        city.setId(cursor.getInt(cursor.getColumnIndex("id")));
        city.setCityID(cursor.getString(cursor.getColumnIndex("city_id")));
        city.setCity(cursor.getString(cursor.getColumnIndex("city")));
        city.setCityEn(cursor.getString(cursor.getColumnIndex("city_en")));
        city.setDistrict(cursor.getString(cursor.getColumnIndex("district")));
        city.setProvince(cursor.getString(cursor.getColumnIndex("province")));
        return city;
    }
}
