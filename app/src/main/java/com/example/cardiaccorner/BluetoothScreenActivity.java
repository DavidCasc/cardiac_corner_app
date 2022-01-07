package com.example.cardiaccorner;

import static java.lang.Integer.parseInt;

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

    TextView statusText;
    BluetoothDevice monitor;
    BluetoothSocket socket;
    int tries;
    private UUID SerialUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    String str = "";
    int systolic, diastolic;
    Intent intent;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(BluetoothScreenActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_screen);

        statusText = (TextView) findViewById(R.id.status);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                statusText = (TextView) findViewById(R.id.status);
                BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
                Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();
                Boolean failed = false;

                for (BluetoothDevice device : pairedDevices) {
                    System.out.println(device.getName());
                    if (device.getName().equals("Cardiac Corner Monitor")) {
                        monitor = BTAdapter.getRemoteDevice(device.getAddress());
                    }
                }

                statusText.setText("FINDING");
                if(monitor == null){
                    intent = new Intent(BluetoothScreenActivity.this, ManualEntryActivity.class);
                    startActivity(intent);
                } else if (!monitor.getName().equals("Cardiac Corner Monitor")) {
                    intent = new Intent(BluetoothScreenActivity.this, ManualEntryActivity.class);
                    startActivity(intent);
                } else {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    statusText.setText("CONNECTING");
                    tries = 0;
                    do {
                        try {
                            socket = monitor.createRfcommSocketToServiceRecord(SerialUUID);
                            socket.connect();
                            failed = false;
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("AAAAAH");
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
                    } while (!socket.isConnected() && tries <= 5);

                    statusText.setText("CONNECTED");

                    //Send a character to start the transmission
                    if (!failed) {
                        try {
                            OutputStream outputStream = socket.getOutputStream();
                            outputStream.write(111);
                        } catch (IOException e) {
                            e.printStackTrace();
                            intent = new Intent(BluetoothScreenActivity.this, ManualEntryActivity.class);
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

                            systolic = parseInt(str.substring(0, str.indexOf("/")));
                            diastolic = parseInt(str.substring(str.indexOf("/") + 1, str.length()));


                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }

                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Intent i = new Intent(BluetoothScreenActivity.this, NewMeasurementActivity.class);
                        i.putExtra("sys", systolic);
                        i.putExtra("dia", diastolic);
                        startActivity(i);

                    } else {
                        intent = new Intent(BluetoothScreenActivity.this, ManualEntryActivity.class);
                        startActivity(intent);
                    }

                    statusText.setText("COMPLETE");
                }
            }
        }, 100);
    }

}
