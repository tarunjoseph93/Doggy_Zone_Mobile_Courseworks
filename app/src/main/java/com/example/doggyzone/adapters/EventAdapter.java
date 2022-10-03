package com.example.doggyzone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doggyzone.R;
import com.example.doggyzone.models.EventModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    ArrayList<EventModel> eventList;
    Context context;

//    FirebaseAuth firebaseAuth;

    public EventAdapter(ArrayList<EventModel> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_page_custom_rows, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventModel eventMod = eventList.get(position);
        String day, month, year, startHour, startMinute, endHour, endMinute;
        day = Long.toString(eventMod.day);
        month = Long.toString(eventMod.month);
        year = Long.toString(eventMod.year);
        startHour = eventMod.start_hour;
        startMinute = eventMod.start_minute;
        endHour = eventMod.end_hour;
        endMinute = eventMod.end_minute;

        String date = day + ":" + month + ":" + year;
        String startTime = startHour + ":" + startMinute;
        String endTime = endHour + ":" + endMinute;

        holder.eventTitle.setText(eventMod.event_title);
        holder.eventDesc.setText(eventMod.event_desc);
        holder.eventLocation.setText(eventMod.event_location);
        holder.eventAllDay.setText(eventMod.event_all_day);
        holder.eventDate.setText(date);
        holder.eventStartTime.setText(startTime);
        holder.eventEndTime.setText(endTime);

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventDesc, eventLocation, eventAllDay, eventDate, eventStartTime, eventEndTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.event_title_custom_row);
            eventDesc = itemView.findViewById(R.id.event_description_custom_row);
            eventLocation = itemView.findViewById(R.id.event_location_custom_row);
            eventAllDay = itemView.findViewById(R.id.event_all_day_custom_row);
            eventDate = itemView.findViewById(R.id.event_date_custom_row);
            eventStartTime = itemView.findViewById(R.id.event_start_time_custom_row);
            eventEndTime = itemView.findViewById(R.id.event_end_time_custom_row);

//            firebaseAuth = FirebaseAuth.getInstance();
        }
    }
}
