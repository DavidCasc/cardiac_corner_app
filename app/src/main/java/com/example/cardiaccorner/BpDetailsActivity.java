package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BpDetailsActivity extends AppCompatActivity {

    // temp until this info is being passed in through Entry
    String dateTime;
    Boolean sodiumStatus;
    Boolean stressStatus;
    Boolean exerciseStatus;
    String notes;

    Chip sodiumChip;
    Chip stressChip;
    Chip exerciseChip;

    Entry entry;

    static final String SHARED_PREFS = "cardiacCornerPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bp_details_screen);



        TextView notesText = (TextView) findViewById(R.id.notes_text);
        TextView dateText = (TextView) findViewById(R.id.date_text);
        sodiumChip = (Chip) findViewById(R.id.chip1_card);
        stressChip = (Chip) findViewById(R.id.chip2_card);
        exerciseChip = (Chip) findViewById(R.id.chip3_card);
        TextView diastolic = (TextView) findViewById(R.id.systolic);
        TextView systolic = (TextView) findViewById(R.id.diastolic);

        Bundle bundle = getIntent().getExtras();

        if(bundle.getSerializable("entry")!= null)
        {
            entry = (Entry) bundle.getSerializable("entry");
        }

        System.out.println(entry);

        notesText.setText(entry.getNotes());
        dateText.setText(entry.getTime_created());
        sodiumChip.setChecked(entry.isSodium());
        stressChip.setChecked(entry.isStress());
        exerciseChip.setChecked(entry.isExercise());

        sodiumChip.setCheckable(false);
        stressChip.setCheckable(false);
        exerciseChip.setCheckable(false);

        diastolic.setText(String.valueOf(entry.getDia_measurement()));
        systolic.setText(String.valueOf(entry.getSys_measurement()));
    }

    private void printSystolicValue()
    {
        TextView textView = (TextView) findViewById(R.id.systolic);

        int systolic = 120;
        if(systolic == 0){
            textView.setText(null);
        } else{
            textView.setText(String.valueOf(systolic));
        }
    }

    private void printDiastolicValue()
    {
        TextView textView = (TextView) findViewById(R.id.diastolic);
        int diastolic = 80;
        if(diastolic == 0){
            textView.setText(null);
        } else{
            textView.setText(String.valueOf(diastolic));
        }
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