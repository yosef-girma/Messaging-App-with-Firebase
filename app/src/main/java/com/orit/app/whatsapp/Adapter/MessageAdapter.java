package com.orit.app.whatsapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orit.app.whatsapp.Model.Message;
import com.orit.app.whatsapp.R;

import java.util.List;

/**
 * Created by Joseph on 5/6/2019.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{


    public Context context;
    public List<Message> messageList;

    private  FirebaseAuth mAuth;
    private DatabaseReference userRef;

    public MessageAdapter(Context context,List<Message> messageList)
    {
        this.context     = context;
        this.messageList = messageList;

    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_message_layout,parent,false);

        MessageViewHolder messageViewHolder = new MessageViewHolder(view);

        mAuth = FirebaseAuth.getInstance();
        return messageViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        String senderMessageId = mAuth.getCurrentUser().getUid();

        Message message = messageList.get(position);
        String fromUserId = message.getFrom();

        Toast.makeText(context,"on bind "+ message.getMessage(),Toast.LENGTH_LONG).show();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(fromUserId);


        holder.receiverMessage.setVisibility(View.INVISIBLE);
        holder.receiverTime.setVisibility(View.INVISIBLE);
        holder.senderMessage.setVisibility(View.INVISIBLE);
        holder.senderTime.setVisibility(View.INVISIBLE);

        if (fromUserId.equals(senderMessageId))
        {
            holder.senderMessage.setVisibility(View.VISIBLE);
            holder.senderTime.setVisibility(View.VISIBLE);
            holder.senderMessage.setBackgroundResource(R.drawable.sender_message);
            holder.senderMessage.setTextColor(Color.BLACK);
            holder.senderMessage.setText(message.getMessage());
        }
        else
        {
            holder.receiverMessage.setVisibility(View.VISIBLE);
            holder.receiverTime.setVisibility(View.VISIBLE);
            holder.receiverMessage.setBackgroundResource(R.drawable.receiver_message);
            holder.receiverMessage.setTextColor(Color.BLACK);
            holder.receiverMessage.setText(message.getMessage());

        }
    }

    @Override
    public int getItemCount() {

        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder
    {


        TextView senderMessage,senderTime,receiverMessage,receiverTime;

        public MessageViewHolder(View itemView) {
            super(itemView);

            senderMessage  = (TextView)itemView.findViewById(R.id.sender_message);
            senderTime     = (TextView) itemView.findViewById(R.id.sent_message_time);
            receiverMessage = (TextView)itemView.findViewById(R.id.receiver_message);
            receiverTime    = (TextView)itemView.findViewById(R.id.receiver_message_time);






        }
    }
}
