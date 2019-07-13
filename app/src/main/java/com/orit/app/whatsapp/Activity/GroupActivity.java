package com.orit.app.whatsapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orit.app.whatsapp.Adapter.GroupAdapter;
import com.orit.app.whatsapp.Model.Chat;
import com.orit.app.whatsapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;


public class GroupActivity extends AppCompatActivity {

    private Toolbar grpToolbar;
    private EditText chatMessageInput;
    private ScrollView scrollView;
    private TextView displayChat;
    private FirebaseAuth auth;
    private DatabaseReference userReference,groupNameReference,messageKeyReference;
    private String groupName,currentUserName,currentUId;
    private FirebaseAuth.AuthStateListener authStateListener;
    private RecyclerView groupChatRecyclerView;
    private GroupAdapter groupChatAdapter;
    private ArrayList<Chat> chatList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);


    groupName = getIntent().getExtras().get("groupName").toString();
    grpToolbar =(Toolbar)findViewById(R.id.group_chat_toolbar);
    setSupportActionBar(grpToolbar);
    getSupportActionBar().setTitle(groupName);



    auth = FirebaseAuth.getInstance();
    currentUId = auth.getCurrentUser().getUid();
    userReference = FirebaseDatabase.getInstance().getReference().child("users");
    userReference.keepSynced(true);

    groupNameReference =FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName);

     intializeField();
    getUserInfo();

        chatMessageInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final  int DRAWABLE_LEFT = 0;
                final  int DRAWABLE_TOP = 1;
                final  int DRAWABLE_RIGHT = 2;
                final  int DRAWABLE_BOTTOM = 3;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if (motionEvent.getRawX() >= (chatMessageInput.getRight() - chatMessageInput.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
                    {
                        sendMessage();
                        chatMessageInput.setText("");
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }return false;
            }


        });

    }
    @Override
    protected void onStart()
    {
        super.onStart();


      groupNameReference.keepSynced(true);
      groupNameReference.addChildEventListener(new ChildEventListener() {
          @Override
          public void onChildAdded(DataSnapshot dataSnapshot, String s) {

              if (dataSnapshot.exists())
              {
                  displayMessages(dataSnapshot);
              }
          }

          @Override
          public void onChildChanged(DataSnapshot dataSnapshot, String s) {

              if (dataSnapshot.exists())
              {
                  displayMessages(dataSnapshot);
              }
          }

          @Override
          public void onChildRemoved(DataSnapshot dataSnapshot) {

          }

          @Override
          public void onChildMoved(DataSnapshot dataSnapshot, String s) {

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });
    }


    private void displayMessages(DataSnapshot dataSnapshot) {

        Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext())
        {
            String chatDate    = ((DataSnapshot)iterator.next()).getValue().toString();
            String chatMessage = ((DataSnapshot)iterator.next()).getValue().toString();
            String chatName    = ((DataSnapshot)iterator.next()).getValue().toString();
            String chatTime    = ((DataSnapshot)iterator.next()).getValue().toString();
            Chat chat          = new Chat(chatName,chatMessage,chatDate,chatTime);
            Log.i("Chat ","Display user"+chat.getUser());
            chatList.add(chat);

        }


        Log.i("Chat List Size","Size"+chatList.size());
        groupChatAdapter = new GroupAdapter(getApplicationContext(),chatList);
        groupChatRecyclerView = (RecyclerView)findViewById(R.id.group_chat_recyclerView);
        groupChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupChatRecyclerView.setHasFixedSize(true);

        groupChatRecyclerView.setAdapter(groupChatAdapter);
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);

    }

    public void sendMessage() {

        String message = chatMessageInput.getText().toString().trim();
        String messageKey = groupNameReference.push().getKey();

        if(!TextUtils.isEmpty(message))
        {
            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd ,yyyy");

            String currentDate = dateFormat.format(calendar.getTime());

            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

            String currentTime = timeFormat.format(calendar.getTime());

            HashMap<String,Object> groupMessageKey = new HashMap<>();

            groupNameReference.updateChildren(groupMessageKey);

            messageKeyReference = groupNameReference.child(messageKey);

            HashMap<String,Object>  messageUnit =new HashMap<>();

            Toast.makeText(GroupActivity.this,currentUserName,Toast.LENGTH_LONG).show();
           // Log.i("UserName",currentUserName);

            messageUnit.put("name",currentUserName);
            messageUnit.put("message",message);
            messageUnit.put("date",currentDate);
            messageUnit.put("time",currentTime);

            messageKeyReference.updateChildren(messageUnit);
            chatMessageInput.setText("");
        }
    }

    private void getUserInfo() {

        //currentUserName =

                userReference.child(currentUId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists())
                        {
                            currentUserName = dataSnapshot.child("name").getValue().toString();
                            Toast.makeText(GroupActivity.this,currentUserName+ "found",Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(GroupActivity.this,"Does not found",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Log.w("Group",databaseError.toException());
                    }
                });

    }

    public  void intializeField()
    {
        chatMessageInput =(EditText)findViewById(R.id.chatMessage);
        scrollView  =(ScrollView)findViewById(R.id.grpScrollView);


    }
}
