package com.jarorwar.demo.c10.kuwo.kuwo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.jarorwar.demo.c10.kuwo.kuwo.service.AutoUpdateService;

/**
 * Created by marvinmin on 10/23/16.
 */

public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "onReceive: ");
        Intent i = new Intent(context, AutoUpdateService.class);
        context.startService(i);
    }
}
