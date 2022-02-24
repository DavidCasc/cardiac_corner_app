package com.example.cardiaccorner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

public class BluetoothCommunicationInfo extends AppCompatActivity {
    TextView infoBox;
    Button startBtn, manualEntryBtn;
    boolean paired;

    private void checkBT(){
        BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();
        Boolean failed = false;


        for (BluetoothDevice device : pairedDevices) {
            System.out.println(device.getName());
            if (device.getName().equals("Cardiac Corner Monitor")) {
                paired = true;
            }
        }
        ;
        if(paired){
            startBtn.setEnabled(true);
        } else {
            startBtn.setEnabled(false);
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt_communication_screen);
        infoBox = (TextView) findViewById(R.id.infoBox);
        startBtn =(Button) findViewById(R.id.startBtn);
        manualEntryBtn= (Button) findViewById(R.id.manualEntryBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BluetoothCommunicationInfo.this, BluetoothScreenActivity.class);
                startActivity(i);
            }
        });
        manualEntryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BluetoothCommunicationInfo.this, ManualEntryActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkBT();
    }
}