package com.example.crutch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {
    private HashMap<String,String> getSingleNearbyPlace(JSONObject googlePlacesJSON){
        HashMap<String,String> googlePlaceMap= new HashMap<>();
        String Nameofplace="--NA--";
        String vicinity="--NA--";
        String latitude="";
        String longitude="";
        String reference="";
        try {
            if(!googlePlacesJSON.isNull("name")){
               Nameofplace=googlePlacesJSON.getString("name");
            }
            if(!googlePlacesJSON.isNull("vicinity")){
                vicinity=googlePlacesJSON.getString("vicinity");
            }
            latitude =googlePlacesJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude =googlePlacesJSON.getJSONObject("geometry").getJSONObject("location").getString("lang");

            reference=googlePlacesJSON.getString("reference");

            googlePlaceMap.put("place_name",Nameofplace);
            googlePlaceMap.put("vicinity",vicinity);
            googlePlaceMap.put("lat",latitude);
            googlePlaceMap.put("lang",longitude);
            googlePlaceMap.put("reference",reference);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlaceMap;
    }


    private List<HashMap<String,String>>getAllNearby(JSONArray jsonArray) throws JSONException {
        int conter= jsonArray.length();
        List<HashMap<String,String>> nearbyPlacesList= new ArrayList<>();

        HashMap<String,String>NearbyplaceMap=null;

        for(int i=0;i<conter;i++){
            try {
                NearbyplaceMap = getSingleNearbyPlace((JSONObject) jsonArray.get(i));
                nearbyPlacesList.add(NearbyplaceMap);
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
        return nearbyPlacesList;
    }
    public List<HashMap<String,String>> parse(String jSONdata) throws JSONException {
        JSONArray jsonArray=null;
        JSONObject jsonObject;


        try {
            jsonObject= new JSONObject(jSONdata);
            jsonArray=jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getAllNearby(jsonArray);
    }
}
