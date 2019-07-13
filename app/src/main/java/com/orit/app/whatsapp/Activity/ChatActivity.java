package com.orit.app.whatsapp.Activity;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orit.app.whatsapp.Adapter.MessageAdapter;
import com.orit.app.whatsapp.Model.Message;
import com.orit.app.whatsapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {


    private CircleImageView circleImageView;
    private TextView userName;
    private TextView lastSeen;
    private EditText privateChatMessage;
    private Toolbar toolbar;
    private ScrollView privateChatScrollView;
    private DatabaseReference rootReference;
    private FirebaseAuth mauth; 
    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private RecyclerView privateChatRecyclerView;
    private String senderUid,receiverUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar =(Toolbar)findViewById(R.id.private_chat_toolbar);
        setSupportActionBar(toolbar);


        String username = getIntent().getExtras().getString("username");
        String image    = getIntent().getExtras().getString("image");
        receiverUid     = getIntent().getExtras().getString("uid");

        mauth = FirebaseAuth.getInstance();
        rootReference = FirebaseDatabase.getInstance().getReference();
        senderUid = mauth.getCurrentUser().getUid();


        // String title = getIntent().getExtras().get("userName").toString();

        ActionBar actionBar = getSupportActionBar();

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =  inflater.inflate(R.layout.custom_chat_bar,null);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view);
        circleImageView =(CircleImageView)findViewById(R.id.custom_chat_bar_circleImageview);
        userName = (TextView)findViewById(R.id.custom_chat_bar_username);
        lastSeen = (TextView)findViewById(R.id.custom_chat_bar_last_seen);
        userName.setText(username);
        Glide.with(this).load(Uri.parse(image)).into(circleImageView);
        privateChatMessage = (EditText)findViewById(R.id.private_chat_message);
        privateChatScrollView = (ScrollView) findViewById(R.id.private_chatScrollView);

        privateChatMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final  int DRAWABLE_LEFT = 0;
                final  int DRAWABLE_TOP = 1;
                final  int DRAWABLE_RIGHT = 2;
                final  int DRAWABLE_BOTTOM = 3;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if (motionEvent.getRawX() >= (privateChatMessage.getRight() - privateChatMessage.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
                    {
                        sendMessage();
                        privateChatMessage.setText("");
                        privateChatScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }return false;
            }


        });

        Toast.makeText(this,"Call Message List" + messageList.size(),Toast.LENGTH_LONG).show();
        messageAdapter = new MessageAdapter(this,messageList);

        privateChatRecyclerView = (RecyclerView)findViewById(R.id.private_chat_recyclerView);
        privateChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        privateChatRecyclerView.setItemAnimator(new DefaultItemAnimator());
        privateChatRecyclerView.setAdapter(messageAdapter);

    }


    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(ChatActivity.this,"Call Message onStart" + messageList.size(),Toast.LENGTH_LONG).show();
        rootReference.child("Chat").child(senderUid).child(receiverUid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Toast.makeText(getApplicationContext(),"Call Message child added" + messageList.size(),Toast.LENGTH_LONG).show();
if (dataSnapshot.exists())
{

    Message message = dataSnapshot.getValue(Message.class);
    Toast.makeText(ChatActivity.this,"Message"+message.getMessage(),Toast.LENGTH_LONG).show();

    messageList.add(message);
    messageAdapter.notifyDataSetChanged();
    privateChatRecyclerView.smoothScrollToPosition(privateChatRecyclerView.getAdapter().getItemCount());


}


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Message message = dataSnapshot.getValue(Message.class);
                Toast.makeText(ChatActivity.this,"Message"+message.getMessage(),Toast.LENGTH_LONG).show();


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendMessage() {

        String message = privateChatMessage.getText().toString();


        if (TextUtils.isEmpty(message))
        {
            Toast.makeText(this,"please write something",Toast.LENGTH_LONG).show();
        }
        else
        {

          DatabaseReference chatKeyReference = rootReference.child("Chat").child(senderUid).child(receiverUid).push();
          String  chatKey = chatKeyReference.getKey();


            String senderReference   = "Chat/"+senderUid+"/"+receiverUid;
            String receiverReference = "Chat/"+receiverUid +"/"+senderUid;


            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd ,yyyy");
            String currentDate = dateFormat.format(calendar.getTime());
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            String currentTime = timeFormat.format(calendar.getTime());

            HashMap<String,Object> chatText = new HashMap<>();

            chatText.put("message",message);
            chatText.put("type","text");
            chatText.put("from",senderUid);
            chatText.put("date",currentDate);
            chatText.put("time",currentTime);

          HashMap chatTextDetail = new HashMap();
          chatTextDetail.put(senderReference+chatKey,chatText);
          chatTextDetail.put(receiverReference +chatKey,chatText);
          rootReference.updateChildren(chatTextDetail).addOnCompleteListener(new OnCompleteListener() {
              @Override
              public void onComplete(@NonNull Task task) {

                  if (task.isSuccessful())
                  {
                      Toast.makeText(ChatActivity.this,"Success",Toast.LENGTH_LONG).show();
                  }
                  else
                  {
                      Toast.makeText(ChatActivity.this,"Error",Toast.LENGTH_LONG).show();
                      Log.e("ChatActivity",task.getException().toString());

                  }

              }
          });



        }

    }
}
