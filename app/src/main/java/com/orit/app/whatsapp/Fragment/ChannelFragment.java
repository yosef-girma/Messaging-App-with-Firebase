package com.orit.app.whatsapp.Fragment;

import android.net.Uri;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orit.app.whatsapp.Adapter.ChannelAdapter;
import com.orit.app.whatsapp.Model.Channel;
import com.orit.app.whatsapp.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class ChannelFragment extends Fragment {

    private List<Channel> channelList = new ArrayList<>();
    private ChannelAdapter channelAdapter;
    private DatabaseReference channelDbRef;
    private View view;
    RecyclerView channelRecyclerView;
    public ChannelFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
          view = inflater.inflate(R.layout.fragment_channel, container, false);
          channelRecyclerView = view.findViewById(R.id.channleRecyclerView);
          channelDbRef = FirebaseDatabase.getInstance().getReference().child("channel");
          channelAdapter = new ChannelAdapter(getContext(), channelList);
          channelRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
          channelRecyclerView.setHasFixedSize(true);
          channelRecyclerView.setAdapter(channelAdapter);

        Toast.makeText(getContext(),"channel",Toast.LENGTH_LONG).show();
        Log.i("Channel","Ping Channel");

         channelDbRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 Set<Channel> channels = new HashSet<>();
                 if(dataSnapshot.exists())
                 {

                     Toast.makeText(getContext(),"datasnapshot exist",Toast.LENGTH_LONG).show();
                     for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                     {
                         DataSnapshot nameChild = dataSnapshot1.child("name");
                         DataSnapshot profileChild = dataSnapshot1.child("pic");

                         Uri defaultPic = Uri.parse("android.resource://com.orit.app.whatsapp/drawable/shortcut_user");

                         String name=null,pic=defaultPic.toString();

                        // if(profileChild.exists()){
                             //pic = profileChild.getValue().toString();

                         //}

                         if(nameChild.exists()) {

                             Toast.makeText(getContext(),"name child exist",Toast.LENGTH_LONG).show();
                             name = nameChild.getValue().toString();
                             Channel channel = new Channel(pic,name);
                             channels.add(channel);
                         }

                     }

                     if(! channelList.isEmpty()) {
                         Toast.makeText(getContext(),"inside channlelist not empty",Toast.LENGTH_LONG).show();
                         channelList.clear();
                         channelList.addAll(channels);

                         channelAdapter.notifyDataSetChanged();
                          }
                      else
                     {
                         Log.w("ChannleFragment","No Channel there");

                         Toast.makeText(getContext(),"datasnapshot  not exist",Toast.LENGTH_LONG).show();
                     }

                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
               Log.w("Channel Fragment","Canceled Channel name");
             }
         });

        return view;
    }
}
