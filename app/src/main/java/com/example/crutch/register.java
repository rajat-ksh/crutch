package com.example.crutch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {


    EditText user;
    EditText Mob;
    EditText pass;
    Button signup;

    String email,pswd;

    private  FirebaseAuth auth;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

            auth=FirebaseAuth.getInstance();

        Fetch();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 email=user.getText().toString().trim();
                pswd=pass.getText().toString();
                if (email.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pswd.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pswd.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(email,pswd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);
                                if(task.isSuccessful())
                                {
                                    sendUserData();
                                    Toast.makeText(register.this,"Upload complete", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(register.this, mainpage.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                else{

                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }
                        });




            }
        });

    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef=firebaseDatabase.getReference(auth.getUid());
        UserProfile userProfile=new UserProfile(email);
        myRef.child("users").child(auth.getUid()).setValue(userProfile);
    }


    private void Fetch(){
        user=findViewById(R.id.email);
        pass=findViewById(R.id.password);
        signup=findViewById(R.id.Signup);
        progressBar=findViewById(R.id.progressBar);
    }
}

