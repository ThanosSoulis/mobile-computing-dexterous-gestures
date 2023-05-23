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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    public static DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("actions");
    public static UserStudyModel userStudyModel = new UserStudyModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            startActivity(new Intent(MainActivity.this, PhoneCallActivity.class));
            finish();
        }
    }

    void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(1);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }
}