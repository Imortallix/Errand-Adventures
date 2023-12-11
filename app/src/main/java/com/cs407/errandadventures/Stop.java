package com.cs407.errandadventures;

public class Stop {
    String task;
    String username;
    String location;
    String latlng;
    boolean checked;

    public Stop(String username, String task, String location, String latlng) {
        this.username = username;
        this.task = task;
        this.latlng = latlng;
        this.location = location;
        this.checked = false;
    }

    public String getName() {
        return this.username;
    }
    public String getTask() {return this.task;}

    public String getLocation() {return this.location;}

    public String getLatLng() {return this.latlng;}

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
