package com.orit.app.whatsapp.Activity;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orit.app.whatsapp.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseUser currentUser;
    private TextView requestAccount;
    private TextInputLayout emaiTL,passwordTL;
    private TextInputEditText emailEditText,passwordEditText;
    private Button login_btn,phoneLogin;
    private FirebaseAuth auth;

    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        auth = FirebaseAuth.getInstance();
      //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        rootRef = FirebaseDatabase.getInstance().getReference();

       // currentUser = auth.getCurrentUser();


        emailEditText = (TextInputEditText)findViewById(R.id.login_email);
        passwordEditText =(TextInputEditText)findViewById(R.id.login_password);


        login_btn    = (Button) findViewById(R.id.login_bnt);
        phoneLogin  =(Button) findViewById(R.id.phone_auth_btn);

        phoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneAuth();
            }
        });
        requestAccount = (TextView)findViewById(R.id.request_account);

        login_btn.setOnClickListener(this);
        requestAccount.setOnClickListener(this);



    }

    /*
    @Override
    protected void onStart()
    {
        super.onStart();

        if( currentUser != null)
            launchHome();
    }

    */

    private void launchHome() {
        Intent homeIntent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(homeIntent);
        this.finish();

    }

    @Override
    public void onClick(View view) {

       if (view.getId() == R.id.login_bnt)
           signInUser();
       if( view.getId() == R.id.forget_password)
           launchForgetPassword();

       if (view.getId() == R.id.request_account)
           launchSignUp();
       if( view.getId() == R.id.phone_auth_btn)
           phoneAuth();
    }


    private void signInUser() {

         if(currentUser != null)
             launchHome();
         else {
             String email = emailEditText.getText().toString().trim();
             String password = passwordEditText.getText().toString().trim();


             if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {


                 auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {

                         if(task.isSuccessful())
                         {

                             Toast.makeText(LoginActivity.this,"Welcome",Toast.LENGTH_LONG).show();
                             launchHome();
                         }
                         else
                         {

                             Log.w(LoginActivity.class.getSimpleName(),task.getException());

                             Toast.makeText(LoginActivity.this,"Sometime going wrong",Toast.LENGTH_LONG).show();
                         }
                     }
                 });

             }




         }
    }

    private void launchForgetPassword()
    {

    }

    private void phoneAuth() {


        startActivity(new Intent(LoginActivity.this,PhoneAuthActivity.class));


    }

    private void launchSignUp() {

        Intent registerIntent = new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(registerIntent);

    }

}
