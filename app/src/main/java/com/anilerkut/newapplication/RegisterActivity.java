package com.anilerkut.newapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText registerEmail,registerPassword,registerPasswordAgain;
    Button registerButton;
    TextView alreadyHaveAccountText;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerEmail = findViewById(R.id.emailEditText);
        registerPassword = findViewById(R.id.passwordEditText);
        registerPasswordAgain = findViewById(R.id.passwordAgainEditText);
        registerButton = findViewById(R.id.registerButton);
        alreadyHaveAccountText = findViewById(R.id.alreadyAccountText);

        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                createUser();
            }
        });


        alreadyHaveAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        if (Build.VERSION.SDK_INT >= 21) //status barın rengini değiştirmek için olan kod
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.app_red3)); //status bar or the time bar at the top (see example image1)
        }
    }

    private void createUser()
    {
        String email = registerEmail.getText().toString();
        String password = registerPassword.getText().toString();
        String passwordAgain = registerPasswordAgain.getText().toString();

        if(TextUtils.isEmpty(email)) //emailin boş olup olmama durumuna bakılıyor.
        {
            registerEmail.setError("Email cannot be empty");
            registerEmail.requestFocus();
        }
        if(TextUtils.isEmpty(password)) //emailin boş olup olmama durumuna bakılıyor.
        {
            registerPassword.setError("Password cannot be empty");
            registerPassword.requestFocus();
        }
        if(TextUtils.isEmpty(passwordAgain)) //emailin boş olup olmama durumuna bakılıyor.
        {
            registerPasswordAgain.setError("Password cannot be empty");
            registerPasswordAgain.requestFocus();
        }
        else if(password.equalsIgnoreCase(passwordAgain)) //passwordların eşit olup olmama durumuna bakılıyor.
        {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(RegisterActivity.this,"User registered succesfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this,"Registiration failed: " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            registerPassword.setError("Passwords must be same");
            registerPasswordAgain.setError("Passwords must be same");
            registerPassword.requestFocus();
            registerPasswordAgain.requestFocus();
            Toast.makeText(this, "Unmatch Passwords", Toast.LENGTH_LONG).show();
        }
    }
}