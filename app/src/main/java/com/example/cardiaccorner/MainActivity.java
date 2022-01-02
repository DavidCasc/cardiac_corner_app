package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
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

    private UUID SerialUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private int tries = 0;
    private String str = "";
    BluetoothDevice monitor;
    BluetoothSocket socket;

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
                        /**
                        BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
                        Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();
                        for (BluetoothDevice device : pairedDevices){
                            System.out.println(device.getName());
                            if(device.getName().equals("Cardiac Corner Monitor")) {
                                monitor = BTAdapter.getRemoteDevice(device.getAddress());
                            }
                        }

                        do {
                            try {
                                socket = monitor.createRfcommSocketToServiceRecord(SerialUUID);
                                socket.connect();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } while (!socket.isConnected() && tries < 3);

                        //Send a character to start the transmission
                        try {
                            OutputStream outputStream = socket.getOutputStream();
                            outputStream.write(111);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Receive the bit stream from the bluetooth transmission
                        try {

                            //Read the transmission
                            InputStream inputStream = socket.getInputStream();
                            inputStream.skip(inputStream.available());

                            while(inputStream.available() == 0){
                                System.out.println(inputStream.available());
                                Thread.sleep(100);
                            }
                            for(int i = 0; i < 7; i++){
                                byte b = (byte) inputStream.read();
                                System.out.println(b);

                                //If the byte is the ending character break
                                if(b == 'n'){
                                    break;
                                } else {
                                    //Concat to the string
                                    str = str + b;
                                }
                            }

                            System.out.println(str);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                         socket.close();
                        } catch (IOException e) {
                         e.printStackTrace();
                        }
                        **/

                        Intent i = new Intent(MainActivity.this,NewMeasurementActivity.class);
                        //i.putExtra("measurement", str);
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