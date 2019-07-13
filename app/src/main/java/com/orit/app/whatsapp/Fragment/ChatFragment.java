package com.orit.app.whatsapp.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orit.app.whatsapp.Adapter.ChatAdapter;
import com.orit.app.whatsapp.Model.User;
import com.orit.app.whatsapp.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class ChatFragment extends Fragment {


    DatabaseReference databaseReference;
    ChatAdapter chatAdapter;
    RecyclerView chatRecyclerView;
    ArrayList<User> copy;
    View v;

    @Override
    public void onStart() {
        super.onStart();

    }

    public ChatFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        v =inflater.inflate(R.layout.fragment_chat, container, false);

       copy = new ArrayList<>();
//       users.add(new User("","yosef","abc","not noeedd"));

        final Set<User> users= new HashSet<User>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    User userData;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        DataSnapshot nameChild   = dataSnapshot1.child("name");
                        DataSnapshot statusChild = dataSnapshot1.child("status");
                        DataSnapshot imageChild  = dataSnapshot1.child("image");
                        if (nameChild.exists() && statusChild.exists() && imageChild.exists()) {
                            String username =   nameChild.getValue().toString();
                            String status   =   statusChild.getValue().toString();
                            String image    =   imageChild.getValue().toString();
                            String uid = dataSnapshot1.child("uid").getValue().toString();


                            userData = new User(image, username, status, uid);

                            users.add(userData);
                            //addToUser(userData);
                            Log.i("Chat", username + " status " + status);
                            // StringBuilder stringBuilder = new StringBuilder();
                            // stringBuilder.append("User" + username + " status " + status);
                            // temp.setText(stringBuilder);
                            //User user = dataSnapshot1.getValue(User.class);

                        } else {
                            Log.i("Chat ", "DataSnapshot");
                        }
                    }
                    if(users.size() >0 )
                    {
//                        Toast.makeText(getContext(),"Here users not nulll inside",Toast.LENGTH_LONG).show();

                        copy.clear();
                        copy.addAll(users);

                        chatAdapter =new ChatAdapter(getContext(),copy);
                        chatAdapter.notifyDataSetChanged();
                        chatRecyclerView = (RecyclerView)v.findViewById(R.id.chatRecyclerView);
                        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        chatRecyclerView.setHasFixedSize(true);
                        chatRecyclerView.setAdapter(chatAdapter);
                    }
                    else
                    {

                        Toast.makeText(getContext(),"Here users is null inside" ,Toast.LENGTH_LONG).show();

                        Log.i("crazy is null","");
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        chatAdapter =new ChatAdapter(getContext(),new ArrayList<User>());

       chatRecyclerView = (RecyclerView)v.findViewById(R.id.chatRecyclerView);
       chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       chatRecyclerView.setHasFixedSize(true);
       chatRecyclerView.setAdapter(chatAdapter);


       return v;
    }

    private void addToUser(User user)
    {
        Log.i("calling","add to user called");
    //    users.add(user);
    }

}
