package com.createdinam.vidyo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.createdinam.vidyo.model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VarificationActivity extends AppCompatActivity {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor myEdit;
    Button buttonSignIn;
    //These are the objects needed
    private static String mobile;
    //It is the verification id that will be sent to the user
    private String mVerificationId;
    private String mUserID;
    //The edittext to input the code
    private EditText editTextCode;
    // send fcm request
    private static String API_URL = "https://createdinam.com/wp-json/pd/fcm/subscribe/";
    RequestQueue requestQueue;
    //firebase auth object
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varification);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        // setup request
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference("USERS");
        editTextCode = findViewById(R.id.editTextCode);


        //getting mobile number from the previous activity
        //and sending the verification code to the number
        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        sendVerificationCode(mobile);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
//                startActivity(new Intent(VarificationActivity.this,MainActivity.class));
//                finish();
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                // send it to server
                Log.d("Found Token",token);
                setupTokenForNotification(token);
            }
        });
    }

    private void setupTokenForNotification(final String token) {
        final String API_SECRET_KEY = "^SQMht!VMQZ0%v%oMp%23@7XJd";
        String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;
        int version = Build.VERSION.SDK_INT;
        final String versionRelease = Build.VERSION.RELEASE;
        StringRequest tokenRequest = new StringRequest(Request.Method.POST, API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(VarificationActivity.this, "res "+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VarificationActivity.this, "Error "+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("api_secret_key",API_SECRET_KEY);
                param.put("user_email","sonishivam457@gmail.com");
                param.put("device_token",token);
                param.put("subscribed","Letest News");
                param.put("device_name",model);
                param.put("os_version", versionRelease);
                return param;
            }
        };
        requestQueue.add(tokenRequest);
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + mobile, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallbacks);
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VarificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        if (credential.getSmsCode().isEmpty()) {

        } else {
            //signing the user
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VarificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            FirebaseUser user = mAuth.getCurrentUser();
                            mUserID = user.getUid();
                            setDataSaveToServer(mUserID,"private_user",mobile);
                            Log.d("status", "" + task.isSuccessful());
                            myEdit.putBoolean("status", task.isSuccessful());
                            myEdit.putString("user_type", "private_user");
                            myEdit.putString("user_id", mUserID);
                            myEdit.commit();
                            Intent intent = new Intent(VarificationActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            Toast.makeText(VarificationActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setDataSaveToServer(String id,String name,String mob){
        // store user info to server
        UserInfo userInfo = new UserInfo(id, name, mob);
        mReference.child(mUserID).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(VarificationActivity.this, "Save To Server ", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(VarificationActivity.this, "Something Went Wrong....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
