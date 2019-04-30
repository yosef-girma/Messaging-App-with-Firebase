package com.orit.app.whatsapp.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orit.app.whatsapp.Chat;
import com.orit.app.whatsapp.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Joseph on 4/29/2019.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private Context context;
    private ArrayList<Chat> chatList;
    public GroupAdapter(Context context,ArrayList<Chat> chatList)
    {
    this.context  = context;
    this.chatList = chatList;
    }

    @NonNull
    @Override
    public GroupAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_chat,parent,false);

        GroupViewHolder groupViewHolder = new GroupViewHolder(v);
        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupViewHolder holder, int position)
    {

        Chat chat = chatList.get(position);

        //Uri uri = Uri.parse(chat.getProfile());
        //holder.grpChatUserPic.setImageURI(uri);

        Log.i("Chat","This is user"+chat.getUser());
        Glide.with(context).load(R.drawable.shortcut_user).into(holder.grpChatUserPic);
        holder.grpChatUser.setText(chat.getUser());
        holder.grpChatMessage.setText(chat.getMessage());
        holder.grpChatDate.setText(chat.getDate());
        holder.grpChatTime.setText(chat.getTime());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder
    {

        CircleImageView grpChatUserPic;
        TextView grpChatUser,grpChatMessage,grpChatTime,grpChatDate;
        public GroupViewHolder(View itemView) {
            super(itemView);

            grpChatUserPic = (CircleImageView)itemView.findViewById(R.id.group_chat_profile_pic);
            grpChatUser    = (TextView)itemView.findViewById(R.id.group_chat_user);
            grpChatMessage = (TextView)itemView.findViewById(R.id.group_chat_message);
            grpChatTime    = (TextView)itemView.findViewById(R.id.group_chat_message_time);
            grpChatDate    = (TextView)itemView.findViewById(R.id.group_chat_date);

        }
    }
}
