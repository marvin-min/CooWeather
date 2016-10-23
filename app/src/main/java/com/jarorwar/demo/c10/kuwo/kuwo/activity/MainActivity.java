package com.jarorwar.demo.c10.kuwo.kuwo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jarorwar.demo.c10.kuwo.kuwo.R;
import com.jarorwar.demo.c10.kuwo.kuwo.model.City;
import com.jarorwar.demo.c10.kuwo.kuwo.model.District;
import com.jarorwar.demo.c10.kuwo.kuwo.model.Province;
import com.jarorwar.demo.c10.kuwo.kuwo.util.CoolWeatherDB;
import com.jarorwar.demo.c10.kuwo.kuwo.util.HttpCallBackListener;
import com.jarorwar.demo.c10.kuwo.kuwo.util.HttpUtil;
import com.jarorwar.demo.c10.kuwo.kuwo.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    public static final String HOST = "http://112.74.35.68:9080";
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_DISTRICT = 2;
    public static final String PROVINCE_TYPE = "province";
    public static final String CITY_TYPE = "city";
    public static final String DISTRICT_TYPE = "district";


    private ProgressDialog mProgressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB mCoolWeatherDB;
    private List<String> dataList = new ArrayList<>();
    private List<Province> provinces;
    private List<City> cities;
    private List<District> districts;

    private Province selectedProvince;
    private City selectedCity;
    private District selectedDistrict;
    private int currentLevel;
    private boolean isSelectedSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isSelectedSource = WeatherActivity.SOURCE.equals(getIntent().getStringExtra("source"));
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("city_selected",false) && !isSelectedSource){
            Intent intent = new Intent(this,WeatherActivity.class);
            intent.putExtra("districtName", prefs.getString("districtName",""));
            intent.putExtra("districtCode", prefs.getString("districtCode",""));
                    startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        titleText = (TextView) findViewById(R.id.title_text);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        mCoolWeatherDB = CoolWeatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinces.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cities.get(position);
                    queryDistinct();
                } else if (currentLevel == LEVEL_DISTRICT) {
                    selectedDistrict = districts.get(position);
                    String fullName = selectedProvince.getName() + "." + selectedCity.getName() + "." + selectedDistrict.getName();
//                    Toast.makeText(MainActivity.this,  , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                    intent.putExtra("districtName", fullName);
                    intent.putExtra("districtCode", "CN101" + selectedProvince.getCode() + selectedCity.getCode() + selectedDistrict.getCode());
                    startActivity(intent);
                    finish();
                }
            }
        });
        queryProvinces();
    }


    private void queryProvinces() {
        provinces = mCoolWeatherDB.getAllProvinces();
        if (provinces.size() > 0) {
            dataList.clear();
            for (Province province : provinces) {
                dataList.add(province.getName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer(null, null, PROVINCE_TYPE);
        }

    }


    private void queryCities() {
        cities = mCoolWeatherDB.getAllCitiesByProvince(selectedProvince.getCode());
        if (cities.size() > 0) {
            dataList.clear();
            for (City city : cities) {
                dataList.add(city.getName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getName());
            currentLevel = LEVEL_CITY;
        } else {
            queryFromServer(selectedProvince.getCode(), null, CITY_TYPE);
        }
    }


    private void queryDistinct() {
        districts = mCoolWeatherDB.getAllDistrictsByProvinceAndCity(selectedProvince.getCode(), selectedCity.getCode());
        if (districts.size() > 0) {
            dataList.clear();
            for (District district : districts) {
                dataList.add(district.getName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getName() + "." + selectedCity.getName());
            currentLevel = LEVEL_DISTRICT;
        } else {
            queryFromServer(selectedProvince.getCode(), selectedCity.getCode(), DISTRICT_TYPE);
        }
    }


    private void queryFromServer(final String provinceCode, final String cityCode, final String type) {
        showProgressDialog();
        String url = null;

        switch (type) {
            case PROVINCE_TYPE:
                url = HOST + "/provinces";
                break;
            case CITY_TYPE:
                url = HOST + "/cities-" + provinceCode;
                break;
            case DISTRICT_TYPE:
                url = HOST + "/district-" + provinceCode + "-" + cityCode;
                break;
        }
        url += ".json";
        HttpUtil.sendHttpRequest(url, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                switch (type) {
                    case PROVINCE_TYPE:
                        result = Utility.handleProvinces(mCoolWeatherDB, response);
                        break;
                    case CITY_TYPE:
                        result = Utility.handleCities(mCoolWeatherDB, response);
                        break;
                    case DISTRICT_TYPE:
                        result = Utility.handleDistricts(mCoolWeatherDB, response);
                        break;
                }

                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            switch (type) {
                                case PROVINCE_TYPE:
                                    queryProvinces();
                                    break;
                                case CITY_TYPE:
                                    queryCities();
                                    break;
                                case DISTRICT_TYPE:
                                    queryDistinct();
                                    break;
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(MainActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void closeProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在加载数据.。。");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_DISTRICT) {
            queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        } else {
            if(isSelectedSource){
                Intent intent = new Intent(this,WeatherActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }


}
