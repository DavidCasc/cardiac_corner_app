package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    // temporarily added this code so that I can get into the main part of the app without having to log in
    Button btnRegister;
    EditText username, password, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        btnRegister = (Button) findViewById(R.id.sign_up);

        btnRegister.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        register();
                    }
                });

    }

    public void register() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(username.getText().toString());
        registerRequest.setPassword(password.getText().toString());
        registerRequest.setEmail(email.getText().toString());

        Call<RegisterResponse> registerResponseCall = AuthClient.getUserService().userRegister(registerRequest);

        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if(response.isSuccessful()) {
                    Intent i = new Intent(SignUpActivity.this,SignInActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(SignUpActivity.this, "Register Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Throwable: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}