package com.example.dexterous_gestures;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private recognizingBackground sensor;
    private static long prevTime = 0;

    @SuppressLint("HandlerLeak")
    private static Handler resultHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();

//            if(bundle.containsKey(MSG_KEY)) {
//                String string = bundle.getString(MSG_KEY);
//                String scoreString = bundle.getString(SCORE_KEY);
//                String speedString = bundle.getString(SPEED_KEY);
//                long time = System.currentTimeMillis();
//                if ((time - prevTime) >= GESTURE_GAP_TIME) {
//                    Log.d(TAG, "" + Integer.parseInt(string) + ", " + Float.parseFloat(scoreString) + ", " + Float.parseFloat(speedString));
//                    Log.d(TAG, string + (time - prevTime));
//                    prevTime = time;
//                }
//            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This starts the recognizingBackground thread
        // sensor
        sensor = new recognizingBackground(getApplicationContext(), resultHandler, true);
        sensor.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensor.stopThread();
        try {
            sensor.join();
            Log.d(TAG, "Sensor join");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}