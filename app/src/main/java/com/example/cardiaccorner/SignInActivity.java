package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

public class  SignInActivity extends AppCompatActivity {

    EditText username, password;
    Button btnLogin;
    Button backBtn;
    static final String SHARED_PREFS = "cardiacCornerPrefs";

    private void saveData(String Key, String Val) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Key, Val);
        editor.commit();
    }
    public void storeLogs(ArrayList<Entry> log){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String arr = gson.toJson(log);
        editor.putString("logs", arr);
        editor.commit();
    }
    private void signIn(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loggedIn", true);
        editor.commit();
    }

    private boolean loggedIn(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if(sharedPreferences.contains("init")){
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_screen);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.sign_in);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(password.getText().toString())){
                    Toast.makeText(SignInActivity.this, "Username / Password Required", Toast.LENGTH_LONG).show();
                } else {
                    login();
                }
            }
        });

        backBtn = (Button) findViewById(R.id.back_button);
        backBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SignInActivity.this,WelcomeActivity.class);
                        startActivity(i);

                    }
                });
    }
    public void login() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username.getText().toString());
        loginRequest.setPassword(password.getText().toString());

        Call<LoginResponse> loginResponseCall = AuthClient.getUserService().userLogin(loginRequest);


        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    signIn();
                    saveData("refreshToken", response.body().getRefreshToken());
                    saveData("username", username.getText().toString());
                    saveData("password", password.getText().toString());
                    saveData("email", response.body().getEmail());

                    ArrayList<Entry> log = new ArrayList<>();
                    log.add(new Entry("23-06-2021 16:23:07", 120, 80, true, false, false, "Had a good workout", true));
                    log.add(new Entry("30-06-2021 11:23:07", 125, 90, false, false, true, "Had a stressful meeting", true));
                    log.add(new Entry("20-07-2021 19:23:07", 130, 95, false, true, true, "Meeting with the parents", true));
                    log.add(new Entry("21-10-2021 23:23:07", 150, 100, true, true, false, "Good workout", true));
                    log.add(new Entry("23-10-2021 10:23:07", 110, 60, false, false, true, "Bad exam", true));
                    log.add(new Entry("25-10-2021 9:23:07", 115, 75, false, true, true, "Family Dinner", true));
                    log.add(new Entry("01-01-2022 12:23:07", 121, 84, true, false, false, "New Year resolution at the gym", true));
                    storeLogs(log);
                    Intent i = new Intent(SignInActivity.this,MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(SignInActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(SignInActivity.this, "Throwable: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}