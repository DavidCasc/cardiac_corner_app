package com.example.cardiaccorner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BluetoothConnectInfo extends AppCompatActivity {
    TextView infoBox;
    Button nextBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt_connect_info_screen);

        infoBox = (TextView) findViewById(R.id.infobox);
        nextBtn = (Button) findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BluetoothConnectInfo.this, BluetoothCommunicationInfo.class);
                startActivity(i);
            }
        });
    }
}
