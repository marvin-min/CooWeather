package com.jarorwar.demo.c10.kuwo.kuwo.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.jarorwar.demo.c10.kuwo.kuwo.activity.WeatherActivity;
import com.jarorwar.demo.c10.kuwo.kuwo.receiver.AutoUpdateReceiver;
import com.jarorwar.demo.c10.kuwo.kuwo.util.HttpCallBackListener;
import com.jarorwar.demo.c10.kuwo.kuwo.util.HttpUtil;
import com.jarorwar.demo.c10.kuwo.kuwo.util.Utility;

/**
 * Created by marvinmin on 10/23/16.
 */

public class AutoUpdateService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("enableAutoUpdate", false)) {
            stopSelf(startId);
        } else {
            final String districtCode = prefs.getString("districtCode", "");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    updateWeather(districtCode);
                    System.out.println("=======onStartCommand=======");
                }
            }).start();
            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            int anHour = 60 * 60 * 1000;
            long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
            Intent i = new Intent(this, AutoUpdateReceiver.class);
            PendingIntent pendIntent = PendingIntent.getBroadcast(this, 0, i, 0);
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendIntent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather(String districtCode) {
        System.out.println("=======updateWeather=======");
        String url = WeatherActivity.URL + districtCode;
        HttpUtil.sendHttpRequest(url, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(AutoUpdateService.this, response);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

    }

}
