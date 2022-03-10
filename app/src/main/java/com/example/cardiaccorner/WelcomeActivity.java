package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {

    static final String SHARED_PREFS = "cardiacCornerPrefs";

    private void saveData(String Key, String Val) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Key, Val);
        editor.apply();
    }

    private void initData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("init", true);
        editor.apply();
    }

    private boolean loggedIn(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if(sharedPreferences.contains("loggedIn")){
            return true;
        } else {
            return false;
        }
    }
    public void storeLogs(ArrayList<Entry> log){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String arr = gson.toJson(log);
        editor.putString("logs", arr);
        editor.commit();
    }

    Button signInBtn;
    Button signUpBtn;
    Button tempBypassBtn; //TEMPORARY LOGIN BYPASS FOR DEVELOPMENT

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        initData();
        if(loggedIn()){
            Intent i = new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(i);
        }

        signInBtn = (Button) findViewById(R.id.sign_in);
        signInBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(WelcomeActivity.this,SignInActivity.class);
                        startActivity(i);
                    }
                });

        signUpBtn = (Button) findViewById(R.id.sign_up);
        signUpBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(WelcomeActivity.this,SignUpActivity.class);
                        startActivity(i);
                    }
                });

        tempBypassBtn = (Button) findViewById(R.id.tempBypass);
        tempBypassBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Entry> log = new ArrayList<>();
                        log.add(new Entry("23-06-2021 16:23:07", 30, 30, true, false, false, "Had a good workout", true));
                        log.add(new Entry("30-06-2021 11:23:07", 210, 120, false, false, true, "Had a stressful meeting", true));
                        log.add(new Entry("20-07-2021 19:23:07", 60, 60, false, true, true, "Meeting with the parents", true));
                        log.add(new Entry("21-10-2021 23:23:07", 180, 90, true, true, false, "Good workout", true));
                        log.add(new Entry("23-10-2021 10:23:07", 90, 75, false, false, true, "Bad exam", true));
                        log.add(new Entry("25-10-2021 9:23:07",150 , 85, false, true, true, "Family Dinner", true));
                        log.add(new Entry("01-01-2022 12:23:07", 120, 80, true, false, false, "New Year resolution at the gym", true));
                        storeLogs(log);
                        Intent i = new Intent(WelcomeActivity.this,MainActivity.class);
                        startActivity(i);
                    }
                });

    }
}