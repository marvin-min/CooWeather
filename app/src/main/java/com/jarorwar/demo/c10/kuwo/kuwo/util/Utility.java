package com.jarorwar.demo.c10.kuwo.kuwo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.DateFormat;

import com.jarorwar.demo.c10.kuwo.kuwo.model.AddressBase;
import com.jarorwar.demo.c10.kuwo.kuwo.model.City;
import com.jarorwar.demo.c10.kuwo.kuwo.model.District;
import com.jarorwar.demo.c10.kuwo.kuwo.model.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by marvinmin on 10/19/16.
 */

public class Utility {
    public synchronized static boolean handleProvinces(CoolWeatherDB weatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray provinces = new JSONArray(response.toString());
                for (int i = 0; i < provinces.length(); i++) {
                    JSONObject o = provinces.getJSONObject(i);
                    Province province = new Province();
                    province.setName(o.getString("name"));
                    province.setCode(o.getString("code"));
                    weatherDB.saveProvince(province);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public synchronized static boolean handleCities(CoolWeatherDB weatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray cities = new JSONArray(response.toString());
                for (int i = 0; i < cities.length(); i++) {
                    JSONObject o = cities.getJSONObject(i);
                    City city = new City();
                    city.setName(o.getString("name"));
                    city.setCode(o.getString("code"));
                    city.setProvinceCode(o.getString("province_code"));
                    weatherDB.saveCity(city);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public synchronized static boolean handleDistricts(CoolWeatherDB weatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray distircts = new JSONArray(response.toString());
                for (int i = 0; i < distircts.length(); i++) {
                    JSONObject o = distircts.getJSONObject(i);
                    District district = new District();
                    district.setName(o.getString("name"));
                    district.setCode(o.getString("code"));
                    district.setProvinceCode(o.getString("province_code"));
                    district.setCityCode(o.getString("city_code"));
                    weatherDB.saveDistrict(district);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public  static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray ja = new JSONArray(jsonObject.get("HeWeather data service 3.0").toString());
            JSONObject w = ja.getJSONObject(0);
//            JSONObject aqi = (JSONObject) w.get("aqi");
            JSONObject basic = (JSONObject) w.get("basic");
//            JSONArray dailyForecastes = new JSONArray(w.get("daily_forecast").toString());
//            JSONArray hourlyForecast = new JSONArray(w.get("hourly_forecast").toString());
            JSONObject now = (JSONObject) w.get("now");
//            JSONObject suggestion = (JSONObject) w.get("suggestion");
            String updatedAt = (String) ((JSONObject)basic.get("update")).get("loc");
            String cond = (String) ((JSONObject)now.get("cond")).get("txt");
            String tmp = now.get("tmp").toString();
            String cityCode = basic.getString("id");
            String cityName = basic.getString("city");
            saveWeatherInfo(context,updatedAt,cond,tmp,cityName,cityCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void saveWeatherInfo(Context context,String updatedAt, String cond, String tmp,String cityName,String cityCode) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("updatedAt",updatedAt);
        editor.putString("cond",cond);
        editor.putString("tmp",tmp);
        editor.putBoolean("city_selected",true);
        editor.putString("districtName",cityName);
        editor.putString("districtCode",cityCode);
        editor.putString("current",(String) DateFormat.format("yyyy年MM月dd日",new Date()));
        editor.commit();
    }

}
