package com.jarorwar.demo.c10.kuwo.kuwo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.FeatureInfo;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jarorwar.demo.c10.kuwo.kuwo.R;
import com.jarorwar.demo.c10.kuwo.kuwo.service.AutoUpdateService;
import com.jarorwar.demo.c10.kuwo.kuwo.util.HttpCallBackListener;
import com.jarorwar.demo.c10.kuwo.kuwo.util.HttpUtil;
import com.jarorwar.demo.c10.kuwo.kuwo.util.Utility;

import java.util.Date;

public class WeatherActivity extends Activity {
    public static final String KEY = "10458efcf6b646399c9d4fd0537eff3b";
    public static final String URL = "https://api.heweather.com/x3/weather?key=10458efcf6b646399c9d4fd0537eff3b&cityid=";
    public static final String SOURCE = "select_city";
    private TextView title_text;
    private TextView updatedAt;
    private TextView tmp;
    private TextView condText;
    private TextView today;
    private Button selectCity;
    private ImageButton refreshWeather;
    private ToggleButton autoUpdateEnable;
    private boolean enableAutoUpdate = false;

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

        refreshWeather = (ImageButton) findViewById(R.id.refresh_air);
        selectCity = (Button) findViewById(R.id.selected_city);
        autoUpdateEnable = (ToggleButton) findViewById(R.id.auto_update_cotroll_button);
        autoUpdateEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                enableAutoUpdate = isChecked;
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putBoolean("enableAutoUpdate", isChecked);
                editor.commit();
                startAutoUpdate();
            }
        });

        selectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
                intent.putExtra("source", SOURCE);
                startActivity(intent);
                finish();
            }
        });

        refreshWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshWeather();
            }
        });

        refreshWeather();
    }

    private void refreshWeather() {
        String districtCode = getIntent().getStringExtra("districtCode");
        if (!TextUtils.isEmpty(districtCode)) {
            querytWeather(districtCode);
        } else {
            showWeather();
        }
    }

    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        title_text.setText(prefs.getString("districtName", ""));
        today.setText(prefs.getString("current", ""));
        tmp.setText(prefs.getString("tmp", ""));
        condText.setText(prefs.getString("cond", ""));
        updatedAt.setText(prefs.getString("updatedAt", ""));
        enableAutoUpdate = prefs.getBoolean("enableAutoUpdate", false);

        startAutoUpdate();

    }

    private void startAutoUpdate() {
        Intent intet = new Intent(WeatherActivity.this, AutoUpdateService.class);
        if (enableAutoUpdate) {
            startService(intet);
        }
    }

    private void querytWeather(String districtCode) {
        updatedAt.setText("同步中...");
        HttpUtil.sendHttpRequest(URL + districtCode, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(WeatherActivity.this, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(WeatherActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
