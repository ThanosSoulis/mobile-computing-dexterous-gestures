package com.example.dexterous_gestures;

import static com.example.dexterous_gestures.recognizingBackground.MSG_KEY;
import static com.example.dexterous_gestures.recognizingBackground.SCORE_KEY;
import static com.example.dexterous_gestures.recognizingBackground.SPEED_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    public static final long GESTURE_GAP_TIME = 500;

    private recognizingBackground sensor;
    private static long prevTime = 0;

    public static DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("actions");

    public static UserStudyModel userStudyModel = new UserStudyModel();

    @SuppressLint("HandlerLeak")
    private static Handler resultHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();

            if(bundle.containsKey(MSG_KEY)) {
                String string = bundle.getString(MSG_KEY);
                String scoreString = bundle.getString(SCORE_KEY);
                String speedString = bundle.getString(SPEED_KEY);
                long time = System.currentTimeMillis();
                if ((time - prevTime) >= GESTURE_GAP_TIME) {
                    Log.d(TAG, "" + Integer.parseInt(string) + ", " + Float.parseFloat(scoreString) + ", " + Float.parseFloat(speedString));
                    Log.d(TAG, string + (time - prevTime));
                    prevTime = time;
                }
            }
        }
    };
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.accept_button)
            onDecline();
        else if (id == R.id.decline_button)
            onAccept();
        else if (id == R.id.settings)
            showDialog();
    }
    private void onDecline() {
        Log.d(TAG, "onDecline: Called !");
    }

    private void onAccept() {
        Log.d(TAG, "onAccept: Called !");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This starts the recognizingBackground thread
        sensor = new recognizingBackground(getApplicationContext(), resultHandler, true);
        sensor.start();

        // Set button actions
        ImageButton bt_decline = findViewById(R.id.decline_button);
        bt_decline.setOnClickListener(this);

        ImageButton bt_accept = findViewById(R.id.accept_button);
        bt_accept.setOnClickListener(this);

        FloatingActionButton bt_settings = findViewById(R.id.settings);
        bt_settings.setOnClickListener(this);
    }

    void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(
                1);
        newFragment.show(getSupportFragmentManager(), "dialog");
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