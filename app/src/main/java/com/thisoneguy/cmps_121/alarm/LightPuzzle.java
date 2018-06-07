package com.thisoneguy.cmps_121.alarm;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class LightPuzzle extends AppCompatActivity {
    public boolean[] lights = {false, true, false, true, false, false};
    public Button[] buttons= {null, null, null, null, null, null};
    MediaPlayer alarmsong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        alarmsong = MediaPlayer.create(this, R.raw.alarm_sound);
        alarmsong.setLooping(true);
        alarmsong.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_puzzle);

        Random random = new Random();
        ConstraintLayout layout = findViewById(R.id.lights_layout);
        for (int i = 0; i < layout.getChildCount(); i++) {
            lights[i] = random.nextBoolean();
            Button currentButton = (Button) layout.getChildAt(i);
            buttons[i] = (Button) layout.getChildAt(i);
            changeButtonColor(currentButton, lights[i]);
        }

    }

    public void lightTapped(View view) {
        Button pressedButton = (Button) view;

        if (pressedButton.getId() == findViewById(R.id.light_0).getId()) {
            adjustLightAdjacencies(0);
        } else if (pressedButton.getId() == findViewById(R.id.light_1).getId()) {
            adjustLightAdjacencies(1);
        } else if (pressedButton.getId() == findViewById(R.id.light_2).getId()) {
            adjustLightAdjacencies(2);
        } else if (pressedButton.getId() == findViewById(R.id.light_3).getId()) {
            adjustLightAdjacencies(3);
        } else if (pressedButton.getId() == findViewById(R.id.light_4).getId()) {
            adjustLightAdjacencies(4);
        } else if (pressedButton.getId() == findViewById(R.id.light_5).getId()) {
            adjustLightAdjacencies(5);
        }

        checkForCompletion();
    }

    public void changeButtonColor(Button button, boolean state) {
        if (state) {
            button.setBackgroundColor(Color.GREEN);
        } else {
            button.setBackgroundColor(Color.YELLOW);
        }
    }

    public void adjustLightAdjacencies(int index) {
        if (index == 0) {
            switchSingleLight(0);
            changeButtonColor(buttons[0], lights[0]);
            switchSingleLight(1);
            changeButtonColor(buttons[1], lights[1]);
        } else if (index == 5) {
            switchSingleLight(5);
            changeButtonColor(buttons[5], lights[5]);
            switchSingleLight(4);
            changeButtonColor(buttons[4], lights[4]);
        } else {
            switchSingleLight((index -1));
            changeButtonColor(buttons[index -1], lights[index -1]);
            switchSingleLight(index);
            changeButtonColor(buttons[index], lights[index]);
            switchSingleLight((index +1));
            changeButtonColor(buttons[index +1], lights[index +1]);
        }
    }

    public void switchSingleLight(int index) {
        if (lights[index]) {
            lights[index] = false;
        } else {
            lights[index] = true;
        }
    }

    public void checkForCompletion() {
        boolean complete = true;
        for (int i = 0; i < lights.length; i++) {
            if (lights[i] == false) {
                complete = false;
            }
        }
        if (complete) {
            //Victory condition, snooze alarm
            alarmsong.stop();
            alarmsong.reset();
            //go back to main screen
            Intent mainScreen = new Intent(this, MainActivity.class);
            mainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainScreen);
            finish();
        }
    }
}
