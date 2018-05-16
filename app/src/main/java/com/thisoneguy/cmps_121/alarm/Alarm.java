package com.thisoneguy.cmps_121.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Receiver", "swag");

        //get the info from intent
        String intentInfo = intent.getExtras().getString("extra");

        //intent to the Ringtone service
        Intent service_intent = new Intent(context, RingtoneService.class);

        service_intent.putExtra("extra", intentInfo);
        //start service
        context.startService(service_intent);
    }
}
