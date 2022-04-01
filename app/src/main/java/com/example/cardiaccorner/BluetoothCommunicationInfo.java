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

/**
 *  The BluetoothCommunicationInfo class is used for event handling
 *  of the bt_communication_screen.xml layout.
 * @Author: David Casciano
 */
public class BluetoothCommunicationInfo extends AppCompatActivity {
    TextView infoBox;
    Button startBtn, manualEntryBtn;
    boolean paired;

    /**
     * The checkBT() function will get the bluetooth adapter and check if
     * the bluetooth device is connected
     */
    private void checkBT(){
        //get the default bluetooth adapter of the device
        BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();
        Boolean failed = false;

        //Enumerate through the device list and check if there is a device
        //with the name Cardiac Corner Monitor
        for (BluetoothDevice device : pairedDevices) {
            System.out.println(device.getName());
            if (device.getName().equals("Cardiac Corner Monitor")) {
                paired = true; //If the device is in the list set flag true
            }
        }
        //If there is a flag enable the startBtn
        if(paired){
            startBtn.setEnabled(true);
        } else {
            startBtn.setEnabled(false);
        }
    }

    /**
     * The onCreate is a generic function, please refer to the lifecycle of
     * an activity to find the order
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt_communication_screen); //Find the layout XML
        infoBox = (TextView) findViewById(R.id.infoBox);

        //Find the needed buttons for onClick Listeners
        startBtn =(Button) findViewById(R.id.startBtn);
        manualEntryBtn= (Button) findViewById(R.id.manualEntryBtn);

        //Create event handling for a click event of the startBtn
        //this was later renamed to Collect
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This will start the collection activity for the bluetooth communication
                Intent i = new Intent(BluetoothCommunicationInfo.this, BluetoothScreenActivity.class);
                startActivity(i);
            }
        });

        //Create event handling or a click event of the manualEntryBtn
        manualEntryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This will start the manual entry activity
                Intent i = new Intent(BluetoothCommunicationInfo.this, ManualEntryActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * onResume is a generic function within the activity lifecycle
     * Please check the Android documentation about its position
     *
     * When the activity comes back into focus this function is called.
     * Therefore, call the checkBT() function to update the bluetooth status
     */
    @Override
    protected void onResume() {
        super.onResume();
        checkBT();
    }
}