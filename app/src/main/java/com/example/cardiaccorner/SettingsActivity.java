package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    Button signoutBtn;
    TextView emailText, accText;
    String emailStr, accStr, refreshToken;


    static final String SHARED_PREFS = "cardiacCornerPrefs";

    private void clearPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
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
}