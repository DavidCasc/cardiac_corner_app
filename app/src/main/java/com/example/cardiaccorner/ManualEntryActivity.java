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

public class ManualEntryActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ManualEntryActivity.this, MainActivity.class);
        startActivity(i);
    }

    Button submit;
    EditText sysEntry, diaEntry;
    String dateTime;

    private void setDateTime()
    {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        dateTime = currentDate + " " +currentTime;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_entry_screen);

        submit = (Button) findViewById(R.id.mesSubmit);
        sysEntry = (EditText) findViewById(R.id.SystolicEntry);
        diaEntry = (EditText) findViewById(R.id.diastolicEntry);

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
