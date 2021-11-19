package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

public class BreakdownActivity extends AppCompatActivity {

    Button finishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breakdown_screen);

        finishBtn = (Button) findViewById(R.id.finish_button);
        finishBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(BreakdownActivity.this,MainActivity.class);
                        startActivity(i);
                    }
                });

    }
}
