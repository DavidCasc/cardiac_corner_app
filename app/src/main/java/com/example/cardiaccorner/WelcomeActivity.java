package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * The WelcomeActivity Class is the class which populates and gives functionality
 * to the welcome page.
 *
 * @Authors: David Casciano and Laura Reid
 */
public class WelcomeActivity extends AppCompatActivity {

    static final String SHARED_PREFS = "cardiacCornerPrefs";

    /**
     * TODO
     * @param Key
     * @param Val
     */
    private void saveData(String Key, String Val) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Key, Val);
        editor.apply();
    }

    /**
     * TODO
     */
    private void initData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("init", true);
        editor.apply();
    }

    /**
     * TODO
     * @return
     */
    private boolean loggedIn(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if(sharedPreferences.contains("loggedIn")){
            return true;
        } else {
            return false;
        }
    }

    /**
     * TODO
     * @param log
     */
    public void storeLogs(ArrayList<Entry> log){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String arr = gson.toJson(log);
        editor.putString("logs", arr);
        editor.commit();
    }

    //Activity Elements
    Button signInBtn;
    Button signUpBtn;

    /**
     * Autogenerated the onCreate function which would handle and attach
     * the listeners to the elements
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        initData();
        if(loggedIn()){
            Intent i = new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(i);
        }

        //Properly handle the sign in button being pressed
        signInBtn = (Button) findViewById(R.id.sign_in);
        signInBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(WelcomeActivity.this,SignInActivity.class);
                        startActivity(i);
                    }
                });

        //Properly handle the sign up button being pressed
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