package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

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


    ImageView searchIcon;

    TextView searchView;

    Chip sodiumChip;
    Chip stressChip;
    Chip exerciseChip;

    String username;
    static final String SHARED_PREFS = "cardiacCornerPrefs";
    Button detailsBtn, searchBtn, backBtn;
    RecyclerView recyclerView;

    RecyclerAdapter recyclerAdapter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(BpHistoryActivity.this, MainActivity.class);
        startActivity(i);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        username = loadData("username");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bp_history_screen);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.getSerializable("entries") != null){
            ArrayList<Entry> entries = (ArrayList<Entry>) bundle.getSerializable("entries");
            logs = entries;
        } else {
            if (!logsStored()) {
                fetchLogs();
            }
            logs = retrieveLogs();
        }

        System.out.println(logs);

        backBtn = (Button) findViewById(R.id.back_button);
        backBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(BpHistoryActivity.this,MainActivity.class);
                        startActivity(i);

                    }
                });

        searchView = (TextView) findViewById(R.id.SearchText);
        searchIcon = (ImageView) findViewById(R.id.SearchIcon);

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logs.clear();
                for(Entry e: retrieveLogs()){
                    if(String.valueOf(e.getDia_measurement()).contains(searchView.getText().toString())){
                        logs.add(e);
                    } else if (String.valueOf(e.getSys_measurement()).contains(searchView.getText().toString())){
                        logs.add(e);
                    } else if (e.getTime_created().contains(searchView.getText().toString())){
                        logs.add(e);
                    } else if (searchView.getText().toString().toLowerCase().equals("sodium") && e.isSodium()){
                        logs.add(e);
                    } else if (searchView.getText().toString().toLowerCase().equals("stress") && e.isStress()){
                        logs.add(e);
                    } else if (searchView.getText().toString().toLowerCase().equals("exercise") && e.isExercise()){
                        logs.add(e);
                    }
                }
                recyclerAdapter.notifyDataSetChanged();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.logScroll);
        recyclerAdapter = new RecyclerAdapter(this, logs);
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
