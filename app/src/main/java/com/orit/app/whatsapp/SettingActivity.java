package com.orit.app.whatsapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{


    private DatabaseReference rootReference;
    private CircleImageView profileImageView;
    private CircleImageView skipNow;
    private EditText userNameEditText,profileStatusEditText;
    private Button updateProfileBtn;
    private String currentUser ;
    private Toolbar toolbar;
    private  ProgressDialog progressDialog;
    private StorageReference userProfileStorageReference;
    private static  final int PICK_IMAGE_REQUEST = 1000;
    private Uri  profileImageUri = Uri.parse("android.resource://com.orit.app.whatsapp/drawable/shortcut_user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_setting);


     userProfileStorageReference = FirebaseStorage.getInstance().getReference().child("Profile");

     toolbar =(Toolbar)findViewById(R.id.setting_toolbar);
     setSupportActionBar(toolbar);
     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     getSupportActionBar().setDisplayShowHomeEnabled(true);


     currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
     rootReference = FirebaseDatabase.getInstance().getReference();
     profileImageView    = (CircleImageView)findViewById(R.id.profile_picture);
     skipNow             = (CircleImageView)findViewById(R.id.skip_now);
     userNameEditText    =  (EditText)findViewById(R.id.user_name);
     profileStatusEditText =  (EditText)findViewById(R.id.profile_status);
     updateProfileBtn      =  (Button) findViewById(R.id.update_profile);
     progressDialog =new ProgressDialog(SettingActivity.this);
     skipNow.setOnClickListener(this);
     updateProfileBtn.setOnClickListener(this);
     //profileImageView.setOnClickListener(this);
     profileImageView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
           //  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            // intent.setType("image/*");
            // startActivityForResult(intent,PICK_IMAGE_REQUEST);

             CropImage.activity().start(SettingActivity.this);

         }
     });



getUserInfo();

    }


    private  void getUserInfo()
    {

        rootReference.child("users").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String imageUrl = null,username = null ,status = null;
                if(dataSnapshot.child("name").exists() && dataSnapshot.child("status").exists() &&
                        dataSnapshot.child("image").exists())
                {
                        username = dataSnapshot.child("name").getValue().toString();
                        status   = dataSnapshot.child("status").getValue().toString();
                        if (dataSnapshot.child("image").exists())
                        imageUrl = dataSnapshot.child("image").getValue().toString();
                        else
                            imageUrl= profileImageUri.toString();

                        userNameEditText.setText(username);
                         profileStatusEditText.setText(status);

                         Glide.with(SettingActivity.this).load(imageUrl)
                            .into(profileImageView);

                }
                else
                {
                    Toast.makeText(SettingActivity.this,"Setup your profile rightaway",Toast.LENGTH_LONG).show();

                }






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(SettingActivity.this,"some eror whiel rettre",Toast.LENGTH_LONG).show();
            }
        });



    }

    public String getUserName()
    {
        String userName = userNameEditText.getText().toString().trim();
        if (! TextUtils.isEmpty(userName))
        return userName;
        else
        {
            String userEmail =FirebaseAuth.getInstance().getCurrentUser().getEmail();
            try {

                if (! TextUtils.isEmpty(userEmail)) {
                Toast.makeText(SettingActivity.this,userEmail,Toast.LENGTH_LONG).show();

                    return userEmail;
                }
                else
                {
                    userEmail = "user";
                    return userEmail;
                }


            }
            catch (Exception e)
            {
             Log.w("TAG",e.getMessage());
            }
        return userEmail;
        }


    }
    public String getProfileStatus()
    {
        String profileStatus = profileStatusEditText.getText().toString().trim();
        if (! TextUtils.isEmpty(profileStatus))
        {
            return  profileStatus;
        }
        else
        {
            return "no profile status yet";
        }
    }

    private void updateSetting() {

       String profileStatus = profileStatusEditText.getText().toString().trim();

   {
       StorageReference  childSRef = userProfileStorageReference.child(currentUser + "."+getFileExtension(profileImageUri));
       childSRef.putFile(profileImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


               if (task.isSuccessful())
               {
                  //rootReference.child("users").child(currentUser).child("image").setValue(task.getResult().getDownloadUrl().toString());

                   HashMap<String,Object>  profile = new HashMap<>();


                   profile.put("uid", currentUser);
                   profile.put("name",getUserName());
                   profile.put("status",getProfileStatus());
                   // get download uri
                   profile.put("image",task.getResult().getUploadSessionUri().toString());
                   rootReference.child("users").child(currentUser).setValue(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()) {
                               Toast.makeText(SettingActivity.this,"Profile Updated Successfully",Toast.LENGTH_LONG).show();
                               launchHome();}
                           else
                           {Log.e(SettingActivity.class.getSimpleName(),task.getException().toString());
                           }
                       }
                   });

                   Toast.makeText(SettingActivity.this,"here i am done",Toast.LENGTH_LONG).show();

               }
               else
               {
                   Toast.makeText(SettingActivity.this,task.getException().toString(),Toast.LENGTH_LONG).show();
               }
           }
       });

   }


    }
    private void launchHome() {
        Intent homeIntent = new Intent(SettingActivity.this,MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
       // finish();
        startActivity(homeIntent);


    }


    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.update_profile:
                updateSetting();

                break;
            case R.id.skip_now:
                //startActivity(new Intent(SettingActivity.this,MainActivity.class));
                break;


        }

    }

    private void showFileChooser()
    {




        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileImageUri = result.getUri();

               // progressDialog.setTitle("uploading");
               // progressDialog.setCancelable(true);
               // progressDialog.show();


            }

            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();

                Toast.makeText(SettingActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        }
        else
            {

                Toast.makeText(SettingActivity.this,"cool",Toast.LENGTH_LONG).show();
         }
    }
      /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == PICK_IMAGE_REQUEST  && requestCode == RESULT_OK  && data != null ) {


            Uri imageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);


        }

            if ( requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ) {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK)
                {
                Uri resultUri = result.getUri();



                final ProgressDialog progressDialog =new ProgressDialog(SettingActivity.this);
                progressDialog.setTitle("uploading");
                progressDialog.setCancelable(true);
                StorageReference  childSRef = userProfileStorageReference.child(currentUser + "."+getFileExtension(resultUri));



                progressDialog.show();

                childSRef.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        progressDialog.dismiss();

                        Toast.makeText(SettingActivity.this,"Profile Pic uploaded",Toast.LENGTH_LONG).show();
                        rootReference.child("users").child(currentUser).child("image").setValue(taskSnapshot.getDownloadUrl());



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();


                        Log.w("SettingActivity",e.getMessage());


                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        progressDialog.dismiss();


                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%..." + taskSnapshot.getBytesTransferred()/1024 +"KB");


                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
*/

      /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if ( requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&  data != null) {
            Uri imageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }

           if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
               CropImage.ActivityResult result = CropImage.getActivityResult(data);


               Toast.makeText(SettingActivity.this,"here we go",Toast.LENGTH_LONG).show();
               if(resultCode == RESULT_OK)
               {

                   final ProgressDialog progressDialog =new ProgressDialog(SettingActivity.this);
                   progressDialog.setTitle("uploading");
                   progressDialog.setCancelable(true);

                   Uri resultUri = result.getUri();

                   StorageReference  childSRef = userProfileStorageReference.child(currentUser + "."+getFileExtension(resultUri));



                   progressDialog.show();

                   childSRef.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                           progressDialog.dismiss();

                           Toast.makeText(SettingActivity.this,"Profile Pic uploaded",Toast.LENGTH_LONG).show();
                           rootReference.child("users").child(currentUser).child("image").setValue(taskSnapshot.getDownloadUrl());



                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {

                           progressDialog.dismiss();


                           Log.w("SettingActivity",e.getMessage());


                       }
                   }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        progressDialog.dismiss();


                           double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                           progressDialog.setMessage("Uploaded " + ((int) progress) + "%..." + taskSnapshot.getBytesTransferred()/1024 +"KB");


                       }
                   });


               }

       }


    }
    */


    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
