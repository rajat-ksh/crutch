package com.example.crutch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class Mapsactivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location mylocation;
    private Marker CurrentLocationMarker;
    private static final int Request_User_Location_code=99;
    private double latitude,longitude;
    private int proximityRadius=10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapsactivity);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            checkUserLocaionPermisson();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){

            buildGoogleApiClient();

            mMap.setMyLocationEnabled(true);
        }


    }



    public void onClick(View v){

        String hospital="HOSPITAL",police="POLICE",fire="FIRE";
        Object transferData[]=new Object[2];
        GetNearbyPlaces getNearbyPlaces=new GetNearbyPlaces();


       switch (v.getId())
       {
           case R.id.search_btn:
               EditText address_field=findViewById(R.id.search);
               String addtrss=address_field.getText().toString();

               List<Address> addressList=null;
               MarkerOptions usermarkerOptions= new MarkerOptions();
           if(!TextUtils.isEmpty(addtrss)){
               Geocoder geocoder=new Geocoder(this);
               try {
                   addressList =geocoder.getFromLocationName(addtrss,6);
                   if(addressList!=null){
                       for (int i=0;i<addressList.size();i++){
                           Address userAddress=addressList.get(i);
                           LatLng latLng = new LatLng(userAddress.getLatitude(),userAddress.getLongitude());
                           usermarkerOptions.position(latLng);
                           usermarkerOptions.title(addtrss);
                           usermarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                           mMap.addMarker(usermarkerOptions);

                           mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                           mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                       }
                   }else{
                       Toast.makeText(this,"No location found",Toast.LENGTH_SHORT).show();
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }


           }
           break;

           case R.id.hospital:
               mMap.clear();
               String url= getUrl(latitude,longitude,hospital);
               transferData[0]=mMap;
               transferData[1]=url;

               getNearbyPlaces.execute(transferData);
               Toast.makeText(this,"saeching for nearby hospital",Toast.LENGTH_LONG).show();
               Toast.makeText(this,"Showing nearby hospital",Toast.LENGTH_LONG).show();

               break;
           case R.id.police:
               mMap.clear();
                url= getUrl(latitude,longitude,police);
               transferData[0]=mMap;
               transferData[1]=url;

               getNearbyPlaces.execute(transferData);
               Toast.makeText(this,"saeching for nearby police",Toast.LENGTH_LONG).show();
               Toast.makeText(this,"Showing nearby Police Station",Toast.LENGTH_LONG).show();

               break;

           case R.id.fire:
               mMap.clear();
                url= getUrl(latitude,longitude,fire);
               transferData[0]=mMap;
               transferData[1]=url;

               getNearbyPlaces.execute(transferData);
               Toast.makeText(this,"saeching for nearby fire",Toast.LENGTH_LONG).show();
               Toast.makeText(this,"Showing nearby Fire Station",Toast.LENGTH_LONG).show();

               break;
       }

    }

    private String getUrl(double latitude, double longitude, String nearbyplaces){
      StringBuilder googleURL=new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location="+latitude+","+longitude);
        googleURL.append("&radius"+proximityRadius);
        googleURL.append("&type"+nearbyplaces);
        googleURL.append("&sensor=true");
        googleURL.append("&key"+"AIzaSyA8SM0ys5qNaDuHK-Yev4JEmIGbjySFO-w");


        Log.d("MapsActivity","url="+googleURL.toString());
        return googleURL.toString();
    }

    public  boolean checkUserLocaionPermisson(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){

                ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_code);

            }else {

                ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_code);

            }
            return false;

        }else{

            return true;}
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case Request_User_Location_code:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                        if(googleApiClient==null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }else {
                    Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show();
                }
        }
    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }
    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();

        mylocation=location;
        if(CurrentLocationMarker!=null){
            CurrentLocationMarker.remove();
        }
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        CurrentLocationMarker=mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if(googleApiClient!=null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
