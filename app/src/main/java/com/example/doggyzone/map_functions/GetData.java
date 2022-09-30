package com.example.doggyzone.map_functions;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
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

                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                MarkerOptions mOpts = new MarkerOptions();
                mOpts.title(name);
                mOpts.position(latLng);
                gMap.addMarker(mOpts);
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
