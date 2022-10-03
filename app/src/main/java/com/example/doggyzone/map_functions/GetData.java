package com.example.doggyzone.map_functions;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.doggyzone.MapActivity;
import com.example.doggyzone.MarkerPageActivity;
import com.example.doggyzone.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GetData extends AsyncTask<Object, String, String> {

    String nearbyData;
    GoogleMap gMap;
    String url;

    @Override
    protected String doInBackground(Object... objects) {

        try {
            gMap = (GoogleMap) objects[0];
            url = (String) objects[1];
            DownloadURL downloadURL = new DownloadURL();
            nearbyData = downloadURL.downloadURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nearbyData;
    }

    @Override
    protected void onPostExecute(String s) {
//        gMap.clear();
        try {

            JSONObject jObj = new JSONObject(s);
            JSONArray jArray = jObj.getJSONArray("results");

            for(int i = 0; i < jArray.length(); i++) {
                JSONObject newJObj = jArray.getJSONObject(i);
                JSONObject getLocation = newJObj.getJSONObject("geometry").getJSONObject("location");

                String lat = getLocation.getString("lat");
                String lng = getLocation.getString("lng");

                JSONObject fetchName = jArray.getJSONObject(i);
                String name = fetchName.getString("name");

                JSONObject newJObj1 = jArray.getJSONObject(i);
                JSONObject getOpeningHourObj = newJObj1.getJSONObject("opening_hours");

                String open = Boolean.toString(getOpeningHourObj.getBoolean("open_now"));

                JSONObject getAddress = jArray.getJSONObject(i);
                String address = getAddress.getString("vicinity");

                JSONObject getRating = jArray.getJSONObject(i);
                String rating = getRating.getString("rating");

                JSONObject getUserRatings = jArray.getJSONObject(i);
                String userRatings = getUserRatings.getString("user_ratings_total");

                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                MarkerOptions mOpts = new MarkerOptions();
                mOpts.title(name);
                mOpts.position(latLng);
                mOpts.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                gMap.addMarker(mOpts);
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

//                gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                    @Override
//                    public boolean onMarkerClick(@NonNull Marker marker) {
//
//
//                        return true;
//                    }
//                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
