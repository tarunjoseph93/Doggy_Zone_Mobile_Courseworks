package com.example.doggyzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText userEmail, userPassword;
    Button login_Button;
    TextView registerUser;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String user_email = "", user_password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        userEmail = findViewById(R.id.email_edittext);
        userPassword = findViewById(R.id.password_edittext);
        login_Button = findViewById(R.id.login_button);
        registerUser = findViewById(R.id.register_text_view);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginValidation();
            }
        });

    }

    public void loginValidation() {
        user_email  = userEmail.getText().toString().trim();
        user_password  = userPassword.getText().toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
            userEmail.setError("Invalid Email ID pattern! Check if you have entered your email ID correctly");
        }
        else if (TextUtils.isEmpty(user_password)) {
            userPassword.setError("Password field is empty!");
        }
        else {
            login();
        }
    }

    private void login() {
        progressDialog.setMessage("Logging you into your account!");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(user_email,user_password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}