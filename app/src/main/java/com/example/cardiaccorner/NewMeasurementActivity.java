package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewMeasurementActivity extends AppCompatActivity {

    String dateTime = null; // this will be set when the user presses continue
    int systolic = 0;
    int diastolic = 0;
    Boolean sodiumStatus = false;
    Boolean stressStatus = false;
    Boolean exerciseStatus = false;
    String notes = null;

    Chip sodiumChip;
    Chip stressChip;
    Chip exerciseChip;

    Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_measurement_screen);

        printSystolicValue();
        printDiastolicValue();

        continueBtn = (Button) findViewById(R.id.continue_button);
        continueBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        continueButtonClicked();
                        Intent i = new Intent(NewMeasurementActivity.this,BreakdownActivity.class);
                        startActivity(i);
                    }
                });

        sodiumChip = (Chip) findViewById(R.id.chip1);
        sodiumChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(sodiumChip.isChecked()){
                            sodiumStatus = true;
                        } else{
                            sodiumStatus = false;
                        }
                    }
                });

        stressChip = (Chip) findViewById(R.id.chip2);
        stressChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(stressChip.isChecked()){
                            stressStatus = true;
                        } else{
                            stressStatus = false;
                        }
                    }
                });

        exerciseChip = (Chip) findViewById(R.id.chip3);
        exerciseChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(exerciseChip.isChecked()){
                            exerciseStatus = true;
                        } else{
                            exerciseStatus = false;
                        }
                    }
                });
    }

    private void continueButtonClicked()
    {
        // set date and notes strings
        setDateTime();
        notesToString();

        // create entry
        Entry newEntry = new Entry(dateTime, systolic, diastolic, sodiumStatus, stressStatus, exerciseStatus, notes);

        //save to db
        System.out.println(newEntry);

        //go to next page / prompt user / anything else
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

    private void notesToString()
    {
        EditText notesText = (EditText) findViewById(R.id.notes_textbox);
        notes = notesText.getText().toString();
    }

    private void setDateTime()
    {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        dateTime = currentDate + " " +currentTime;
    }
}