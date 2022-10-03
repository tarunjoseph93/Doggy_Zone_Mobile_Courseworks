package com.example.doggyzone.models;

public class EventModel {
    public String event_title, event_desc, event_location, event_all_day, start_hour,
            start_minute, end_hour, end_minute;

    public long day, month, year;

    public EventModel() {
    }

    public EventModel(String event_title, String event_desc, String event_location,
                      String event_all_day, String start_hour, String start_minute,
                      String end_hour, String end_minute, long day, long month, long year) {
        this.event_title = event_title;
        this.event_desc = event_desc;
        this.event_location = event_location;
        this.event_all_day = event_all_day;
        this.start_hour = start_hour;
        this.start_minute = start_minute;
        this.end_hour = end_hour;
        this.end_minute = end_minute;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEvent_desc() {
        return event_desc;
    }

    public void setEvent_desc(String event_desc) {
        this.event_desc = event_desc;
    }

    public String getEvent_location() {
        return event_location;
    }

    public void setEvent_location(String event_location) {
        this.event_location = event_location;
    }

    public String getEvent_all_day() {
        return event_all_day;
    }

    public void setEvent_all_day(String event_all_day) {
        this.event_all_day = event_all_day;
    }

    public String getStart_hour() {
        return start_hour;
    }

    public void setStart_hour(String start_hour) {
        this.start_hour = start_hour;
    }

    public String getStart_minute() {
        return start_minute;
    }

    public void setStart_minute(String start_minute) {
        this.start_minute = start_minute;
    }

    public String getEnd_hour() {
        return end_hour;
    }

    public void setEnd_hour(String end_hour) {
        this.end_hour = end_hour;
    }

    public String getEnd_minute() {
        return end_minute;
    }

    public void setEnd_minute(String end_minute) {
        this.end_minute = end_minute;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public long getMonth() {
        return month;
    }

    public void setMonth(long month) {
        this.month = month;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }
}
