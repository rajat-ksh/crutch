package com.example.crutch;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlaces extends AsyncTask<Object,String,String> {

    private String googleplaceData,url;
    private GoogleMap mMap;

    @Override
    protected String doInBackground(Object... objects) {
        mMap=(GoogleMap) objects[0];
        url=(String)objects[1];

        DownloadUrl downloadUrl=new DownloadUrl();
        try{
        googleplaceData=downloadUrl.ReatTheUrl(url);
        }catch (IOException e){
            e.printStackTrace();
        }
        return googleplaceData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>>nearByPlacesList=null;
        DataParser dataParser=new DataParser();
        try {
            nearByPlacesList=dataParser.parse(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DisplayNearbyPlaces(nearByPlacesList);

    }

    private void DisplayNearbyPlaces(List<HashMap<String,String>>nearByPlacesList){
        for(int i=0;i<nearByPlacesList.size();i++){
            MarkerOptions markerOptions= new MarkerOptions();

            HashMap<String,String> googleNearbyPlace=nearByPlacesList.get(i);
            String nameofPlace=googleNearbyPlace.get("place_name");
            String vicinity=googleNearbyPlace.get("vicinity");
            double lat= Double.parseDouble(googleNearbyPlace.get("lat"));
            double lang= Double.parseDouble(googleNearbyPlace.get("lang"));

            LatLng latLng = new LatLng(lat,lang);
            markerOptions.position(latLng);
            markerOptions.title(nameofPlace+":"+vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

            mMap.addMarker(markerOptions);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));


        }
    }
}
