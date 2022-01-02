package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BpHistoryActivity extends AppCompatActivity {

    // temp until this info is being passed in through Entry
    String dateTime = "27-11-2021 23:41:30";
    int systolic = 120;
    int diastolic = 80;
    Boolean sodiumStatus = true;
    Boolean stressStatus = false;
    Boolean exerciseStatus = true;

    Chip sodiumChip;
    Chip stressChip;
    Chip exerciseChip;

    String username;
    static final String SHARED_PREFS = "cardiacCornerPrefs";
    Button detailsBtn;
    RecyclerView recyclerView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        username = loadData("username");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bp_history_screen);
        /**
        printSystolicValue();
        printDiastolicValue();

        TextView dateText = (TextView) findViewById(R.id.date_text);
        dateText.setText(dateTime);

        detailsBtn = (Button) findViewById(R.id.details);
        detailsBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(BpHistoryActivity.this,BpDetailsActivity.class);
                        startActivity(i);
                    }
                });

        sodiumChip = (Chip) findViewById(R.id.chip1_card);
        if(sodiumStatus == true){
            sodiumChip.setChecked(true);
            sodiumChip.setCheckedIconVisible(true);
            sodiumChip.setVisibility(View.VISIBLE);
        }
        sodiumChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(sodiumChip.isChecked()){
                            sodiumChip.setChecked(false);
                        } else{
                            sodiumChip.setChecked(true);
                            sodiumChip.setCheckedIconVisible(true);
                        }
                    }
                });

        stressChip = (Chip) findViewById(R.id.chip2_card);
        if(stressStatus == true){
            stressChip.setChecked(true);
            stressChip.setCheckedIconVisible(true);
            stressChip.setVisibility(View.VISIBLE);
        }
        stressChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(stressChip.isChecked()){
                            stressChip.setChecked(false);
                        } else{
                            stressChip.setChecked(true);
                            stressChip.setCheckedIconVisible(true);
                        }
                    }
                });

        exerciseChip = (Chip) findViewById(R.id.chip3_card);
        if(exerciseStatus == true){
            exerciseChip.setChecked(true);
            exerciseChip.setCheckedIconVisible(true);
            exerciseChip.setVisibility(View.VISIBLE);
        }
        exerciseChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(exerciseChip.isChecked()){
                            exerciseChip.setChecked(false);
                        } else{
                            exerciseChip.setChecked(true);
                            exerciseChip.setCheckedIconVisible(true);
                        }
                    }
                });
        **/

        if(!logsStored()) {
            fetchLogs();
        }
        logs = retrieveLogs();
        System.out.println(logs);

        recyclerView = (RecyclerView) findViewById(R.id.logScroll);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(this, logs);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    public void fetchLogs() {
        Call<LogsResponse> logsRequestCall = ApiClient.getUserService().fetchLogs(username);
        logsRequestCall.enqueue(new Callback<LogsResponse>() {
            @Override
            public void onResponse(Call<LogsResponse> call, Response<LogsResponse> response) {
                storeLogs(response.body().getLogs());
            }

            @Override
            public void onFailure(Call<LogsResponse> call, Throwable t) {
                Toast.makeText(BpHistoryActivity.this, "Failure", Toast.LENGTH_LONG).show();
            }
        });

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
}
