package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

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
    String username;
    static final String SHARED_PREFS = "cardiacCornerPrefs";

    private void saveData(String Key, String Val) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Key, Val);
        editor.apply();
    }
    public String loadData(String Key) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(Key, "");
    }
    public Boolean logsStored(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.contains("logs");
    }
    public void storeLogs(ArrayList<Entry> logs){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String arr = gson.toJson(logs);
        editor.putString("logs", arr);
        editor.apply();
    }

    public ArrayList<Entry> retrieveLogs(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String arr = sharedPreferences.getString("logs", null);
        Type type = new TypeToken<ArrayList<Entry>>() {}.getType();

        ArrayList<Entry> logs = gson.fromJson(arr, type);

        if (logs == null) {
            logs = new ArrayList<Entry>();
        }
        return logs;
    }
    ArrayList<Entry> logs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        username = loadData("username");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bp_history_screen);
        if(!logsStored()){
            fetchLogs();
        } else {
            logs = retrieveLogs();
        }
        System.out.println(logs.toString());
    }

    public void fetchLogs() {
        Call<LogsResponse> logsRequestCall = ApiClient.getUserService().fetchLogs(username);
        logsRequestCall.enqueue(new Callback<LogsResponse>() {
            @Override
            public void onResponse(Call<LogsResponse> call, Response<LogsResponse> response) {
                storeLogs(response.body().getLogs());
                logs = response.body().getLogs();
            }

            @Override
            public void onFailure(Call<LogsResponse> call, Throwable t) {
                Toast.makeText(BpHistoryActivity.this, "Failure", Toast.LENGTH_LONG).show();
            }
        });

    }
}
