package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    Button signoutBtn, clearDataBtn, backBtn;
    TextView emailText, accText;
    String emailStr, accStr, refreshToken;
    int count = 3;
    Toast toastMessage;

    Timer timer = new Timer();


    static final String SHARED_PREFS = "cardiacCornerPrefs";

    private void clearPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    private void clearPrefsData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("logs", "");
        editor.commit();
    }
    public String loadData(String Key) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(Key, "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen);
        emailText = (TextView) findViewById(R.id.textView);
        accText = (TextView) findViewById(R.id.textView3);

        emailStr = "Email: " + loadData("email");
        accStr = "Username: " + loadData("username");
        refreshToken = loadData("refreshToken");



        emailText.setText(emailStr);
        accText.setText(accStr);

        signoutBtn = (Button) findViewById(R.id.sign_out);
        signoutBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logoutDelete();
                        clearPrefs();
                        Intent i = new Intent(SettingsActivity.this,WelcomeActivity.class);
                        startActivity(i);
                    }
                });

        clearDataBtn = (Button) findViewById(R.id.clear_data);

        clearDataBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(count == 3){
                            count--;
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(3000);
                                        count = 3;
                                        clearDataBtn.setBackgroundColor(Color.parseColor("#673AB7"));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 0);
                            if(toastMessage != null){
                                toastMessage.cancel();
                            }
                            toastMessage = Toast.makeText(SettingsActivity.this, String.format("Press clear data button %d more to unlock", count), Toast.LENGTH_LONG);
                            toastMessage.show();
                        } else if (count == 0){
                            clearData();
                            clearPrefsData();
                        } else if (count == 1){
                            count--;
                            clearDataBtn.setBackgroundColor(getResources().getColor(R.color.red));
                            if(toastMessage != null){
                                toastMessage.cancel();
                            }
                            toastMessage = Toast.makeText(SettingsActivity.this, "Press clear data button again to clear", Toast.LENGTH_LONG);
                            toastMessage.show();
                        } else {
                            count--;
                            if(toastMessage != null){
                                toastMessage.cancel();
                            }
                            toastMessage = Toast.makeText(SettingsActivity.this, String.format("Press clear data button %d more to unlock", count), Toast.LENGTH_LONG);
                            toastMessage.show();
                        }

                    }
                }
        );

        backBtn = (Button) findViewById(R.id.back_button);
        backBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SettingsActivity.this,MainActivity.class);
                        startActivity(i);

                    }
                });
    }

    public void logoutDelete() {
        Call<LogoutResponse> ResponseCall = AuthClient.getUserService().logout(refreshToken);
        ResponseCall.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {

            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {

            }
        });

    }
    public void clearData() {
        Call<ClearLogsResponse> ResponseCall = ApiClient.getUserService().deleteAll(loadData("username"));
        ResponseCall.enqueue(new Callback<ClearLogsResponse>() {
            @Override
            public void onResponse(Call<ClearLogsResponse> call, Response<ClearLogsResponse> response) {

            }

            @Override
            public void onFailure(Call<ClearLogsResponse> call, Throwable t) {

            }
        });
    }
}