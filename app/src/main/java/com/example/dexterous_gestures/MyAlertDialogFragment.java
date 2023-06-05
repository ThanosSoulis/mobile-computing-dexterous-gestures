package com.example.dexterous_gestures;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyAlertDialogFragment extends DialogFragment {
    private String selectedContext;
    private String selectedGesture;

    public static MyAlertDialogFragment newInstance(int title) {
        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog, null, false);
        Set<String> uniqueOptions = new HashSet<>();

        for (String value : MainActivity.gestureToCodeMap.values()) {
            if (!value.contains("Full")) {
                uniqueOptions.add(value);
            }
        }




        EditText inputUser = view.findViewById(R.id.userId);
        RadioGroup radioGroupGesture = view.findViewById(R.id.radioGroupGesture);
        RadioGroup radioGroupContext = view.findViewById(R.id.radioGroupContext);

        for (String option : uniqueOptions) {
            RadioButton radioButton = new RadioButton(view.getContext());
            radioButton.setText(option);
            radioGroupGesture.addView(radioButton);
        }

        List<String> stringList = Arrays.asList("Sit", "Distracted (Reading out loud)", "Distracted (Typing)");

        for (String option : stringList) {
            RadioButton radioButton = new RadioButton(view.getContext());
            radioButton.setText(option);
            radioGroupContext.addView(radioButton);
        }

        radioGroupGesture.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Handle radio button selection changes here
                RadioButton radioButton = view.findViewById(checkedId);
                selectedGesture = radioButton.getText().toString();
            }
        });

        radioGroupContext.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Handle radio button selection changes here
                RadioButton radioButton = view.findViewById(checkedId);
                selectedContext = radioButton.getText().toString();

            }
        });

        if(MainActivity.userStudyModel.userId != null){
            inputUser.setText(MainActivity.userStudyModel.userId);
        }

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String userId = inputUser.getText().toString();
                        if(
                             !userId.isEmpty() && selectedContext != null && selectedGesture != null
                        ){
                        MainActivity.userStudyModel.setUserId(userId);
                            MainActivity.userStudyModel.setContextId(selectedContext);
                            MainActivity.userStudyModel.setExpectedGesture(selectedGesture);

                        } else Toast.makeText(getActivity(),"Input can't be empty!", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MyAlertDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}