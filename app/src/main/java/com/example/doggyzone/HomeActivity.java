package com.example.doggyzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doggyzone.adapters.EventAdapter;
import com.example.doggyzone.functions.ImageLoadASyncTask;
import com.example.doggyzone.models.EventModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    Button addEventButton;
    TextView dogNameTV, dogAgeTV, dogBreedTV, dogColorTV, dogOwnerTV;
    ImageView displayPicture;
    RecyclerView eventsRV;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    private ArrayList<EventModel> eventList;
    private FirebaseAuth firebaseAuth;
    public static final String REALTIME_DATABASE_URL = "https://doggy-zone-default-rtdb.firebaseio.com/";

    String userId = "", dogName = "", dogAge = "", dogBreed = "", dogColor = "", dogOwner = "", dogPic = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();

        BottomNavigationView btmNavView = findViewById(R.id.bottom_navbar);
        btmNavView.setSelectedItemId(R.id.menu_home);
        addEventButton = findViewById(R.id.add_event_button);
        dogNameTV = findViewById(R.id.dog_name_heading);
        dogAgeTV = findViewById(R.id.dogAgeTextView);
        dogBreedTV = findViewById(R.id.dogBreedTextView);
        dogColorTV = findViewById(R.id.dogColorTextView);
        dogOwnerTV = findViewById(R.id.ownerTextView);
        displayPicture = findViewById(R.id.home_display_pic);
        eventsRV = findViewById(R.id.home_recycler_view);

        userUISetup();
        loadAllEvents();

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AddEventActivity.class));
            }
        });

        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.menu_home:
                        return true;
                    case R.id.menu_map:
                        startActivity(new Intent(getApplicationContext(), MapActivity.class));
                        overridePendingTransition(0,0);
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

    private void loadAllEvents() {
        eventList = new ArrayList<>();
        userId = firebaseAuth.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance(REALTIME_DATABASE_URL)
                .getReference("Events");
        databaseReference.child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        eventList.clear();
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            EventModel eventMod = dataSnapshot1.getValue(EventModel.class);
                            eventList.add(eventMod);
                        }
                        layoutManager = new LinearLayoutManager(HomeActivity.this);
                        eventsRV.setLayoutManager(layoutManager);
                        mAdapter = new EventAdapter(eventList, HomeActivity.this);
                        eventsRV.setAdapter(mAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void userUISetup() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        userId = firebaseAuth.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance(REALTIME_DATABASE_URL)
                .getReference("Users");
        databaseReference.child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String fName = "" + dataSnapshot.child("fname").getValue();
                        String lName = "" + dataSnapshot.child("lname").getValue();
                        String fullName = fName + " " + lName;

                        dogName = "" + dataSnapshot.child("dog_name").getValue();
                        dogAge = "" + dataSnapshot.child("dog_age").getValue();
                        dogBreed = "" + dataSnapshot.child("dog_breed").getValue();
                        dogColor = "" + dataSnapshot.child("dog_color").getValue();
                        dogPic = "" + dataSnapshot.child("dog_image").getValue();
                        dogOwner = fullName;

                        ImageLoadASyncTask imageLoadASyncTask = new ImageLoadASyncTask(dogPic, displayPicture);
                        imageLoadASyncTask.execute();

                        dogNameTV.setText(dogName);
                        dogAgeTV.setText(dogAge);
                        dogBreedTV.setText(dogBreed);
                        dogColorTV.setText(dogColor);
                        dogOwnerTV.setText(dogOwner);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}