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
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class MyAlertDialogFragment extends DialogFragment {

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

        EditText inputUser = view.findViewById(R.id.userId);
        EditText inputContext = view.findViewById(R.id.contextId);
        EditText inputGesture = view.findViewById(R.id.gesture);

        if(MainActivity.userStudyModel.userId != null){
            inputUser.setText(MainActivity.userStudyModel.userId);
            inputContext.setText(MainActivity.userStudyModel.contextId);
        }

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String userId = inputUser.getText().toString();
                        String contextId = inputContext.getText().toString();
                        String gesture = inputGesture.getText().toString();
                        if(
                             !userId.isEmpty() || !contextId.isEmpty() || !gesture.isEmpty()
                        ){
                        MainActivity.userStudyModel.setUserId(userId);
                        MainActivity.userStudyModel.setContextId(contextId);
                        MainActivity.userStudyModel.setGesture(gesture);
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