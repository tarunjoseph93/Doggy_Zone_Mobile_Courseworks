package com.example.doggyzone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText userEmail, userPassword;
    Button login_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        userEmail = (EditText) findViewById(R.id.email_edittext);
        userPassword = (EditText) findViewById(R.id.password_edittext);
        login_Button = (Button) findViewById(R.id.login_button);

        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginValidation();
            }
        });

    }

    public void loginValidation() {
        String user_email = userEmail.getText().toString().trim();
        String user_password = userPassword.getText().toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
            Toast.makeText(this, "Invalid Email ID pattern! Check if you have entered your email ID correctly", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(user_password)) {
            Toast.makeText(this, "Password field is empty!", Toast.LENGTH_SHORT).show();
        }
        else {
            login();
        }
    }

    private void login() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }
}