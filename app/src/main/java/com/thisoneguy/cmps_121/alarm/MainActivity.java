package com.thisoneguy.cmps_121.alarm;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    // Alarm Manager
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;
    private final String TAG = "TESTGPS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this; //Is this necessary? "this" should already be "this"

        // This statement requests permission from the user.
        // If permissions are not set in the Manifest file, then access
        // will automatically be denied. Once the user chooses an option,
        // onRequestPermissionsResult is called.
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                99);

        // initialize our alarm manager
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // initialize all our time picker
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);

        // initialize text update box
        update_text = (TextView) findViewById(R.id.Update_Alarm);

        // create an instance of a calendar
        final Calendar calendar = Calendar.getInstance();

        //create an intent
        final Intent intent = new Intent(this.context, Alarm.class);

        // initialize Set Alarm
        Button alarm_on = (Button) findViewById(R.id.Set_Alarm);
        // create an onClickListener to start the alarm

        //finished by Thuan Le
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // method the changes the update text Textbox
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                // convert the int values to strings

                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                if (hour > 12)
                {
                    hour_string = String.valueOf(hour -12);
                }

                if (minute < 10)
                {
                    minute_string = "0" + String.valueOf(minute);
                }

                set_alarm_test("Alarm set to: " + hour_string + ":" + minute_string);

                //put extra info in intent(Tells clock that alarm on button was pressed)
                intent.putExtra("extra", "alarm on");

                // create a pending intent which delays the intent
                // until the specified calendar time
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                //set the alarm manager
                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
            }
        });



        // initialize Cancel Alarm
        Button alarm_off = (Button) findViewById(R.id.End_Alarm);

        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set the update that change the update text TextBox
                set_alarm_test("Alarm off!");

                //Cancels the alarm
                if (pending_intent != null) { //Needs this check to prevent null pointer exceptions
                    alarm_manager.cancel(pending_intent);
                }

                //put extra info into intent(Tells clock that alarm off was pressed)
                intent.putExtra("extra", "alarm off");

                //stop ringtone
                sendBroadcast(intent);
            }
        });


        //goes to math activity for now
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                //check which switch is on and run corresponding activity
                if (prefs.getBoolean("switch_mental", true)) {
                    String gameChoice = prefs.getString("game_list", "0");
                    switch (gameChoice){
                        case "0":
                            startActivity(new Intent(MainActivity.this, math_activity.class));
                            break;
                        case "1":
                            startActivity(new Intent(MainActivity.this, MovingButton.class));
                            break;
                        case "2":
                            startActivity(new Intent(MainActivity.this, LightPuzzle.class));
                    }

                }
                else if (prefs.getBoolean("switch_physical", true)){
                    startActivity(new Intent(MainActivity.this, GyroCheck.class));
                }

                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });


    }

    private void set_alarm_test(String output) {
        update_text.setText(output);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        } else if (id == R.id.moving_button) {
            Intent movingButton = new Intent(this, MovingButton.class);
            startActivity(movingButton);
        } else if (id == R.id.light_puzzle) {
            Intent lightPuzzle = new Intent(this, LightPuzzle.class);
            startActivity(lightPuzzle);
        } else if (id == R.id.gyro) {
            Intent gyroActivity = new Intent(this, GyroCheck.class);
            startActivity(gyroActivity);
        } else if (id == R.id.math_problem) {
            Intent mathProblem = new Intent(this, math_activity.class);
            startActivity(mathProblem);
        }

        return super.onOptionsItemSelected(item);
    }

    // This class implements OnRequestPermissionsResultCallback, so when the
    // user is prompted for location permission, the below method is called
    // as soon as the user chooses an option.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "callback");
        switch (requestCode) {
            case 99:
                // If the permissions aren't set, then return. Otherwise, proceed.
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                                , 10);
                    }
                    Log.d(TAG, "returning program");
                    return;
                } else {
                    // Create Intent to reference MyService, start the Service.
                    Log.d(TAG, "starting service");
                    Intent i = new Intent(this, GPSService.class);
                    if (i == null)
                        Log.d(TAG, "intent null");
                    else {
                        startService(i);
                    }

                }
                break;
            default:
                break;
        }
    }



}
