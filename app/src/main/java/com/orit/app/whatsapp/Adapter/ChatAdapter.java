package com.orit.app.whatsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orit.app.whatsapp.Activity.ChatActivity;
import com.orit.app.whatsapp.R;
import com.orit.app.whatsapp.Model.User;

import java.util.ArrayList;

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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_cardview,parent,false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User user = users.get(position);
        final String username   = user.getUser();;
        final String userPic    = user.getImage();
        final String userStatus = user.getStatus();
        final String uid        = user.getUid();

        holder.status.setText(userStatus);
        holder.username.setText(username);

        Glide.with(context).load(userPic).placeholder(R.drawable.shortcut_user).into(holder.imgview);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                launchChatActivity(username,userPic,uid);
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(context,"Long Click",Toast.LENGTH_LONG).show();

                return true;
            }
        });
    }

    private void launchChatActivity(String username,String userPic,String uid) {

    Intent launcher = new Intent(context,ChatActivity.class);

    launcher.putExtra("username",username);
    launcher.putExtra("image",userPic);
    launcher.putExtra("uid",uid);



    context.startActivity(launcher);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        View mView;
        CircleImageView imgview;
        TextView username;
        TextView status ;

        public MyViewHolder(View itemView) {
            super(itemView);

            mView= itemView;
            imgview = itemView.findViewById(R.id.card_profile_pic);
            username = itemView.findViewById(R.id.card_user);
            status   = itemView.findViewById(R.id.card_preview);
          }

    }
}
