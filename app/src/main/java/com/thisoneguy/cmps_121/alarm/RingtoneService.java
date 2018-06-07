package com.thisoneguy.cmps_121.alarm;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.preference.PreferenceManager;
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
        //alarm will stop and go to new activity where the alarm will resume

        else if(this.isRunning && startId == 0){
            alarmsong.stop();
            alarmsong.reset();
            this.isRunning = false;
            this.startId = 0;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            //check which switch is on and run corresponding activity
            if (prefs.getBoolean("switch_mental", true)) {
                String gameChoice = prefs.getString("game_list", "0");
                switch (gameChoice){
                    case "0":
                        startActivity(new Intent(RingtoneService.this, math_activity.class));
                        break;
                    case "1":
                        startActivity(new Intent(RingtoneService.this, MovingButton.class));
                        break;
                    case "2":
                        startActivity(new Intent(RingtoneService.this, LightPuzzle.class));
                }

            }
            else if (prefs.getBoolean("switch_physical", true)){
                startActivity(new Intent(RingtoneService.this, GyroCheck.class));
            }
            //startActivity(new Intent(RingtoneService.this, math_activity.class));
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
