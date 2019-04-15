package com.example.crutch;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.List;
import java.util.Locale;

//import android.media.MediaPlayer;


public class mainpage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    double lt,lg;
    private static final int REQUEST_SMS = 1;
    String ad;

    TextView locationText;

    LocationManager locationManager;
    int counter = 1;
    String number, sms;
    private static final int REQUEST_CAMERA = 1;
    private Camera camera;
    private boolean isFlashOn;
    MediaPlayer mp;
    private boolean hasFlash;

    Camera.Parameters params;
    // MediaPlayer mp;

    ImageButton SOSimgbtn;
    ImageView sos, maps, nearby, emgcall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        turnOffFlash();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationText = findViewById(R.id.locationText);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //calling map activity
        maps = findViewById(R.id.map);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mainpage.this, Mapsactivity.class);
                startActivity(i);
            }
        });

        //calling sos calls
        sos = findViewById(R.id.soscalls);
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mainpage.this, Sos.class);
                startActivity(i);
            }
        });

        //calling emergency calls
        emgcall = findViewById(R.id.calls);
        emgcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent link = new Intent(Intent.ACTION_VIEW, Uri.parse("http://indianhelpline.com/"));
                startActivity(link);
            }
        });


        //callig sos button
        SOSimgbtn = findViewById(R.id.sos);

        //calling nearby
        nearby=findViewById(R.id.nearby);
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mainpage.this,Mapsactivity.class);
                startActivity(i);
            }
        });


        // First check if device is supporting flashlight or not

        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(mainpage.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                }
            });
            alert.show();
            return;
        }

        // get the camera
        getCamera();

        // displaying button image
        toggleButtonImage();

        SOSimgbtn.setOnClickListener(new View.OnClickListener() {

                                         @Override
                                         public void onClick(View v) {

                                             if (ContextCompat.checkSelfPermission(mainpage.this,
                                                     Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                                 ActivityCompat.requestPermissions(mainpage.this,
                                                         new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                                             } else if (isFlashOn) {
                                                 // turn off flash
                                                 turnOffFlash();
                                             } else {
                                                 // turn on flash
                                                 turnOnFlash();
                                                 getLocation();

                                             }

                                         }


                                     }
        );
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    private void sendsms() {


        //1st persons messages
        //help me as soon as possible
        number = "7985034370";
        try {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);

            }
            String sms="Help me Please as soon as possible: my Location:";
            if (number.length() == 10) {
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(number, null,sms, null, null);
                Toast.makeText(mainpage.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mainpage.this, "please enter a valid number", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(mainpage.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
        //latitdue longitude for 1st person
        try {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);

            }
            if (number.length() == 10) {
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(number, null,ad, null, null);
                Toast.makeText(mainpage.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mainpage.this, "please enter a valid number", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(mainpage.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }





        //2nd person
        //help me msg
        String number1 = "7733021684";
        try {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);

            }

             sms="Help me Please as soon as possible: my Location:";
            if (number.length() == 10) {
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(number1, null,sms, null, null);
                Toast.makeText(mainpage.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mainpage.this, "please enter a valid number", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(mainpage.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
        //latitide and longitude
        try {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);

            }

            if (number.length() == 10) {
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(number1, null,ad, null, null);
                Toast.makeText(mainpage.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mainpage.this, "please enter a valid number", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(mainpage.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }


        playSound();
    }

    // Get the camera
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Failed to Open. Error: ", e.getMessage());
            }
        }
    }

    // Turning On flash
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound
            //playSound();

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

            // changing button/switch image
            toggleButtonImage();
           ;
        }

    }


    // Turning Off flash
    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound
            //playSound();

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

            counter = 0;

            // changing button/switch image
            toggleButtonImage();
        }
    }


    private void toggleButtonImage() {
        if (isFlashOn) {
            SOSimgbtn.setImageResource(R.mipmap.sos2);
        } else {
            SOSimgbtn.setImageResource(R.mipmap.sos1);
        }
    }

    private void playSound(){
        if(isFlashOn){
            mp = MediaPlayer.create(this, R.raw.danger);
        }else{


        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }
        });
        mp.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // on pause turn off the flash
        turnOffFlash();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // on resume turn on the flash
        if (hasFlash)
            turnOffFlash();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // on starting the app get the camera params
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // on stop release the camera
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainpage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
        } else if (id == R.id.emg_contanct) {
            Intent i = new Intent(this, emg_contact.class);
            startActivity(i);

        } else if (id == R.id.reqloc) {
            Intent i = new Intent(this, reqloc.class);
            startActivity(i);

        } else if (id == R.id.setting) {

        } else if (id == R.id.logout) {

            Intent i = new Intent(this, login.class);

            startActivity(i);

        } else if (id == R.id.about) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCamera();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lt=location.getLatitude();
        lg=location.getLongitude();

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude() + "\n"+addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));

            ad=locationText.getText().toString();


        }catch(Exception e)
        {

        }

        sendsms();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}