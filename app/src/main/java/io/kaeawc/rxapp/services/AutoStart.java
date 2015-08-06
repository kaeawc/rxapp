package io.kaeawc.rxapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStart extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        if (context == null) {
            return;
        }

        Intent foreverIntent = new Intent(context, ForeverService.class);
        context.startService(foreverIntent);
    }
}