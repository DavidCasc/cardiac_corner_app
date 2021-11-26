package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private void saveData(String Key, String Val) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Key, Val);
        editor.commit();
    }
    public String loadData(String Key) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(Key, "");
    }

    Button settingsBtn;
    Button newMeasurementBtn;
    Button bpHistoryBtn;
    Button graphViewBtn;
    static final String SHARED_PREFS = "cardiacCornerPrefs";
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        username = loadData("username");
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