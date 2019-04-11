package com.example.crutch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.textclassifier.TextClassification;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class emg_contact extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;
    Button btn1,btn2,btn3,btn4,btn5;
    EditText ed1,ed2,ed3,ed4,ed5;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emg_contact);
        btn1 = findViewById(R.id.call1);
        btn2 = findViewById(R.id.call2);
        btn3 = findViewById(R.id.call3);
        btn4 = findViewById(R.id.call4);
        btn5 = findViewById(R.id.call5);


        ed1 = findViewById(R.id.num1);
        ed2 = findViewById(R.id.num2);
        ed3 = findViewById(R.id.num3);
        ed4 = findViewById(R.id.num4);
        ed5 = findViewById(R.id.num5);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = ed1.getText().toString();
                makePhoneCall();

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = ed2.getText().toString();
                makePhoneCall();

            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = ed3.getText().toString();
                makePhoneCall();

            }
        });


        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = ed4.getText().toString();
                makePhoneCall();

            }
        });


        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = ed5.getText().toString();
                makePhoneCall();

            }
        });


    }
    private void makePhoneCall() {

        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else if(number.length()==10){
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }else{
                Toast.makeText(this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
