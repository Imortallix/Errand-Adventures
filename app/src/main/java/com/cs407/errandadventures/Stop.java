package com.cs407.errandadventures;

public class Stop {
    String task;
    String username;
    String location;
    String latlng;
    String checked;

    public Stop(String username, String task, String location, String latlng, String checked) {
        this.username = username;
        this.task = task;
        this.latlng = latlng;
        this.location = location;
        this.checked = checked;
    }

    public String getName() {
        return this.username;
    }
    public String getTask() {return this.task;}

    public String getLocation() {return this.location;}

    public String getLatLng() {return this.latlng;}

    public String isChecked() {return checked;}
    public void setChecked(String checked) {this.checked = checked;}
}
