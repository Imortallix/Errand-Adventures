package com.cs407.errandadventures;

public class Stop {
    String task;
    String username;
    String location;
    String latlng;

    public Stop(String username, String task, String location, String latlng) {
        this.username = username;
        this.task = task;
        this.latlng = latlng;
        this.location = location;
    }

    public String getName() {
        return this.username;
    }
    public String getTask() {return this.task;}

    public String getLocation() {return this.location;}

    public String getLatLng() {return this.latlng;}
}
