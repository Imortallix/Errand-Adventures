package com.cs407.errandadventures;

public class Stop {
    String task;
    String username;
    String address;
    int id;

    public Stop(String username, String task) {
        this.username = username;
        this.task = task;
    }

    public String getName() {
        return this.username;
    }
    public String getTask() {return this.task;}
}
