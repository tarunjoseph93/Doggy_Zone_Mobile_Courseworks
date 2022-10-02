package com.example.doggyzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.listener.PermissionGrantedResponse;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class AddEventActivity extends AppCompatActivity {

    public static final String REALTIME_DATABASE_URL = "https://doggy-zone-default-rtdb.firebaseio.com/";
    private static final int PERMISSIONS_CALENDAR_CODE = 20;
    EditText eventTitleET, eventDescET, eventLocationET;
    Spinner startTimeHourSpinner, startTimeMinuteSpinner,
            endTimeHourSpinner, endTimeMinuteSpinner;
    Switch allDaySwitch;
    Button addAppointmentButton;
    LinearLayout startTimeLL, endTimeLL;
    ImageButton backButton;
    DatePicker datePicker;

    private FirebaseAuth firebaseAuth;

    String eventTitle = "", eventDesc = "", eventLocation = "", day = "", month = "", year = "";
    Boolean allDayCheck = false;

    String[] hours = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
            "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" };
    String[] minutes = {
            "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
            "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
            "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        backButton = findViewById(R.id.eventBackButton);
        eventTitleET = findViewById(R.id.add_event_title_edittext);
        eventDescET = findViewById(R.id.add_event_desc_edittext);
        eventLocationET = findViewById(R.id.add_event_location_edittext);
        allDaySwitch = findViewById(R.id.add_event_switch);
        startTimeHourSpinner = findViewById(R.id.add_event_start_time_hour_spinner);
        startTimeMinuteSpinner = findViewById(R.id.add_event_start_time_minute_spinner);
        endTimeHourSpinner = findViewById(R.id.add_event_end_time_hour_spinner);
        endTimeMinuteSpinner = findViewById(R.id.add_event_end_time_minute_spinner);
        addAppointmentButton = findViewById(R.id.add_event_add_appointment_button);
        startTimeLL = findViewById(R.id.start_time_linear_layout);
        endTimeLL = findViewById(R.id.end_time_linear_layout);
        datePicker = findViewById(R.id.add_event_date_picker);

        firebaseAuth = FirebaseAuth.getInstance();
        allDaySwitch.setChecked(false);

        startTimeHourSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, hours));
        startTimeMinuteSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, minutes));
        endTimeHourSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, hours));
        endTimeMinuteSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, minutes));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        allDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    hideEventLinearLayout();
                    allDayCheck = true;
                }
                else
                {
                    showEventLinearLayout();
                    allDayCheck = false;
                }
            }
        });

        addAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAppointment();
            }
        });
    }

    private void showEventLinearLayout() {
        startTimeLL.setVisibility(View.VISIBLE);
        endTimeLL.setVisibility(View.VISIBLE);

    }

    private void hideEventLinearLayout() {
        startTimeLL.setVisibility(View.GONE);
        endTimeLL.setVisibility(View.GONE);
    }

    private void addAppointment() {
        String userId = firebaseAuth.getUid();
        HashMap<String, Object> hashMap = new HashMap<>();

        eventTitle = eventTitleET.getText().toString().trim();
        eventDesc = eventDescET.getText().toString().trim();
        eventLocation = eventLocationET.getText().toString().trim();

        int startHourIndex = startTimeHourSpinner.getSelectedItemPosition();
        int startMinuteIndex = startTimeMinuteSpinner.getSelectedItemPosition();
        int endHourIndex = endTimeHourSpinner.getSelectedItemPosition();
        int endMinuteIndex = endTimeMinuteSpinner.getSelectedItemPosition();

        hashMap.put("event_title", eventTitle);
        hashMap.put("event_desc", eventDesc);
        hashMap.put("event_location", eventLocation);
        if(allDaySwitch.isChecked()) {
            allDayCheck = true;
            hashMap.put("event_all_day", allDayCheck.toString());
            hashMap.put("day", datePicker.getDayOfMonth());
            hashMap.put("month", datePicker.getMonth() + 1);
            hashMap.put("year", datePicker.getYear());
        }
        else {
            allDayCheck = false;
            hashMap.put("event_all_day", allDayCheck.toString());
            hashMap.put("start_hour", hours[startHourIndex]);
            hashMap.put("start_minute", minutes[startMinuteIndex]);
            hashMap.put("end_hour", hours[endHourIndex]);
            hashMap.put("end_minute", minutes[endMinuteIndex]);
            hashMap.put("day", datePicker.getDayOfMonth());
            hashMap.put("month", datePicker.getMonth() + 1);
            hashMap.put("year", datePicker.getYear());
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance(REALTIME_DATABASE_URL)
                .getReference("Events");

        databaseReference.child(userId).child(eventTitle).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
//                                startActivity(new Intent(AddEventActivity.this, HomeActivity.class));

                                Intent intent = new Intent(Intent.ACTION_INSERT).
                                        setData(CalendarContract.Events.CONTENT_URI).
                                        putExtra(CalendarContract.Events.TITLE, eventTitle).
                                        putExtra(CalendarContract.Events.DESCRIPTION, eventDesc).
                                        putExtra(CalendarContract.Events.EVENT_LOCATION, eventLocation);

                                if(allDaySwitch.isChecked()) {
                                    long dateMillis = 0;
                                    Calendar date = Calendar.getInstance();
                                    date.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                                    dateMillis = date.getTimeInMillis();

                                    intent.putExtra(CalendarContract.Events.ALL_DAY, allDayCheck);
                                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, dateMillis);
                                }
                                else {
                                    long startMillis = 0, endMillis = 0;

                                    Calendar startTime = Calendar.getInstance();
                                    startTime.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                                            Integer.parseInt(hours[startHourIndex]), Integer.parseInt(minutes[startMinuteIndex]));
                                    startMillis = startTime.getTimeInMillis();

                                    Calendar endTime = Calendar.getInstance();
                                    endTime.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                                            Integer.parseInt(hours[endHourIndex]), Integer.parseInt(minutes[endMinuteIndex]));
                                    endMillis = endTime.getTimeInMillis();

                                    intent.putExtra(CalendarContract.Events.ALL_DAY, allDayCheck);
                                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis);
                                    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis);
                                }
                                startActivity(intent);

                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddEventActivity.this, "Could not write to database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        Toast.makeText(this, "Title: " + eventTitle + "\n" + "Description: " + eventDesc +
                "\n" + "Location: " + eventLocation + "\n" + "allDayCheck: " + allDayCheck + "\n" + hours[startHourIndex] + ":" +
                minutes[startMinuteIndex] + "\n" + hours[endHourIndex] + ":" + minutes[endMinuteIndex] + "\n" +
                "Date: " + datePicker.getDayOfMonth() + "-" + datePicker.getMonth() + "-" + datePicker.getYear(), Toast.LENGTH_SHORT).show();
    }
}