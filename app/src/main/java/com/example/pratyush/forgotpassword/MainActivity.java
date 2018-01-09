package com.example.pratyush.forgotpassword;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class MainActivity extends AppCompatActivity {

    private Button signUp,logIn;
    private TextView forgotPassword;
    private EditText editTextemail,editTextpassword;//take username as email for now
    private FirebaseAuth mAuth;
    ProgressBar pbarmain;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextemail=findViewById(R.id.username);
        editTextpassword=findViewById(R.id.password);
        signUp=findViewById(R.id.signup);
        logIn=findViewById(R.id.login);
        forgotPassword=findViewById(R.id.forgot_password);
        mAuth=FirebaseAuth.getInstance();
        pbarmain=findViewById(R.id.pbarmain);
        //myToolbar=findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perform a new signup here
                finish();
                startActivity(new Intent(getApplicationContext(),signUpActivity.class));
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pbarmain.setVisibility(View.VISIBLE);
                userLogin();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perform forgot Password options here
                Intent intent=new Intent(getApplicationContext(),forgotPassword.class);
                startActivity(intent);
            }
        });

    }

    private void userLogin()
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

        pbarmain.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                pbarmain.setVisibility(View.GONE);
                if(task.isSuccessful())
                {

                    finish();
                    Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                else
                {
                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                pbarmain.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(this,ProfileActivity.class));
        }
    }
}
