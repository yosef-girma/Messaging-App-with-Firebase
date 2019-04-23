package com.orit.app.whatsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView haveAccount;
    private TextInputLayout emaiTL,passwordTL;
    private TextInputEditText emailEditText,passwordEditText;
    private Button signUpBtn;
    private FirebaseAuth auth;
    private ProgressDialog progresDialog;
    private DatabaseReference rootRef;
    private String TAG = SignUpActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);




        auth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        emailEditText = (TextInputEditText)findViewById(R.id.register_email);
        passwordEditText =(TextInputEditText)findViewById(R.id.register_password);


        signUpBtn    = (Button) findViewById(R.id.sign_up_btn);
        haveAccount = (TextView)findViewById(R.id.have_account);

        signUpBtn.setOnClickListener(this);
        haveAccount.setOnClickListener(this);

        progresDialog = new ProgressDialog(this);
        progresDialog.setTitle("creating new account");
        progresDialog.setMessage("Please wait a moment while creating your account");
        progresDialog.setCanceledOnTouchOutside(true);

    }

    @Override
    public void onClick(View view)
    {

        if (view.getId() == R.id.sign_up_btn)
        {
            createUser();
        }

        if(view.getId() == R.id.have_account)
            launchSignIn();
    }

    private void launchSignIn() {

    Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
    startActivity(intent);

    }

    private void createUser() {

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();


        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

           progresDialog.show();
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful())
                    {

                        String userId = auth.getCurrentUser().getUid();
                        rootRef.child("users").child(userId).setValue("");

                        progresDialog.dismiss();
                        Toast.makeText(SignUpActivity.this,"Welcome",Toast.LENGTH_LONG).show();
                        launchHome();
                    }
                    else
                    {

                        progresDialog.dismiss();
                        Toast.makeText(SignUpActivity.this,"Sometime going wrong",Toast.LENGTH_LONG).show();
                        Log.w(TAG,task.getException());
                    }
                }
            });

        }



    }

    private void launchHome() {
        Intent homeIntent = new Intent(SignUpActivity.this,MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();

    }

}
