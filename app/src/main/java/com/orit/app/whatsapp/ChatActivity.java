package com.orit.app.whatsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class ChatActivity extends AppCompatActivity {

 Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar =(Toolbar)findViewById(R.id.private_chat_toolbar);
        setSupportActionBar(toolbar);

        // String title = getIntent().getExtras().get("userName").toString();
         getSupportActionBar().setTitle("Chat");
         getSupportActionBar().setDisplayShowHomeEnabled(true);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }
}
