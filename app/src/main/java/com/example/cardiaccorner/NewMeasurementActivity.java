package com.example.cardiaccorner;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class NewMeasurementActivity extends AppCompatActivity {

    String dateTime = null; // this will be set when the user presses continue
    int systolic = 0;
    int diastolic = 0;
    TextView systolicLabel;
    TextView diastolicLabel;
    Boolean sodiumStatus = false;
    Boolean stressStatus = false;
    Boolean exerciseStatus = false;
    String notes = null;

    Chip sodiumChip;
    Chip stressChip;
    Chip exerciseChip;

    Button continueBtn;
    String Measurement;

    private UUID SerialUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private int tries = 0;
    private String str = "";
    BluetoothDevice monitor;
    BluetoothSocket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_measurement_screen);

        systolicLabel = (TextView) findViewById(R.id.systolic);
        diastolicLabel = (TextView) findViewById(R.id.diastolic);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String Measurement = extras.getString("measurement");
            //The key argument here must match that used in the other activity
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
                Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();
                for (BluetoothDevice device : pairedDevices){
                    System.out.println(device.getName());
                    if(device.getName().equals("Cardiac Corner Monitor")) {
                        monitor = BTAdapter.getRemoteDevice(device.getAddress());
                    }
                }

                if(!monitor.getName().equals("Cardiac Corner Monitor")){
                    Intent intent = new Intent(NewMeasurementActivity.this,MainActivity.class);
                    intent.putExtra("err", "Could not obtain connection");
                    startActivity(intent);
                }

                do {
                    try {
                        socket = monitor.createRfcommSocketToServiceRecord(SerialUUID);
                        socket.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Intent intent = new Intent(NewMeasurementActivity.this,MainActivity.class);
                        intent.putExtra("err", "Could not obtain connection");
                        startActivity(intent);
                    }
                } while (!socket.isConnected() && tries < 3);

                //Send a character to start the transmission
                try {
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(111);
                } catch (IOException e) {
                    e.printStackTrace();
                    Intent intent = new Intent(NewMeasurementActivity.this,MainActivity.class);
                    intent.putExtra("err", "Could not Obtain Connection");
                    startActivity(intent);
                }

                //Receive the bit stream from the bluetooth transmission
                try {

                    //Read the transmission
                    InputStream inputStream = socket.getInputStream();
                    inputStream.skip(inputStream.available());

                    for(int i = 0; i < 100; i++){
                        System.out.println(inputStream.available());
                        if(inputStream.available() > 0) {
                            break;
                        } else if (i == 99) {
                            /**
                            Intent intent = new Intent(NewMeasurementActivity.this,MainActivity.class);
                            intent.putExtra("err", "Connection Timed Out");
                            startActivity(intent);
                            **/
                            timer.cancel();
                        }
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
                            str = str + (char)b;
                        }
                    }

                    systolic = parseInt(str.substring(0,str.indexOf("/")));
                    diastolic = parseInt(str.substring(str.indexOf("/")+1, str.length()));

                    systolicLabel.setText(str.substring(0,str.indexOf("/")));
                    diastolicLabel.setText(str.substring(str.indexOf("/")+1, str.length()));

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 100);

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

    @Override
    protected void onStart() {
        super.onStart();
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