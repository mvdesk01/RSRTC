package com.mtech.rsrtcsc.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.repository.cache.PrefrenceHelper;
import com.mtech.rsrtcsc.repository.cache.PrefrenceKeyConstant;

public class Profile extends AppCompatActivity {
    TextView name,mobile,email;
    ImageView img;
    Button Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        img = findViewById(R.id.ivHumberger);
        Back = findViewById(R.id.back);


        name.setText(PrefrenceHelper.getPrefrenceStringValue(Profile.this, PrefrenceKeyConstant.FULL_NAME));
        mobile.setText(PrefrenceHelper.getPrefrenceStringValue(Profile.this, PrefrenceKeyConstant.PHONE_NO));
        email.setText(PrefrenceHelper.getPrefrenceStringValue(Profile.this, PrefrenceKeyConstant.EMAIL_ID));


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, MainActivity.class));

            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, MainActivity.class));
            }
        });


    }
}