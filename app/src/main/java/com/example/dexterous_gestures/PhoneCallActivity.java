package com.example.dexterous_gestures;

import static com.example.dexterous_gestures.recognizingBackground.MSG_KEY;
import static com.example.dexterous_gestures.recognizingBackground.SCORE_KEY;
import static com.example.dexterous_gestures.recognizingBackground.SPEED_KEY;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class PhoneCallActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "PhoneCallActivity";
    public static final long GESTURE_GAP_TIME = 500;
    private recognizingBackground sensor;
    private static long prevTime = 0;
    private MediaPlayer mediaPlayer = null;
    private float startResponseTime = 0;
    @SuppressLint("HandlerLeak")
    private Handler resultHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();

            if(bundle.containsKey(MSG_KEY)) {
                String string = bundle.getString(MSG_KEY);
                String scoreString = bundle.getString(SCORE_KEY);
                String speedString = bundle.getString(SPEED_KEY);
                long time = System.currentTimeMillis();
                if ((time - prevTime) >= GESTURE_GAP_TIME) {
                    int gestureCode = Integer.parseInt(string);
                    float gestureScore = Float.parseFloat(scoreString);
                    float gestureSpeed = Float.parseFloat(speedString);

                    Log.d(TAG, "" + gestureCode + ", " + gestureScore + ", " + gestureSpeed);
                    Log.d(TAG, string + (time - prevTime));
                    prevTime = time;

                    //TODO Log the actual gesture here

                    if(Objects.equals(MainActivity.gestureToCodeMap.get(gestureCode), MainActivity.userStudyModel.gesture)) {
                        // TODO do we ever need to call onAccept ?
                        onDecline();

                    }
                }
            }
        }
    };
    @Override
    public void onClick(View view) {
        int id = view.getId();
        
        if (id == R.id.decline_button)
            onDecline();
        else if (id == R.id.accept_button)
            onAccept();
    }
    private void onDecline() {
        Log.d(TAG, "onDecline: Called !");
        mediaPlayer.stop();

        float reactionTime = stopResponseTimeTracker();
        // TODO Log the successful gesture here

        startActivity(new Intent(PhoneCallActivity.this, IdleActivity.class));
        finish();
    }

    private void onAccept() {
        Log.d(TAG, "onAccept: Called !");
        mediaPlayer.stop();

        float reactionTime = stopResponseTimeTracker();
        // TODO Log the successful gesture here

        startActivity(new Intent(PhoneCallActivity.this, IdleActivity.class));
        finish();
    }

    private void startResponseTimeTracker(){
        startResponseTime = System.currentTimeMillis();
    }

    // Return the response time in milliseconds
    private float stopResponseTimeTracker(){
        return System.currentTimeMillis() - startResponseTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_call);

        if( MainActivity.userStudyModel.gesture.compareTo("Touch") != 0)
        {
            //This starts the recognizingBackground thread
            sensor = new recognizingBackground(getApplicationContext(), resultHandler, true);
            sensor.start();
        }

        // Set button actions
        ImageButton bt_decline = findViewById(R.id.decline_button);
        bt_decline.setOnClickListener(this);

        ImageButton bt_accept = findViewById(R.id.accept_button);
        bt_accept.setOnClickListener(this);

        // Starting the ringtone
        mediaPlayer = MediaPlayer.create(this, R.raw.incoming_call);
        mediaPlayer.setLooping(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        startResponseTimeTracker();
        mediaPlayer.start();
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
        } finally {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }
}