package com.example.cardiaccorner;

import static java.lang.Integer.parseInt;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class BluetoothScreenActivity extends AppCompatActivity {

    //Page Elements
    TextView statusText;

    //Bluetooth Elements
    BluetoothDevice monitor;
    BluetoothSocket socket;
    int tries;
    private UUID SerialUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    //Data storage variabels
    String str = "";
    int systolic, diastolic;

    //Activity control
    Intent intent;
    Timer spawner = new Timer();
    Activity activity;

    /**
     * Create a new timer task.
     *
     * We use a timer task because if you were not to do it asyncronously
     * the activity lifecycle would hand and you would not be able to use
     * the app.
     */
    TimerTask btTask = new TimerTask() {
        @Override
        public void run() {
            statusText = (TextView) findViewById(R.id.status); //Get the element to alter the status text
            BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter(); //Get the default bt adapter
            Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices(); //Get the set of bluetooth devices
            Boolean failed = false;

            //Enumerate through the devices to look for the Cardiac Corner Monitor
            for (BluetoothDevice device : pairedDevices) {
                System.out.println(device.getName());
                if (device.getName().equals("Cardiac Corner Monitor")) {
                    monitor = BTAdapter.getRemoteDevice(device.getAddress()); //Get device info if it is there
                }
            }

            statusText.setText("FINDING"); //update status text

            //If there was no monitor found go to manual entry page
            if (monitor == null) {
                intent = new Intent(BluetoothScreenActivity.this, ManualEntryActivity.class);
                finish();
                startActivity(intent);
            } //If the name is not the device go to the manual entry page
            else if (!monitor.getName().equals("Cardiac Corner Monitor")) {
                intent = new Intent(BluetoothScreenActivity.this, ManualEntryActivity.class);
                finish();
                startActivity(intent);
            } else {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                statusText.setText("CONNECTING"); //update status text
                tries = 0;

                //Try to make a bluetooth connection through a socket
                do {
                    try {
                        socket = monitor.createRfcommSocketToServiceRecord(SerialUUID);
                        socket.connect();
                        failed = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (!socket.isConnected()) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (tries == 5) {
                        failed = true;
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    tries++;
                } while (!socket.isConnected() && tries <= 5); //We will only try five times

                statusText.setText("CONNECTED"); //Update the device to connected

                //Send a character to start the transmission
                if (!failed) {
                    try {
                        OutputStream outputStream = socket.getOutputStream();
                        outputStream.write(111);
                    } catch (IOException e) {
                        e.printStackTrace();
                        intent = new Intent(BluetoothScreenActivity.this, ManualEntryActivity.class);
                        finish();
                        startActivity(intent);
                    }

                    //Receive the bit stream from the bluetooth transmission
                    try {

                        //Read the transmission
                        InputStream inputStream = socket.getInputStream();
                        inputStream.skip(inputStream.available());

                        for (int i = 0; i < 100; i++) {
                            System.out.println(inputStream.available());
                            if (inputStream.available() > 0) {
                                break;
                            } else if (i == 99) {
                                intent = new Intent(BluetoothScreenActivity.this, ManualEntryActivity.class);
                                finish();
                                startActivity(intent);
                            }
                            Thread.sleep(100);
                        }

                        for (int i = 0; i < 7; i++) {
                            byte b = (byte) inputStream.read();
                            System.out.println(b);

                            //If the byte is the ending character break
                            if (b == 'n') {
                                break;
                            } else {
                                //Concat to the string
                                str = str + (char) b;
                            }
                        }
                        //Break the transmission into the two substrings
                        systolic = parseInt(str.substring(0, str.indexOf("/")));
                        diastolic = parseInt(str.substring(str.indexOf("/") + 1, str.length()));


                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    //Close the blutooth socket
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Automatically go to the entry screen
                    Intent i = new Intent(BluetoothScreenActivity.this, NewMeasurementActivity.class);
                    i.putExtra("sys", systolic); //Hand off the information for both sys and dia
                    i.putExtra("dia", diastolic);
                    finish();
                    startActivity(i);

                } else {
                    intent = new Intent(BluetoothScreenActivity.this, ManualEntryActivity.class);
                    finish();
                    startActivity(intent);
                }
                //Set status to complete
                statusText.setText("COMPLETE");
            }
        }
    };

    /**
     * Have an event listener for the back press to handle it properly
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(BluetoothScreenActivity.this, MainActivity.class);
        finish();
        startActivity(i);
    }

    /**
     * Autogenerated onCreate just to get the status text element
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_screen);
        activity = this;
        statusText = (TextView) findViewById(R.id.status);
    }

    /**
     * Start the task once the activity is created and in focus
     */
    @Override
    protected void onResume() {
        super.onResume();
        spawner.schedule(btTask,100);
    }

    /**
     * Properly stop the async task when we quit the activity
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        btTask.cancel();
        spawner.cancel();
    }
}
