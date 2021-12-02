package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

public class BpDetailsActivity extends AppCompatActivity {

    // temp until this info is being passed in through Entry
    String dateTime = "27-11-2021 23:41:30";
    int systolic = 120;
    int diastolic = 80;
    Boolean sodiumStatus = true;
    Boolean stressStatus = false;
    Boolean exerciseStatus = true;
    String notes = "here is where the user can type lots of words!";

    Chip sodiumChip;
    Chip stressChip;
    Chip exerciseChip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bp_details_screen);

        printSystolicValue();
        printDiastolicValue();

        TextView notesText = (TextView) findViewById(R.id.notes_text);
        notesText.setText(notes);

        TextView dateText = (TextView) findViewById(R.id.date_text);
        dateText.setText(dateTime);

        sodiumChip = (Chip) findViewById(R.id.chip1);
        if(sodiumStatus == true){
            sodiumChip.setChecked(true);
            sodiumChip.setCheckedIconVisible(true);
        } else{
            sodiumChip.setChecked(false);
        }
        sodiumChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(sodiumChip.isChecked()){
                            sodiumChip.setChecked(false);
                        } else{
                            sodiumChip.setChecked(true);
                            sodiumChip.setCheckedIconVisible(true);
                        }
                    }
                });

        stressChip = (Chip) findViewById(R.id.chip2);
        if(stressStatus == true){
            stressChip.setChecked(true);
            stressChip.setCheckedIconVisible(true);
        } else{
            stressChip.setChecked(false);
        }
        stressChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(stressChip.isChecked()){
                            stressChip.setChecked(false);
                        } else{
                            stressChip.setChecked(true);
                            stressChip.setCheckedIconVisible(true);
                        }
                    }
                });

        exerciseChip = (Chip) findViewById(R.id.chip3);
        if(exerciseStatus == true){
            exerciseChip.setChecked(true);
            exerciseChip.setCheckedIconVisible(true);
        } else{
            exerciseChip.setChecked(false);
        }
        exerciseChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(exerciseChip.isChecked()){
                            exerciseChip.setChecked(false);
                        } else{
                            exerciseChip.setChecked(true);
                            exerciseChip.setCheckedIconVisible(true);
                        }
                    }
                });

    }

    private void printSystolicValue()
    {
        TextView textView = (TextView) findViewById(R.id.systolic);

        if(systolic == 0){
            textView.setText(null);
        } else{
            textView.setText(String.valueOf(systolic));
        }
    }

    private void printDiastolicValue()
    {
        TextView textView = (TextView) findViewById(R.id.diastolic);

        if(diastolic == 0){
            textView.setText(null);
        } else{
            textView.setText(String.valueOf(diastolic));
        }
    }
}