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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Sos extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;
    TextView police, hospital,fire,NEM;
    ImageView imgpol,imghos,imgfire,imgNEM;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        police=findViewById(R.id.police);
        hospital =findViewById(R.id.ambulace);
        fire=findViewById(R.id.fire);
        NEM=findViewById(R.id.emg_contanct);

        imgpol=findViewById(R.id.policeimg);
        imghos=findViewById(R.id.hospimg);
        imgfire=findViewById(R.id.fireimg);
        imgNEM=findViewById(R.id.emg_contanctimg);

        NEM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number="112";
                makePhoneCall();
            }
        });
        imgNEM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number="112";
                makePhoneCall();
            }
        });



        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number="100";
                makePhoneCall();
            }
        });
        imgpol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number="100";
                makePhoneCall();
            }
        });



        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number="101";
                makePhoneCall();
            }
        });
        imgfire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number="101";
                makePhoneCall();
            }
        });



        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number="102";
                makePhoneCall();
            }
        });
        imghos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number="102";
                makePhoneCall();
            }
        });

    }

    private void makePhoneCall() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:"+number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
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
