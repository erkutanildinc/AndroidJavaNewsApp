package com.anilerkut.newapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText loginEmail,loginPassword;
    Button loginButton,googleButton;
    TextView dontHaveAccountText;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.emailLoginEditText);
        loginPassword = findViewById(R.id.passwordLoginEditText);
        loginButton = findViewById(R.id.loginButton);
        dontHaveAccountText = findViewById(R.id.dontHaveAccountText);
        googleButton = findViewById(R.id.googleSignInButton);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        googleButton.setOnClickListener(new View.OnClickListener() { //google butonuna tıklandığında olucaklar
            @Override
            public void onClick(View view)
            {
                signInWithGoogle();
            }
        });

        dontHaveAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginOperations();
            }
        });

        if (Build.VERSION.SDK_INT >= 21) //status barın rengini değiştirmek için olan kod
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.app_red3)); //status bar or the time bar at the top (see example image1)
        }
    }


    private void loginOperations() //email ve password ile olan login operasyonları
    {
        String userEmail = loginEmail.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        if(TextUtils.isEmpty(userEmail))
        {
            loginEmail.setError("Email cannot be empty");
            loginEmail.requestFocus();
        }
        if(TextUtils.isEmpty(userPassword))
        {
            loginPassword.setError("Password cannot be empty");
            loginPassword.requestFocus();
        }
        else
        {
            mAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(LoginActivity.this,"User logged in succesfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"Login failed: " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void signInWithGoogle()
    {
        Intent signInWithGoogleIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInWithGoogleIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult( Task<GoogleSignInAccount> result)
    {
        try {
            GoogleSignInAccount acc = result.getResult(ApiException.class);
            Toast.makeText(this,"Sign In Succesfully",Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e)
        {
            Toast.makeText(this,"Sign In Failed",Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct)
    {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this,"User logged in succesfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Login failed:" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}