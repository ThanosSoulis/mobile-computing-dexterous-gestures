package com.example.dexterous_gestures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Random;

public class IdleActivity extends AppCompatActivity {
    private static final String TAG = "IdleActivity";

    private int startTime;
    private int regularStartTime;
    private int timeInterval;
    private int repetitions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle);

        handleSystemBars(true);
        readTimeSettings(getIntent());
        startPhoneCallTimer();
    }

    private void handleSystemBars(Boolean hide){

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        // Configure the behavior of the hidden system bars.
        assert windowInsetsController != null;
        windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        //Hide the system bars
        if (hide)
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
        else
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars());
    }

    private void readTimeSettings(Intent intent) {
        Bundle extras = intent.getExtras();

        this.startTime = extras.getInt(MainActivity.StartTime);
        this.timeInterval = extras.getInt(MainActivity.TimeInterval);
        this.regularStartTime = extras.getInt(MainActivity.RegularStartTime);
        this.repetitions = extras.getInt(MainActivity.Repetitions);
    }

    //Need to run this on the main thread, as we are trying to start an activity
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            Bundle bundle = new Bundle();
            bundle.putInt(MainActivity.StartTime, startTime);
            bundle.putInt(MainActivity.RegularStartTime, timeInterval);
            bundle.putInt(MainActivity.TimeInterval, regularStartTime);
            //Decrease the number of repetitions when starting a PhoneCall
            bundle.putInt(MainActivity.Repetitions, repetitions - 1);

            Intent intent = new Intent(IdleActivity.this, PhoneCallActivity.class);
            intent.putExtras(bundle);

            startActivityForResult(intent,2);
            finish();
        }
    };


    private void startPhoneCallTimer(){

        long randomInterval = (long) (Math.random() * timeInterval * 1000L);
        boolean fromMain = getCallingActivity().getClassName().equals(MainActivity.class.getName());

        long interval;
        if(fromMain)
            interval = randomInterval + startTime * 1000L;
        else
            interval = randomInterval + regularStartTime * 1000L;

        mHandler.postDelayed(mUpdateTimeTask, interval);

        //TODO Log interval in seconds
        Log.d(TAG, "Start Interval: "+ (float)(interval/1000f));
    }


    @Override
    public void onBackPressed() {
        // Commenting out the super disables the back functionality
        //super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handleSystemBars(false);
    }
}