package com.example.crutch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity {

    EditText username;
    EditText pass;
    Button btn;
   TextView create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Fetch();
        helper help=new helper(this);
        SQLiteDatabase database=help.getReadableDatabase();
        Cursor cursor =database.rawQuery("Select mob_no from users where id=?",new String[]{"1"});
        if(cursor != null){
            cursor.moveToFirst();
            cursor.getString(0);
        }
             create.setOnClickListener(new View.OnClickListener(){
                 @Override
                 public void onClick(View v) {
                     Intent i =new Intent(login.this,register.class);
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

                if(user.equals("12345") && Password.equals("12345")){
                    Intent i =new Intent(login.this,mainpage.class);
                    startActivity(i);

                }else{
                    Toast.makeText(getApplicationContext(),"Logi fail",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void Fetch(){
        username=findViewById(R.id.mobile);
        pass=findViewById(R.id.password);
        btn=findViewById(R.id.btnlogin);
        create=findViewById(R.id.create);
   }
}

