package com.orit.app.whatsapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orit.app.whatsapp.ChatActivity;
import com.orit.app.whatsapp.R;
import com.orit.app.whatsapp.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
/**
 * Created by Joseph on 4/23/2019.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {


    public Context context;
    public ArrayList<User> users ;
    public ChatAdapter(Context context,ArrayList<User> users)
    {

        this.context = context;
        this.users   = users;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_cardview,parent);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User user = users.get(position);
        holder.status.setText(user.getStatus());
        holder.username.setText(user.getUser());
        Glide.with(context).load(user.getUid()).into(holder.imgview);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        CircleImageView imgview;
        TextView username;
        TextView status ;

        public MyViewHolder(View itemView) {
            super(itemView);

            imgview = itemView.findViewById(R.id.card_profile_pic);
            username = itemView.findViewById(R.id.card_user);
            status   = itemView.findViewById(R.id.card_preview);
          }

    }
}
