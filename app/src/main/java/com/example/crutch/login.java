package com.example.crutch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    EditText username;
    EditText pass;
    Button btn;
   TextView create,emergency,FR;
   ProgressBar progressBar;

   String Email,Password;

   private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(this,mainpage.class));
            finish();
        }


        setContentView(R.layout.activity_login);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Fetch();

             create.setOnClickListener(new View.OnClickListener(){
                 @Override
                 public void onClick(View v) {
                     Intent i =new Intent(login.this,register.class);
                     startActivity(i);
                 }
             });

             emergency.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent i=new Intent(login.this,Sos.class);
                     startActivity(i);
                 }
             });



             btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userLogin();
                }
            });

             FR.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     resetPassword();
                 }
             });




    }

    private void setSupportActionBar(Toolbar toolbar) {
    }
    ////////////////////////////////////////////////////////////
        //For login
    private void userLogin(){

        Email=username.getText().toString().trim();
        Password=pass.getText().toString().trim();

        if (Email.isEmpty()) {
            username.setError("Email is required");
            username.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            username.setError("Please enter a valid email");
            username.requestFocus();
            return;
        }

        if (Password.isEmpty()) {
            pass.setError("Password is required");
            pass.requestFocus();
            return;
        }

        if (Password.length() < 6) {
            pass.setError("Minimum lenght of password should be 6");
            pass.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent i= new Intent(login.this,mainpage.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else
                {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    ////////////////////////////////////////////
    //For reseting password
    public void resetPassword(){
        Email=username.getText().toString().trim();
        if(Email.equals("")){
            username.setError("Email is required");
            username.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.sendPasswordResetEmail(Email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(login.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(login.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });


    }



    private void Fetch(){
        username=findViewById(R.id.email);
        pass=findViewById(R.id.pswd);
        FR=findViewById(R.id.reset);
        btn=findViewById(R.id.btnlogin);
        create=findViewById(R.id.create);
        emergency=findViewById(R.id.emg);
        progressBar=findViewById(R.id.progressBar);
   }
}

