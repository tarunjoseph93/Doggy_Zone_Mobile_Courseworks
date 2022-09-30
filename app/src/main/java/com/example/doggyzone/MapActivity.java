package com.example.doggyzone;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.doggyzone.map_functions.GetData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_FINE_LOCATION_CODE = 100;
    Button searchButton;
    Spinner searchSpinner;
    SupportMapFragment mapFragment;
    GoogleMap gMap;
    FusedLocationProviderClient fLPC;
    double currentLatitude = 0, currentLongitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        fLPC = LocationServices.getFusedLocationProviderClient(this.getApplicationContext());

        searchButton = findViewById(R.id.maps_search_button);
        searchSpinner = findViewById(R.id.maps_spinner);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        BottomNavigationView btmNavView = findViewById(R.id.bottom_navbar);
        btmNavView.setSelectedItemId(R.id.menu_map);

        String[] nameList = {"Vet Clinics", "Dog Stores", "Dog-Friendly Cafes", "Dog-Friendly Restaurants", "Dog-Friendly Bars"};

        searchSpinner.setAdapter(new ArrayAdapter<>(
                getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, nameList));


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gMap.clear();
                getCurrentLocation();

                int a = searchSpinner.getSelectedItemPosition();
                if(Objects.equals(nameList[a], "Vet Clinics")) {

                    StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                    sb.append("location=" + currentLatitude + "," + currentLongitude);
                    sb.append("&radius=3000");
                    sb.append("&type=veterinary_care");
                    sb.append("&sensor=true");
                    sb.append("&key=" + getResources().getString(R.string.google_maps_api_key));

                    String vetUrl = sb.toString();
                    Object getData[] = new Object[2];
                    getData[0] = gMap;
                    getData[1] = vetUrl;

                    GetData fetchData = new GetData();
                    fetchData.execute(getData);
                }
                else {
                    if(Objects.equals(nameList[a], "Dog-Friendly Cafes")) {
                        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                        sb.append("keyword=dog");
                        sb.append("&location=" + currentLatitude + "," + currentLongitude);
                        sb.append("&radius=3000");
                        sb.append("&type=cafe");
                        sb.append("&sensor=true");
                        sb.append("&key=" + getResources().getString(R.string.google_maps_api_key));

                        String cafeURL = sb.toString();
                        Object getData[] = new Object[2];
                        getData[0] = gMap;
                        getData[1] = cafeURL;

                        GetData fetchData = new GetData();
                        fetchData.execute(getData);
                    }
                    else if(Objects.equals(nameList[a], "Dog Stores")) {

                        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                        sb.append("keyword=dog");
                        sb.append("&location=" + currentLatitude + "," + currentLongitude);
                        sb.append("&radius=3000");
                        sb.append("&type=store");
                        sb.append("&sensor=true");
                        sb.append("&key=" + getResources().getString(R.string.google_maps_api_key));

                        String storeURL = sb.toString();
                        Object getData[] = new Object[2];
                        getData[0] = gMap;
                        getData[1] = storeURL;

                        GetData fetchData = new GetData();
                        fetchData.execute(getData);
                    }
                    else if(Objects.equals(nameList[a], "Dog-Friendly Restaurants")) {

                        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                        sb.append("keyword=dog");
                        sb.append("&location=" + currentLatitude + "," + currentLongitude);
                        sb.append("&radius=3000");
                        sb.append("&type=restaurant");
                        sb.append("&sensor=true");
                        sb.append("&key=" + getResources().getString(R.string.google_maps_api_key));

                        String restaurantURL = sb.toString();
                        Object getData[] = new Object[2];
                        getData[0] = gMap;
                        getData[1] = restaurantURL;

                        GetData fetchData = new GetData();
                        fetchData.execute(getData);
                    }
                    else if(Objects.equals(nameList[a], "Dog-Friendly Bars")) {

                        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                        sb.append("keyword=dog");
                        sb.append("&location=" + currentLatitude + "," + currentLongitude);
                        sb.append("&radius=3000");
                        sb.append("&type=bar");
                        sb.append("&sensor=true");
                        sb.append("&key=" + getResources().getString(R.string.google_maps_api_key));

                        String barURL = sb.toString();
                        Object getData[] = new Object[2];
                        getData[0] = gMap;
                        getData[1] = barURL;

                        GetData fetchData = new GetData();
                        fetchData.execute(getData);
                    }
                }
            }
        });

        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.menu_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_map:
                        return true;
                    case R.id.menu_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        getCurrentLocation();

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(PERMISSIONS_FINE_LOCATION_CODE) {
            case PERMISSIONS_FINE_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
        }
    }

    private void getCurrentLocation() {
        if(ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION_CODE);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Toast.makeText(getApplicationContext(), "Location Result: " + locationResult, Toast.LENGTH_SHORT).show();

                if(locationResult == null) {
                    Toast.makeText(getApplicationContext(), "Location Result is Null!", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(Location location:locationResult.getLocations()) {
                    if(location != null) {
                        Toast.makeText(getApplicationContext(), "Current Latitude: " + location.getLatitude() +
                                "\nCurrent Longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        fLPC.requestLocationUpdates(locationRequest, locationCallback, null);

        Task<Location> locationTask = fLPC.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLatitude = location.getLatitude();
                    currentLongitude = location.getLongitude();
                    LatLng latLng = new LatLng(currentLatitude, currentLongitude);
                    gMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                    gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                }
            }
        });
    }
}