package com.example.pratyush.forgotpassword;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101 ;
    ImageView imageView;
    EditText editTextProfile;
    Button buttonsend,signout;
    ProgressBar pbar;
    String displayName;
    Uri uriProfileImage;
    String profileImageUrl;
    FirebaseAuth mAuth;
    TextView tview,textViewProfileCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth=FirebaseAuth.getInstance();

        signout=findViewById(R.id.buttonsignout);
        tview=findViewById(R.id.textviewverified);
        editTextProfile=findViewById(R.id.edittextprofile);
        mAuth=FirebaseAuth.getInstance();
        buttonsend=findViewById(R.id.buttonsend);
        textViewProfileCheck=findViewById(R.id.textViewcheckprofile);
        imageView=findViewById(R.id.profileimage);
        pbar=findViewById(R.id.progressbarprofile);


        //email verification:
        final FirebaseUser user=mAuth.getCurrentUser();

        if(user != null)
             textViewProfileCheck.setText(user.getDisplayName());

        if(user.isEmailVerified())
        {
            tview.setText("email is verified");
        }
        else
        {
            verifyUser(user);
        }

        //signout:
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });



        buttonsend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //sendName(user);
                saveUserInformation();
            }
        });








        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

       loadUserInformation();


      /*  buttonsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // saveUserInformation();
                tview.setText("profile activity started");
            }
        });
*/

    }

    private void verifyUser(final FirebaseUser user)
    {
        tview.setText("email not verified.Click to verify");
        tview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Toast.makeText(getApplicationContext(),"verification link has been sent to your email",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(),"something went wrong please try again later",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void sendName(FirebaseUser user)
    {
        displayName=editTextProfile.getText().toString();
        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName).build();

        user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(ProfileActivity.this,"profile updated",Toast.LENGTH_SHORT).show();
            }
        });
    }






    private void loadUserInformation() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {

            if (user.getPhotoUrl() != null)
            {
                Glide.with(this).load(user.getPhotoUrl().toString()).into(imageView);
            }
            if (user.getDisplayName() != null) {
                editTextProfile.setText(user.getDisplayName());
            }
        }
    }

    private void saveUserInformation() {

        String displayName=editTextProfile.getText().toString();

        if(displayName.isEmpty())
        {
            editTextProfile.setError("please enter name");
            editTextProfile.requestFocus();
            return;
        }

        FirebaseUser user=mAuth.getCurrentUser();


        if(user!=null && profileImageUrl!=null)
        {

            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                        Toast.makeText(ProfileActivity.this,"profile updated",Toast.LENGTH_SHORT).show();
                }
            });

        }

         if(user!=null)
         {
             UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                     .setDisplayName(displayName).build();

             user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                     if(task.isSuccessful())
                         Toast.makeText(ProfileActivity.this,"profile updated",Toast.LENGTH_SHORT).show();
                 }
             });
         }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
           uriProfileImage = data.getData();
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);
                imageView.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private void uploadImageToFirebaseStorage() {

        StorageReference profileImageRef= FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");

        if(uriProfileImage!=null)
        {
            pbar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                     pbar.setVisibility(View.GONE);

                     profileImageUrl=taskSnapshot.getDownloadUrl().toString();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT);
                }
            });
        }
    }

    private void showImageChooser()
    {
      Intent intent=new Intent();
      intent.setType("image");
      intent.setAction(Intent.ACTION_GET_CONTENT);
      startActivityForResult(Intent.createChooser(intent,"select profile image"),CHOOSE_IMAGE);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }







}
