package com.orit.app.whatsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class GroupFragment extends Fragment {

    private String TAG = GroupFragment.class.getSimpleName();
    private ListView groupList;
    private ArrayAdapter gropListAdapter;
    private ArrayList<String> groupsName = new ArrayList<>();
    private DatabaseReference dbGroupReference;
    public GroupFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group, container, false);

        dbGroupReference = FirebaseDatabase.getInstance().getReference().child("Groups");
        dbGroupReference.keepSynced(true);

        dbGroupReference.getDatabase().getReference().child("Groups");


        groupList =(ListView)v.findViewById(R.id.group_list_view);

        gropListAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,groupsName);
        groupList.setAdapter(gropListAdapter);

        getGroups();

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String grpName = adapterView.getItemAtPosition(i).toString();

                Intent intent = new Intent(getContext(),GroupActivity.class);
                intent.putExtra("groupName",grpName);
                startActivity(intent);


            }
        });

        return  v;
    }

    private void getGroups()
    {
        dbGroupReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String>  grpSet = new HashSet<>();

                Iterator iterator =dataSnapshot.getChildren().iterator();

                while (iterator.hasNext())
                grpSet.add(((DataSnapshot)(iterator.next())).getKey());

                groupsName.clear();
                groupsName.addAll(grpSet);
                gropListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w(TAG,"Failed to read value",databaseError.toException());
            }
        });
    }
}
