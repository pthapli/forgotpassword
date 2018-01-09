package com.example.pratyush.forgotpassword;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class signUpActivity extends AppCompatActivity {

    EditText editTextemail,editTextpassword;
    Button signup;
    TextView textView;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextemail = findViewById(R.id.email_);
        editTextpassword = findViewById(R.id.password_);
        progressBar = findViewById(R.id.progressbar);
        textView = findViewById(R.id.login_);
        signup = findViewById(R.id.signup_);

        mAuth = FirebaseAuth.getInstance();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }


    private void registerUser()
    {

        String email = editTextemail.getText().toString().trim();
        String password = editTextpassword.getText().toString();

        if(email.isEmpty())
        {
            editTextemail.setError("email is needed");
            editTextemail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextemail.setError("please enter correct email");
            editTextemail.requestFocus();
        }

        if(password.isEmpty())
        {
            editTextpassword.setError("password is needed");
            editTextpassword.requestFocus();
            return;
        }

        if(password.length()<6)
        {
            editTextpassword.setError("minimum password length is 6");
            editTextpassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);

                if(task.isSuccessful()){
                    finish();
                    Toast.makeText(getApplicationContext(),"user registered",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(signUpActivity.this,ProfileActivity.class));
                }
                else {

                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            Toast.makeText(getApplicationContext(), "user already registered", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


}
