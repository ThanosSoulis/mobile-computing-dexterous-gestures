package com.example.dexterous_gestures;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    public static final String StartTime = "StartTime";
    public static final String TimeInterval = "TimeInterval";
    public static final String RegularStartTime = "RegularStartTime";
    public static final String Repetitions = "Repetitions";
    public static DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("actions");
    public static UserStudyModel userStudyModel = new UserStudyModel();

    public static HashMap<Integer, String> gestureToCodeMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateGestureToCodeMap();
        setupButtons();
    }

    private void populateGestureToCodeMap(){
        gestureToCodeMap.put(14,"FlipFull");
        gestureToCodeMap.put(13,"FlipHalf");
        gestureToCodeMap.put(12, "FlipFull");
        gestureToCodeMap.put(11,"FlipHalf");
        gestureToCodeMap.put(10, "RotateFull");
        gestureToCodeMap.put(9, "RotateHalf");
        gestureToCodeMap.put(8, "RotateFull");
        gestureToCodeMap.put(7, "RotateHalf");
        gestureToCodeMap.put(6, "SpinFull");
        gestureToCodeMap.put(5, "SpinHalf");
        gestureToCodeMap.put(4, "SpinFull");
        gestureToCodeMap.put(3, "SpinHalf");
        gestureToCodeMap.put(-1, "Touch");
    }
    private void setupButtons(){
        Button bt_phone_call = (Button) findViewById(R.id.call_button);
        bt_phone_call.setOnClickListener(this);

        FloatingActionButton bt_settings = findViewById(R.id.settings);
        bt_settings.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.settings)
            showDialog();
        else if (id == R.id.call_button) {

            Bundle bundle = new Bundle();
            bundle.putInt(StartTime,10);
            bundle.putInt(RegularStartTime, 5);
            bundle.putInt(TimeInterval, 2);
            bundle.putInt(Repetitions, 3);

            Intent intent = new Intent(MainActivity.this, IdleActivity.class);
            intent.putExtras(bundle);

            startActivityForResult(intent,0);
            finish();
        }
    }

    void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(1);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }
}