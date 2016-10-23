package com.jarorwar.demo.c10.kuwo.kuwo.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jarorwar.demo.c10.kuwo.kuwo.model.City;
import com.jarorwar.demo.c10.kuwo.kuwo.model.District;
import com.jarorwar.demo.c10.kuwo.kuwo.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marvinmin on 10/18/16.
 */

public class CoolWeatherDB {
    private static final String DBNAME = "cool_weather";
    private static final String PROVINCES_TABLE = "provinces";
    private static final String CITIES_TABLE = "cities";
    private static final String DISTRICTS_TABLE = "districts";

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

    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("name", province.getName());
            values.put("code", province.getCode());
            db.insert(PROVINCES_TABLE, null, values);
        }
    }

    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("name", city.getName());
            values.put("code", city.getCode());
            values.put("province_code", city.getProvinceCode());
            db.insert(CITIES_TABLE, null, values);
        }
    }

    public void saveDistrict(District district) {
        if (district != null) {
            ContentValues values = new ContentValues();
            values.put("name", district.getName());
            values.put("code", district.getCode());
            values.put("province_code", district.getProvinceCode());
            values.put("city_code", district.getCityCode());
            values.put("city_en", district.getCityEN());
            db.insert(DISTRICTS_TABLE, null, values);
        }
    }


    public List<Province> getAllProvinces() {
        Cursor cursor = db.query(PROVINCES_TABLE, null, null, null, null, null, "code asc", null);
        List<Province> provinces = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                provinces.add(buildProvinceFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return provinces;
    }

    public List<City> getAllCitiesByProvince(String province_code) {
        Cursor cursor = db.query(CITIES_TABLE, null, "province_code = ?", new String[]{province_code}, null, null, "code asc", null);
        List<City> cities = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                cities.add(buildCityFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return cities;
    }

    public List<District> getAllDistrictsByProvinceAndCity(String province_code, String city_code) {
        Cursor cursor = db.query(DISTRICTS_TABLE, null, "province_code = ? and city_code = ?", new String[]{province_code, city_code}, null, null, "code asc", null);
        List<District> districts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                districts.add(buildDistrictFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return districts;
    }

    private List<City> buildCityListFromCursor(Cursor cursor) {
        List<City> cities = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                City city = buildCityFromCursor(cursor);
                cities.add(city);
            } while (cursor.moveToNext());
        }
        return cities;
    }

    private Province buildProvinceFromCursor(Cursor cursor) {
        Province province = new Province();
        province.setId(cursor.getInt(cursor.getColumnIndex("id")));
        province.setName(cursor.getString(cursor.getColumnIndex("name")));
        province.setCode(cursor.getString(cursor.getColumnIndex("code")));
        return province;
    }

    private City buildCityFromCursor(Cursor cursor) {
        City city = new City();
        city.setId(cursor.getInt(cursor.getColumnIndex("id")));
        city.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
        city.setName(cursor.getString(cursor.getColumnIndex("name")));
        city.setCode(cursor.getString(cursor.getColumnIndex("code")));
        return city;
    }

    private District buildDistrictFromCursor(Cursor cursor) {
        District district = new District();
        district.setId(cursor.getInt(cursor.getColumnIndex("id")));
        district.setName(cursor.getString(cursor.getColumnIndex("name")));
        district.setCode(cursor.getString(cursor.getColumnIndex("code")));
        district.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
        district.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
        return district;
    }

}
