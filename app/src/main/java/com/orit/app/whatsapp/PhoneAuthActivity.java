package com.orit.app.whatsapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


public class PhoneAuthActivity extends AppCompatActivity {

    private String mVerificationId;

    EditText phoneNoInput;
    Button   sendCodeBtn;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private FirebaseAuth auth;
    ProgressDialog waitDlg;
    String TAG = PhoneAuthActivity.class.getSimpleName();
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);


        auth = FirebaseAuth.getInstance();



        phoneNoInput =(EditText)findViewById(R.id.phone_no);
        sendCodeBtn =(Button) findViewById(R.id.send_code_btn);


        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                waitDlg.dismiss();
                signInWithPhoneCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                waitDlg.dismiss();
                Toast.makeText(PhoneAuthActivity.this,"Verification Failed",Toast.LENGTH_LONG).show();
                phoneNoInput.setVisibility(View.VISIBLE);
                sendCodeBtn.setVisibility(View.VISIBLE);



            }
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                waitDlg.dismiss();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                phoneNoInput.setVisibility(View.GONE);
                sendCodeBtn.setVisibility(View.GONE);


                Toast.makeText(PhoneAuthActivity.this,"code has been sent",Toast.LENGTH_LONG).show();


                // ...
            }
        };




        sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                String phoneno=phoneNoInput.getText().toString().trim();



                //Patterns.PHONE.matcher(phoneno).matches()
                if (!TextUtils.isEmpty(phoneno)  ) {
                    waitDlg = new ProgressDialog(PhoneAuthActivity.this);
                    waitDlg.setCanceledOnTouchOutside(true);
                    waitDlg.setTitle("Verifing...");
                    waitDlg.setMessage("Please wait while verifying your phone no with country code");
                    waitDlg.show();
                  //
                    //  phoneAuth(phoneno);

                    verifyPhone(phoneno,mCallback);
                }

                else{

                    Toast.makeText(PhoneAuthActivity.this,"your phone number please",Toast.LENGTH_LONG).show();}





            }
        });

    }

    public void verifyPhone(String phoneNumber, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallback
    }

    private void phoneAuth(String phoneno)
    {

        Toast.makeText(PhoneAuthActivity.this,phoneno,Toast.LENGTH_LONG).show();
        Log.w("TAG Me","This is nphone nuber"+phoneno);



        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                waitDlg.dismiss();
                signInWithPhoneCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                waitDlg.dismiss();
                Toast.makeText(PhoneAuthActivity.this,"Verification Failed",Toast.LENGTH_LONG).show();
                phoneNoInput.setVisibility(View.VISIBLE);
                sendCodeBtn.setVisibility(View.VISIBLE);



            }
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                waitDlg.dismiss();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                phoneNoInput.setVisibility(View.GONE);
                sendCodeBtn.setVisibility(View.GONE);

                Toast.makeText(PhoneAuthActivity.this,"code has been sent",Toast.LENGTH_LONG).show();


                // ...
            }
        };
    }

    private void signInWithPhoneCredential(final PhoneAuthCredential credential)
    {


        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    waitDlg.dismiss();
                    Toast.makeText(PhoneAuthActivity.this,"Welcome ",Toast.LENGTH_LONG).show();

                    Intent mainIntent = new Intent(PhoneAuthActivity.this,MainActivity.class);
                    startActivity(mainIntent);
                    finish();


                }
                else
                {

                    Log.w("PhoneAuth",task.getException().toString());
                    Toast.makeText(PhoneAuthActivity.this,task.getException().toString(),Toast.LENGTH_LONG).show();


                }
            }
        });
    }
}
