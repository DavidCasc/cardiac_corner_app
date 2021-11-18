package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

public class NewMeasurementActivity extends AppCompatActivity {

    String systolic_var = "120"; //temp until we have measurement from monitor passed in
    String diastolic_var = "80"; //temp until we have measurement from monitor passed in

    Chip chip1;
    Chip chip2;
    Chip chip3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_measurement_screen);

        TextView textView = (TextView) findViewById(R.id.systolic);
        textView.setText(systolic_var);

        TextView textView2 = (TextView) findViewById(R.id.diastolic);
        textView2.setText(diastolic_var);

    }
}