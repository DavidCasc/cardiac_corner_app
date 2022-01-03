package com.example.cardiaccorner;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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

    CardView cardView;
    TextView statusText, textView, textView1;

    EditText diaEntry, sysEntry;
    Button continueBtn, mesSubmit;
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(NewMeasurementActivity.this, MainActivity.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_measurement_screen);

        systolicLabel = (TextView) findViewById(R.id.systolic);
        diastolicLabel = (TextView) findViewById(R.id.diastolic);
        cardView = (CardView) findViewById(R.id.btProgressPanel);
        statusText = (TextView) findViewById(R.id.status);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            if(bundle.getInt("sys")!= 0 && bundle.getInt("dia")!= 0 )
            {
                systolic = bundle.getInt("sys");
                diastolic = bundle.getInt("dia");
            }
        }

        printSystolicValue();
        printDiastolicValue();

        continueBtn = (Button) findViewById(R.id.continue_button);
        continueBtn.setVisibility(View.GONE);
        continueBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(NewMeasurementActivity.this,BreakdownActivity.class);

                        // set date and notes strings
                        setDateTime();
                        notesToString();

                        // create entry
                        Entry entry = new Entry(dateTime, systolic, diastolic, sodiumStatus, stressStatus, exerciseStatus, notes, true);

                        //get username
                        String user = loadData("username");

                        //Store logs
                        addLog(entry,user);
                        startActivity(i);
                    }
                });

        sodiumChip = (Chip) findViewById(R.id.chip1_card);
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

        stressChip = (Chip) findViewById(R.id.chip2_card);
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

        exerciseChip = (Chip) findViewById(R.id.chip3_card);
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
        Entry newEntry = new Entry(dateTime, systolic, diastolic, sodiumStatus, stressStatus, exerciseStatus, notes, true);

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
                //load logs
                ArrayList<Entry> logs = retrieveLogs();

                //Add logs
                logs.add(entry);
                storeLogs(logs);
            }

            @Override
            public void onFailure(Call<LogPostResponse> call, Throwable t) {
                System.out.println("Post Failed");
                entry.setSynced(false);
                //load logs
                ArrayList<Entry> logs = retrieveLogs();

                //Add logs
                logs.add(entry);
                storeLogs(logs);
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
}