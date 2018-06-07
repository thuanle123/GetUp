package com.thisoneguy.cmps_121.alarm;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class GyroCheck extends AppCompatActivity {
    SensorManager sensorManager;
    Sensor gyroscopeSensor;
    SensorEventListener rotationSensorListener;
    float[] rotationMatrix;
    float[] remappedRotationMatrix;
    float[] orientations;
    TextView gyroText;
    ConstraintLayout myLayout;
    MediaPlayer alarmsong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        alarmsong = MediaPlayer.create(this, R.raw.alarm_sound);
        alarmsong.setLooping(true);
        alarmsong.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyro_check);

        gyroText = findViewById(R.id.gyro_text);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        getWindow().getDecorView().setBackgroundColor(Color.RED);
        gyroText.setTextColor(Color.WHITE);
        myLayout = findViewById(R.id.gyro_layout);
    }

    @Override
    protected void onResume(){
        super.onResume();
        rotationSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // More code goes here

                rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);

                // Remap coordinate system
                remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        remappedRotationMatrix);

                // Convert to orientations
                orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);

                // Converting radians to degrees
                for(int i = 0; i < 3; i++) {
                    orientations[i] = (float)(Math.toDegrees(orientations[i]));
                }

                if ((orientations[2] < 45) && (orientations[2] > -45)){
                    myLayout.setBackgroundColor(Color.RED);
                    gyroText.setTextColor(Color.WHITE);
                } else if ((orientations[2] > 45) && (orientations[2] < 135)){
                    myLayout.setBackgroundColor(getResources().getColor(R.color.orange));
                    gyroText.setTextColor(Color.BLACK);
                } else if ((orientations[2] < -45) && (orientations[2] > -135)){
                    myLayout.setBackgroundColor(getResources().getColor(R.color.orange));
                    gyroText.setTextColor(Color.BLACK);
                } else if ((orientations[2] > 135) && (orientations[2] < 175)) {
                    myLayout.setBackgroundColor(Color.YELLOW);
                    gyroText.setTextColor(Color.BLACK);
                } else if ((orientations[2] < -135) && (orientations[2] > -175)) {
                    myLayout.setBackgroundColor(Color.YELLOW);
                    gyroText.setTextColor(Color.BLACK);
                } else if ((orientations[2] > 175) && (orientations[2] <= 180)) {
                    //Snooze alarm
                    //Go back to main screen
                    alarmsong.stop();
                    alarmsong.reset();
                    Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
                    mainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainScreen);

                } else if ((orientations[2] < -175) && (orientations[2] >= -180)) {
                    //Snooze alarm
                    //go back to the main screen
                    alarmsong.stop();
                    alarmsong.reset();
                    Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
                    mainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainScreen);

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        // Register the listener
        sensorManager.registerListener(rotationSensorListener,
                gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(rotationSensorListener);
    }
}
