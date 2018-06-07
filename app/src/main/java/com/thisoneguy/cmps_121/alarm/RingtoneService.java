package com.thisoneguy.cmps_121.alarm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;


public class RingtoneService extends Service{
    private boolean isRunning;
    MediaPlayer alarmsong;
    private int startId;

    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        //get intent info
        String state = intent.getExtras().getString("extra");

        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }
        //no alarm playing & user pressed alarm on
        if(!this.isRunning && startId == 1) {
            alarmsong = MediaPlayer.create(this, R.raw.alarm_sound);
            alarmsong.setLooping(true);
            alarmsong.start();

            this.isRunning = true;
            this.startId = 0;
        }
        else if(this.isRunning && startId == 1){
            this.isRunning = true;
            this.startId = 1;
        }
        else if(!this.isRunning && startId == 0){
            this.isRunning = false;
            this.startId = 0;
        }
        //if alarm is playing & user presses alarm off
        //alarm will stop
        else if(this.isRunning && startId == 0){
            alarmsong.stop();
            alarmsong.reset();
            this.isRunning = false;
            this.startId = 0;
        }
        else {
            this.isRunning = false;
            this.startId = 0;
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRunning = false;
    }
}
