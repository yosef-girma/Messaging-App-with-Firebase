package com.orit.app.whatsapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orit.app.whatsapp.Model.User;
import com.orit.app.whatsapp.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserActivity extends AppCompatActivity {

    private RecyclerView searchRecylcerView;
    private Toolbar toolbar;
    private List<User> userList = new ArrayList<>();

    DatabaseReference databaseReference;
    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        searchRecylcerView= (RecyclerView)findViewById(R.id.search_recyclerView);
        toolbar = (Toolbar)findViewById(R.id.search_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find Friends");
        searchRecylcerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public class SearchViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView imgview;
        TextView username;
        TextView status ;

        public SearchViewHolder(View itemView) {
            super(itemView);
            imgview = itemView.findViewById(R.id.card_profile_pic);
            username = itemView.findViewById(R.id.card_user);
            status   = itemView.findViewById(R.id.card_preview);

        }
    }

}
