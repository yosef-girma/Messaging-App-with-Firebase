package com.orit.app.whatsapp.Test;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orit.app.whatsapp.R;

import java.util.ArrayList;
import java.util.List;



public class ShowImageActivity extends AppCompatActivity {



    ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    public List<Upload> uploadList;
    public RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    //@BindView(R.id.recyclerView)     RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);



//        ButterKnife.bind(this);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
     recyclerView.setHasFixedSize(true);
     recyclerView.setLayoutManager(new LinearLayoutManager(this));


      uploadList = new ArrayList<>();


     databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_UPLOADS);


     progressDialog = new ProgressDialog(this);

     progressDialog.setTitle("please wait");


     databaseReference.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {

             progressDialog.dismiss();
             for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
             {

                 Upload upload = dataSnapshot1.getValue(Upload.class);

                 uploadList.add(upload);

             }
             //creating adapter
             adapter = new MyAdapter(getApplicationContext(), uploadList);

             //adding adapter to recyclerview
             recyclerView.setAdapter(adapter);

         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

             progressDialog.dismiss();
         }
     });

    }
}
