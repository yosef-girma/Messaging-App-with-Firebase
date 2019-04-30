package com.orit.app.whatsapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import  com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orit.app.whatsapp.Adapter.TabAdapter;

public class MainActivity extends AppCompatActivity {

    private    FirebaseDatabase db;
    private    FirebaseUser currentUser;
    private    DatabaseReference rotRef;
    private    TabAdapter tabAdapter;
    private    Toolbar toolbar;
    private    ViewPager viewPager;
    private    TabLayout tabLayout;
    private    FirebaseAuth mauth;

    FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpAuthStateListener();
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        mauth = FirebaseAuth.getInstance();
                 currentUser = mauth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        rotRef = db.getReference();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Hello me");


         tabAdapter = new TabAdapter(getSupportFragmentManager());
         viewPager =(ViewPager)findViewById(R.id.viewPager);
         viewPager.setAdapter(tabAdapter);
         tabLayout =(TabLayout)findViewById(R.id.tabLayout);
         tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item,menu);
        //getting the search view from the menu
        MenuItem searchViewItem = menu.findItem(R.id.find_friends_menu_item);

        //getting search manager from systemservice
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        //getting the search view
        final SearchView searchView = (SearchView) searchViewItem.getActionView();

        //you can put a hint for the search input field
        searchView.setQueryHint("Search your Friends");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //by setting it true we are making it iconified
        //so the search input will show up after taping the search iconified
        //if you want to make it visible all the time make it false
        searchView.setIconifiedByDefault(true);

        //here we will get the search query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //do the search here
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    protected  void onStart()
    {
        super.onStart();

        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);

    if (currentUser == null)
    {

        signMeIn();
    }
    else
    {
    verifyUserExistence();
    }

    }

    private void verifyUserExistence() {


        final String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        rotRef.child("users").child(currentUID).addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                if ((dataSnapshot.child("name").exists()))
                {


                    Log.w(MainActivity.class.getSimpleName(),"Of course exist");

                    Toast.makeText(MainActivity.this,"Welcome",Toast.LENGTH_LONG).show();

                }
                else
                {
                    Log.w(MainActivity.class.getSimpleName(),"Current user id here"+currentUID);
                    Intent settingIntent = new Intent(MainActivity.this,SettingActivity.class);
                    startActivity(settingIntent);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w(MainActivity.class.getSimpleName(),databaseError.toException());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.find_friends_menu_item:
                break;

            case R.id.find_user_menu_item:
                startActivity(new Intent(MainActivity.this,SearchUserActivity.class));
                break;
            case  R.id.group_menu_item:

                createNewGroup();
                break;
            case R.id.sign_out_menu_item:
                mauth.signOut();
                authStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                        if (currentUser == null)
                        {
                            Toast.makeText(MainActivity.this,"Signed out",Toast.LENGTH_LONG).show();
                            signMeIn();

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Already In",Toast.LENGTH_LONG).show();


                        }
                    }
                };


                break;

            case R.id.setting_menu_item:

                Intent settingIntent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(settingIntent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUpAuthStateListener()
    {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser cuser = FirebaseAuth.getInstance().getCurrentUser();

                if (cuser != null)
                    Toast.makeText(MainActivity.this,"Signed in found",Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                }

            }
        };
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null)
            FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);

    }

    private void createNewGroup() {

        AlertDialog.Builder alerDlg = new AlertDialog.Builder(this);

        alerDlg.setTitle("Group Name");

        final EditText groupName = new EditText(MainActivity.this);

        alerDlg.setView(groupName);

        alerDlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (! groupName.getText().toString().isEmpty())
                saveGroupName(groupName.getText().toString());
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        AlertDialog dlg =alerDlg.create();
        dlg.show();
    }

    private void saveGroupName(final String groupName) {

    rotRef.child("Groups").child(groupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {

            if (task.isSuccessful())
                Toast.makeText(MainActivity.this,groupName +" Group Created",Toast.LENGTH_LONG).show();
            else
                Log.d(MainActivity.class.getSimpleName(),task.getException().toString());
        }
    });

    }

    private void signMeIn() {

        Intent loginLauncher = new Intent(MainActivity.this,LoginActivity.class);

        startActivity(loginLauncher);


    }

}
