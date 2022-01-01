package com.example.cardiaccorner;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMeasurementActivity extends AppCompatActivity {

    String dateTime; // this will be set when the user presses continue
    int systolic = 0;
    int diastolic = 0;
    TextView systolicLabel;
    TextView diastolicLabel;
    Boolean sodiumStatus = false;
    Boolean stressStatus = false;
    Boolean exerciseStatus = false;
    String notes = null;
    Entry entry;

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
    private boolean timeout;

    String username;
    static final String SHARED_PREFS = "cardiacCornerPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_measurement_screen);

        systolicLabel = (TextView) findViewById(R.id.systolic);
        diastolicLabel = (TextView) findViewById(R.id.diastolic);

        //TODO: Delete when done testing
        systolicLabel.setText("120");
        diastolicLabel.setText("80");

        systolic = 120;
        diastolic = 80;

        //TODO: uncomment
        /**
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
                            Intent intent = new Intent(NewMeasurementActivity.this,MainActivity.class);
                            intent.putExtra("err", "Connection Timed Out");
                            startActivity(intent);
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
        **/


        printSystolicValue();
        printDiastolicValue();

        continueBtn = (Button) findViewById(R.id.continue_button);
        continueBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(NewMeasurementActivity.this,BreakdownActivity.class);

                        // set date and notes strings
                        setDateTime();
                        notesToString();

                        // create entry
                        Entry entry = new Entry(dateTime, systolic, diastolic, sodiumStatus, stressStatus, exerciseStatus, notes);

                        //load logs
                        ArrayList<Entry> logs = retrieveLogs();

                        //Add logs
                        logs.add(entry);

                        //get username
                        String user = loadData("username");

                        //Store logs
                        addLog(entry,user);
                        storeLogs(logs);
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

    private void addLog(Entry entry, String user) {
        LogPostRequest logPostRequest = new LogPostRequest(entry, user);
        Call<LogPostResponse> logPostCall = ApiClient.getUserService().addLogs(logPostRequest);
        logPostCall.enqueue(new Callback<LogPostResponse>() {
            @Override
            public void onResponse(Call<LogPostResponse> call, Response<LogPostResponse> response) {

            }

            @Override
            public void onFailure(Call<LogPostResponse> call, Throwable t) {

            }
        });
    }

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
    public Boolean logsStored(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.contains("logs");
    }
    public void storeLogs(ArrayList<Entry> log){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String arr = gson.toJson(log);
        editor.putString("logs", arr);
        editor.commit();
    }
    public ArrayList<Entry> retrieveLogs(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String arr = sharedPreferences.getString("logs", null);
        Type type = new TypeToken<ArrayList<Entry>>() {}.getType();

        ArrayList<Entry> log = gson.fromJson(arr, type);

        if (log == null) {
            log = new ArrayList<Entry>();
        }

        return log;
    }
    ArrayList<Entry> logs;
}