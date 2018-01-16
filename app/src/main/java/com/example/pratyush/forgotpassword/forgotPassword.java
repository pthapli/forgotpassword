package com.example.pratyush.forgotpassword;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pratyush.forgotpassword.MyDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {

    private Button send;
    private TextView check;
    private EditText mailgetter;
    //String qwerty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        send=findViewById(R.id.sendmail);
        // check=findViewById(R.id.check1);
        mailgetter=findViewById(R.id.getemail);

        // qwerty=mailgetter.getText().toString().trim();
        //s="hello";


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Editable e=mailgetter.getText();
                String string = "";
                if (e != null) string = e.toString().trim();
                //check.setText(string);
               // qwerty="dummyemail";
               // Toast.makeText(getApplicationContext(),"the email is "+ string,Toast.LENGTH_LONG).show();

                  if(Patterns.EMAIL_ADDRESS.matcher(string).matches())
                {

                    FragmentManager manager=getFragmentManager();
                    MyDialog myDialog=new MyDialog();
                    myDialog.show(manager,"dialogbox");


                    FirebaseAuth.getInstance().sendPasswordResetEmail(string).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                               FragmentManager manager1=getFragmentManager();
                                com.example.pratyush.forgotpassword.MyDialog1 myDialog1=new com.example.pratyush.forgotpassword.MyDialog1();
                                myDialog1.show(manager1,"dialogbox");
                                //finish();






                                //Toast.makeText(forgotPassword.this, "password change request has been sent to your mail", Toast.LENGTH_LONG).show();
                                /*finish();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));*/
                            } else {
                                Toast.makeText(forgotPassword.this, "something went wrong please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }

                else {
                    mailgetter.setError("enter correct email");
                    mailgetter.requestFocus();
                    return;
                }

            }

        });

    }

}
