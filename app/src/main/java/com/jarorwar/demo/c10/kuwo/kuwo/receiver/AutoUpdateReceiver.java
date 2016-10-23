package com.jarorwar.demo.c10.kuwo.kuwo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jarorwar.demo.c10.kuwo.kuwo.service.AutoUpdateService;

import java.sql.Time;
import java.util.Date;

/**
 * Created by marvinmin on 10/23/16.
 */

public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AutoUpdateReceiver", "----123----");
        Intent i = new Intent(context, AutoUpdateService.class);
        context.startService(i);
    }
}
