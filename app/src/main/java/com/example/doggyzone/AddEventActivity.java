package com.example.doggyzone;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class AddEventActivity extends AppCompatActivity {

    EditText eventTitleET, eventDescET, eventLocationET;
    Spinner startTimeHourSpinner, startTimeMinuteSpinner, startTimeDaySpinner,
            endTimeHourSpinner, endTimeMinuteSpinner, endTimeDaySpinner;
    Switch allDaySwitch;
    Button addAppointmentButton;
    LinearLayout startTimeLL, endTimeLL;
    ImageButton backButton;

    String eventTitle = "", eventDesc = "", eventLocation = "";
    Boolean allDayCheck = false;

    String[] hours = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
    String[] minutes = {
            "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
            "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
            "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"};
    String[] days = { "AM", "PM" };

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
        startTimeDaySpinner = findViewById(R.id.add_event_start_time_day_spinner);
        endTimeHourSpinner = findViewById(R.id.add_event_end_time_hour_spinner);
        endTimeMinuteSpinner = findViewById(R.id.add_event_end_time_minute_spinner);
        endTimeDaySpinner = findViewById(R.id.add_event_end_time_day_spinner);
        addAppointmentButton = findViewById(R.id.add_event_add_appointment_button);
        startTimeLL = findViewById(R.id.start_time_linear_layout);
        endTimeLL = findViewById(R.id.end_time_linear_layout);

        allDaySwitch.setChecked(false);

        ArrayAdapter<String> startHourAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hours);
        ArrayAdapter<String> startMinuteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, minutes);
        ArrayAdapter<String> startDayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, days);
        ArrayAdapter<String> endHourAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hours);
        ArrayAdapter<String> endMinuteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, minutes);
        ArrayAdapter<String> endDayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, days);

        startTimeHourSpinner.setAdapter(startHourAdapter);
        startTimeMinuteSpinner.setAdapter(startMinuteAdapter);
        startTimeDaySpinner.setAdapter(startDayAdapter);
        endTimeHourSpinner.setAdapter(endHourAdapter);
        endTimeMinuteSpinner.setAdapter(endMinuteAdapter);
        endTimeDaySpinner.setAdapter(endDayAdapter);

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
        eventTitle = eventTitleET.getText().toString().trim();
        eventDesc = eventDescET.getText().toString().trim();
        eventLocation = eventLocationET.getText().toString().trim();
//        allDayCheck = allDaySwitch.isChecked();

        int startHour = startTimeHourSpinner.getSelectedItemPosition();
        int startMinute = startTimeMinuteSpinner.getSelectedItemPosition();
        int startDay = startTimeDaySpinner.getSelectedItemPosition();
        int endHour = endTimeHourSpinner.getSelectedItemPosition();
        int endMinute = endTimeMinuteSpinner.getSelectedItemPosition();
        int endDay = endTimeDaySpinner.getSelectedItemPosition();

        Toast.makeText(this, "Title: " + eventTitle + "\n" + "Description: " + eventDesc +
                "\n" + "Location: " + eventLocation + "\n" + "allDayCheck: " + allDayCheck + "\n" + hours[startHour] + ":" + minutes[startMinute] + " " + days[startDay] + "\n" + hours[endHour] + ":" + minutes[endMinute] + " " + days[endDay], Toast.LENGTH_SHORT).show();

    }
}