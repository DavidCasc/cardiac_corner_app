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

    }
}