package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;



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
    TextView btBlurb;

    private UUID SerialUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private int tries = 0;
    private String str = "";
    BluetoothDevice monitor;
    BluetoothSocket socket;
    Boolean paired = false;


    void updateStatus() {
        btBlurb = (TextView) findViewById(R.id.btTextBlurb);
        BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();
        Boolean failed = false;

        for (BluetoothDevice device : pairedDevices) {
            System.out.println(device.getName());
            if (device.getName().equals("Cardiac Corner Monitor")) {
                paired = true;
            } else {
                paired = false;
            }
        }
        if(pairedDevices.isEmpty()){
            paired = false;
        }
        ;
        if(paired){
            btBlurb.setText("Bluetooth Device Connected");
            btBlurb.setTextColor(Color.parseColor("#329832"));
        } else {
            btBlurb.setText("Bluetooth Device Not Connected");
            btBlurb.setTextColor(Color.parseColor("#FF0000"));
        }
    }

    @Override
    public void onBackPressed() {
        
    }

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
                        if(paired) {
                            Intent i = new Intent(MainActivity.this, BluetoothCommunicationInfo.class);
                            //i.putExtra("measurement", str);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, BluetoothConnectInfo.class);
                            //i.putExtra("measurement", str);
                            startActivity(i);
                        }
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

    @Override
    protected void onResume() {
        super.onResume();

        updateStatus();
    }
}