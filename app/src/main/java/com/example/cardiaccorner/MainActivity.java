package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button settingsBtn;
    Button newMeasurementBtn;
    Button bpHistoryBtn;
    Button graphViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        settingsBtn = (Button) findViewById(R.id.settings);
        settingsBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this,SettingsActivity.class);
                        startActivity(i);
                    }
                });

        newMeasurementBtn = (Button) findViewById(R.id.new_measurement);
        newMeasurementBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this,NewMeasurementActivity.class);
                        startActivity(i);
                    }
                });

        bpHistoryBtn = (Button) findViewById(R.id.bp_history);
        bpHistoryBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this,BpHistoryActivity.class);
                        startActivity(i);
                    }
                });

        graphViewBtn = (Button) findViewById(R.id.graph_view);
        graphViewBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this,GraphViewActivity.class);
                        startActivity(i);
                    }
                });

    }
}