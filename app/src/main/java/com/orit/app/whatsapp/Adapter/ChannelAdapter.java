package com.orit.app.whatsapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orit.app.whatsapp.Model.Channel;
import com.orit.app.whatsapp.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Joseph on 7/12/2019.
 */

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder> {

     public List<Channel> channelList;
     private Context context;
     public ChannelAdapter(Context context,List<Channel> channelList)
    {
        this.channelList = channelList;
        this.context = context;

    }
    @NonNull
    @Override
    public ChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =    LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_cardview,parent,false);


        return  new ChannelViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ChannelViewHolder holder, int position) {


        Channel channel    =  channelList.get(position);
        String channelPic  =  channel.getProfilePic();
        String channelName =  channel.getName();

        Glide.with(context).load(channelPic).placeholder(R.drawable.shortcut_user).into(holder.channelPic);
        holder.channelName.setText(channelName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Click",Toast.LENGTH_LONG).show();

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context,"Long Click",Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }

    public class ChannelViewHolder extends RecyclerView.ViewHolder
    {

     CircleImageView channelPic;
     TextView channelName;

        public ChannelViewHolder(View itemView)
        {
            super(itemView);

            channelPic  = itemView.findViewById(R.id.channel_pic);
            channelName = itemView.findViewById(R.id.channel_name);

        }


    }

}
