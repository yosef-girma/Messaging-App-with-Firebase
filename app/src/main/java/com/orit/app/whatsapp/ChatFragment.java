package com.orit.app.whatsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orit.app.whatsapp.Adapter.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatFragment extends Fragment {

    DatabaseReference databaseReference;
    ChatAdapter chatAdapter;
    ArrayList<User> users;
    RecyclerView chatRecyclerView;
    public ChatFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =inflater.inflate(R.layout.fragment_chat, container, false);
       users = new ArrayList<User>();
       chatAdapter =new ChatAdapter(getContext(),users);
       databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
       chatRecyclerView = (RecyclerView)v.findViewById(R.id.chatRecyclerView);
       chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       chatRecyclerView.setHasFixedSize(true);
       chatRecyclerView.setAdapter(chatAdapter);


       return v;
    }

}
