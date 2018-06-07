package com.thisoneguy.cmps_121.alarm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MovingButton extends AppCompatActivity {
    public final String TAG = "Moving Button Game: ";
    public int intervalInMilliseconds = 3000;
    public int lowerLimit = 1000; // this can be lowered to 500 but it becomes much more difficult. Dunno if a sleepy person could do it.
    public int speedIncreasePotency = 500;
    public Timer timer;
    MediaPlayer alarmsong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        alarmsong = MediaPlayer.create(this, R.raw.alarm_sound);
        alarmsong.setLooping(true);
        alarmsong.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_button);

        startRandomButton(intervalInMilliseconds);
    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    public void tapMovingButton(View view) {
        if (intervalInMilliseconds > lowerLimit) {
            intervalInMilliseconds = intervalInMilliseconds - speedIncreasePotency;
            timer.cancel();
            timer.purge();
            startRandomButton(intervalInMilliseconds);
        } else {
            timer.cancel();
            timer.purge();
            //victory condition, snooze alarm
            alarmsong.stop();
            alarmsong.reset();
            //go back to main screen
            Intent mainScreen = new Intent(this, MainActivity.class);
            mainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainScreen);
            finish();
        }
    }

    private void startRandomButton(int interval) {
        //final Button movingButton = button;
        timer = new Timer();
        Log.d(TAG, "----New timer task----");
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        setButtonRandomPosition();
                    }
                });

            }
        }, 0, interval);
    }

    public static Point getDisplaySize(@NonNull Context context) {
        Point point = new Point();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getSize(point);
        return point;
    }

    private void setButtonRandomPosition(){
        Button button = findViewById(R.id.moving_button);
        int randomX = new Random().nextInt(((getDisplaySize(this).x) - 300)); //trying to buffer the right side so the button stays on screen. These alterations don't seem to take effect. Not sure why.
        int randomY = new Random().nextInt(((getDisplaySize(this).y) - 400)); //trying to buffer the bottom side so the button stays on screen.
        Log.d(TAG, "X coord: " + Integer.toString(randomX) + " Y coord: " + Integer.toString(randomY));

        button.setX(randomX + 100);// These alterations don't seem to take effect. Not sure why.
        button.setY(randomY + 100);
    }
}
