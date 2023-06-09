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
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.util.Objects;

public class PhoneCallActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "PhoneCallActivity";
    public static final long GESTURE_GAP_TIME = 500;
    private recognizingBackground sensor;
    private static long prevTime = 0;
    private MediaPlayer mediaPlayer = null;
    private long startResponseTime = 0;
    private long stopResponseTime= 0;
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

                    MainActivity.userStudyModel.setActualGesture(MainActivity.gestureToCodeMap.get(gestureCode));

                    if(Objects.equals(MainActivity.gestureToCodeMap.get(gestureCode), MainActivity.userStudyModel.expectedGesture)) {
                        // TODO do we ever need to call onAccept ?
                        onDecline();

                    } else {
                        uploadToFirebase(null);
                    }
                }
            }
        }
    };
    @Override
    public void onClick(View view) {
        int id = view.getId();
        
        if (id == R.id.decline_button){
            MainActivity.userStudyModel.setActualGesture(MainActivity.gestureToCodeMap.get(-1));
            onDecline();
        }
        else if (id == R.id.accept_button){
            onAccept();
            //TODO Log button press here
        }

    }
    private void onDecline() {
        Log.d(TAG, "onDecline: Called !");
        mediaPlayer.stop();

        long reactionTime = stopResponseTimeTracker();
        // TODO Log the successful gesture here
        uploadToFirebase(reactionTime);
        startIdleActivity();

        finish();
    }

    private void uploadToFirebase(Long reactionTime){
        MainActivity.userStudyModel.setResponseTime(String.valueOf(reactionTime));
        MainActivity.userStudyModel.setTimeStampAlarmStart(String.valueOf(startResponseTime));
        MainActivity.userStudyModel.setTimeStampAlarmSop(String.valueOf(stopResponseTime));
        MainActivity.myRef.child(MainActivity.userStudyModel.userId).child(String.valueOf(System.currentTimeMillis())).setValue(MainActivity.userStudyModel);
    }

    private void onAccept() {
        Log.d(TAG, "onAccept: Called !");
        mediaPlayer.stop();

        long reactionTime = stopResponseTimeTracker();
        // TODO Log the successful gesture here

        startIdleActivity();

        finish();
    }

    private void startIdleActivity(){
        Bundle bundle = getIntent().getExtras();

        int repetitions = bundle.getInt(MainActivity.Repetitions);

        if(repetitions > 0) {
            Intent intent = new Intent(PhoneCallActivity.this, IdleActivity.class);
            intent.putExtras(bundle);

            startActivityForResult(intent, 1);
        }
        // When the repetitions are over, start the Main Activity again
        else
            startActivity(new Intent(PhoneCallActivity.this, MainActivity.class));
    }
    private void startResponseTimeTracker(){
        startResponseTime = System.currentTimeMillis();
    }

    // Return the response time in milliseconds
    private long stopResponseTimeTracker(){
        stopResponseTime = System.currentTimeMillis();
        return  stopResponseTime - startResponseTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_call);

        handleSystemBars(true);

        if( MainActivity.userStudyModel.expectedGesture.compareTo("Touch") != 0)
        {
            //This starts the recognizingBackground thread
            sensor = new recognizingBackground(getApplicationContext(), resultHandler, true);
            sensor.start();
        }

        setupButtons();

        // Starting the ringtone
        mediaPlayer = MediaPlayer.create(this, R.raw.incoming_call);
        mediaPlayer.setLooping(true);
    }

    private void setupButtons(){
        // Set button actions
        ImageButton bt_decline = findViewById(R.id.decline_button);
        bt_decline.setOnClickListener(this);

        ImageButton bt_accept = findViewById(R.id.accept_button);
        bt_accept.setOnClickListener(this);
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

        try {
            if(sensor != null)
            {
                sensor.stopThread();
                sensor.join();
                Log.d(TAG, "Sensor join");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            handleSystemBars(false);

            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }
}