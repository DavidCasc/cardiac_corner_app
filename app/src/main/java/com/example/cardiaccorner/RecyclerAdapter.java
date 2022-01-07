package com.example.cardiaccorner;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.bpEntry> {
    static final String SHARED_PREFS = "cardiacCornerPrefs";
    private boolean ok = false;

    private void saveData(String Key, String Val) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Key, Val);
        editor.commit();
    }
    public String loadData(String Key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(Key, "");
    }
    public Boolean logsStored(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.contains("logs");
    }
    public void storeLogs(ArrayList<Entry> log){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String arr = gson.toJson(log);
        editor.putString("logs", arr);
        editor.commit();
    }
    public ArrayList<Entry> retrieveLogs(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String arr = sharedPreferences.getString("logs", null);
        Type type = new TypeToken<ArrayList<Entry>>() {}.getType();

        ArrayList<Entry> log = gson.fromJson(arr, type);

        if (log == null) {
            log = new ArrayList<Entry>();
        }

        return log;
    }

    private Context context;
    private ArrayList<Entry> entries;

    public RecyclerAdapter(Context ct, ArrayList<Entry> entriesList){
        context  = ct;
        entries = entriesList;
    }

    @NonNull
    @Override
    public bpEntry onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.bp_entry, parent, false);
        return new bpEntry(view);
    }

    @Override
    public void onBindViewHolder(@NonNull bpEntry holder, int position) {
        Entry entry = entries.get(position);

        holder.systolic.setText(String.valueOf(entry.getSys_measurement()));
        holder.diastolic.setText(String.valueOf(entry.getDia_measurement()));
        holder.date_text.setText(entry.getTime_created());


        if(entry.getSynced()){
            holder.sync.setVisibility(View.GONE);
        }

        for (int i=0; i<holder.chips.getChildCount();i++){
            Chip chip = (Chip)holder.chips.getChildAt(i);
            if(chip.getText().equals("Heavy Exercise") && entry.isExercise()){
                chip.setChecked(true);
                chip.setVisibility(View.VISIBLE);
            }
            if(chip.getText().equals("Stress") && entry.isStress()){
                chip.setChecked(true);
                chip.setVisibility(View.VISIBLE);
            }
            if(chip.getText().equals("Sodium") && entry.isSodium()){
                chip.setChecked(true);
                chip.setVisibility(View.VISIBLE);
            }
            if(chip.getVisibility() == View.VISIBLE){
                chip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chip.setChecked(true);
                    }
                });
            }
        }

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, BpDetailsActivity.class);
                i.putExtra("entry", entry);
                context.startActivity(i);

            }
        });

        holder.sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = loadData("username");
                entry.setSynced(true);
                addLog(entry, username);
            }
        });



    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class bpEntry extends RecyclerView.ViewHolder{

        TextView systolic, diastolic, date_text;
        Chip exercise, sodium, stress;
        Button details, sync;

        ChipGroup chips;

        public bpEntry(@NonNull View itemView) {
            super(itemView);

            systolic = (TextView) itemView.findViewById(R.id.systolic);
            diastolic = (TextView) itemView.findViewById(R.id.diastolic);
            date_text = (TextView) itemView.findViewById(R.id.date_text);

            chips = (ChipGroup) itemView.findViewById(R.id.chips);


            details = (Button) itemView.findViewById(R.id.details);
            sync = (Button) itemView.findViewById(R.id.sync);

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
                System.out.println(logs);
                //Add logs
                entry.setSynced(false);
                int index = logs.indexOf(entry);
                entry.setSynced(true);
                logs.set(index, entry);
                storeLogs(logs);
                Intent i = new Intent(context, BpDetailsActivity.class);
                i.putExtra("entry", entry);
                context.startActivity(i);

            }

            @Override
            public void onFailure(Call<LogPostResponse> call, Throwable t) {
                System.out.println("Post Failed");
                entry.setSynced(false);
            }
        });
    }
}
