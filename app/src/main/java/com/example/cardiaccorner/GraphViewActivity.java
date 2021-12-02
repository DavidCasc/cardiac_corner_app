package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

public class GraphViewActivity extends AppCompatActivity {

    Chip sodiumChip;
    Chip stressChip;
    Chip exerciseChip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_view_screen);

        sodiumChip = (Chip) findViewById(R.id.chip1);
        sodiumChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(sodiumChip.isChecked()){
                            // code to display data with sodium tag
                        } else{
                            // other case
                        }
                    }
                });

        stressChip = (Chip) findViewById(R.id.chip2);
        stressChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(stressChip.isChecked()){
                            // code to display data with stress tag
                        } else{
                            // other case
                        }
                    }
                });

        exerciseChip = (Chip) findViewById(R.id.chip3);
        exerciseChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(exerciseChip.isChecked()){
                            // code to display data with heavy exercise tag
                        } else{
                            // other case
                        }
                    }
                });

    }
}