package com.example.crutch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity {


    EditText username;
    EditText pass;
    Button btn;
   TextView create,emergency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                String user;
                String Password;
                user=username.getText().toString();
                Password=pass.getText().toString();

                if(user.length()==10){
                    if(user.matches("[A-Za-z]+")){
                        Toast.makeText(getApplicationContext(),"Phone No. should contain only number",Toast.LENGTH_LONG).show();
                }
                else
                        {
                            if(user.equals("9896751225") && Password.equals("12345")){
                            Intent i =new Intent(login.this,mainpage.class);
                            startActivity(i);

                        }else{
                            Toast.makeText(getApplicationContext(),"Login fail",Toast.LENGTH_SHORT).show();

                        }
                        }

                    }else {
                    Toast.makeText(getApplicationContext(),"Enter a Valid Number",Toast.LENGTH_LONG).show();
                }

            }
        });


    }



    private void Fetch(){
        username=findViewById(R.id.mobile);
        pass=findViewById(R.id.password);
        btn=findViewById(R.id.btnlogin);
        create=findViewById(R.id.create);
        emergency=findViewById(R.id.emg);
   }
}

