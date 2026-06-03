package com.mtech.rsrtcsc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.mtech.rsrtcsc.ui.activity.main.MainActivity;

public class ContactUs extends AppCompatActivity {
    ImageView img;
    Button Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        img = findViewById(R.id.ivHumberger);
        Back = findViewById(R.id.btn_back);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactUs.this, MainActivity.class));

            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactUs.this, MainActivity.class));
            }
        });
    }
}