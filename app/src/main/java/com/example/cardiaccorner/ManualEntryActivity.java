package com.example.cardiaccorner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The ManualEntryActivity Class is the class which populates and gives functionality
 * to the manual entry page.
 *
 * @Authors: David Casciano and Laura Reid
 */
public class ManualEntryActivity extends AppCompatActivity {

    /**
     * Properly handle the back button being pressed
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ManualEntryActivity.this, MainActivity.class);
        startActivity(i);
    }

    //Activity Elements
    Button submit;
    EditText sysEntry, diaEntry;
    String dateTime;

    /**
     * Autogenerated the onCreate function which would handle and attach
     * the listeners to the elements
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_entry_screen);

        //Set objects equal to parameters
        submit = (Button) findViewById(R.id.mesSubmit);
        sysEntry = (EditText) findViewById(R.id.SystolicEntry);
        diaEntry = (EditText) findViewById(R.id.diastolicEntry);

        //Properly handle the submit button being pressed
        //The systolic and diastolic values are collected
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sysDataString = sysEntry.getText().toString();
                String diaDataString = diaEntry.getText().toString();
                int sysData = Integer.parseInt(sysDataString);
                int diaData = Integer.parseInt(diaDataString);

                if(sysEntry.getText() == null || diaEntry.getText() == null){

                }
                else {
                    Intent i = new Intent(ManualEntryActivity.this, NewMeasurementActivity.class);
                    i.putExtra("sys", Integer.parseInt(sysEntry.getText().toString()));
                    i.putExtra("dia", Integer.parseInt(diaEntry.getText().toString()));
                    startActivity(i);
                }
            }
        });
    }
}
