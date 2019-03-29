package com.example.crutch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class register extends AppCompatActivity {


    EditText user;
    EditText Mob;
    EditText pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Fetch();

    }
    public void profile(View v){

        String username;
        String mob;
        username=user.getText().toString();
        mob=Mob.getText().toString();
        Intent i=new Intent(this,profile.class);

        if(username.equals("")||mob.equals("")) {
            Toast.makeText(getApplicationContext(),"no empty",Toast.LENGTH_SHORT).show();
        }else{
        i.putExtra("Username",username);
        i.putExtra("Mobile",mob);
        startActivity(i);}
        }


    private void Fetch(){
        user=findViewById(R.id.name);
        Mob=findViewById(R.id.phone);
        pass=findViewById(R.id.password);
    }
}

