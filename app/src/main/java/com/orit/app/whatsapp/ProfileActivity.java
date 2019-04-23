package com.orit.app.whatsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    String receiverUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        receiverUserId = getIntent().getExtras().get("userId").toString();
        Toast.makeText(ProfileActivity.this,"Receiver Id"+receiverUserId,Toast.LENGTH_LONG).show();


    }
}
