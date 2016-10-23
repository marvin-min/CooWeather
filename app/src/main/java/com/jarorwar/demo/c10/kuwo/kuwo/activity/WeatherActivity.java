package com.jarorwar.demo.c10.kuwo.kuwo.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.Toast;

import com.jarorwar.demo.c10.kuwo.kuwo.R;
import com.jarorwar.demo.c10.kuwo.kuwo.util.HttpCallBackListener;
import com.jarorwar.demo.c10.kuwo.kuwo.util.HttpUtil;
import com.jarorwar.demo.c10.kuwo.kuwo.util.Utility;

import java.util.Date;

public class WeatherActivity extends Activity {
    public static final String KEY = "10458efcf6b646399c9d4fd0537eff3b";
    public static final String URL = "https://api.heweather.com/x3/weather?key=10458efcf6b646399c9d4fd0537eff3b&cityid=";
    private TextView title_text;
    private TextView updatedAt;
    private TextView tmp;
    private TextView condText;
    private TextView today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        title_text = (TextView) findViewById(R.id.title_text);
        updatedAt = (TextView) findViewById(R.id.updated_at);
        tmp = (TextView) findViewById(R.id.tmp);
        condText = (TextView) findViewById(R.id.cond_text);
        today = (TextView) findViewById(R.id.today);
        title_text.setText(getIntent().getStringExtra("districtName"));
        String districtCode = getIntent().getStringExtra("districtCode");
        if(!TextUtils.isEmpty(districtCode)){
            querytWeather(districtCode);
        }else {
            showWeather();
        }
    }

    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        today.setText(prefs.getString("current",""));
        tmp.setText(prefs.getString("tmp",""));
        condText.setText(prefs.getString("cond",""));
        updatedAt.setText(prefs.getString("updatedAt",""));

    }

    private void querytWeather(String districtCode){
        HttpUtil.sendHttpRequest(URL+districtCode, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(WeatherActivity.this,response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(WeatherActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
