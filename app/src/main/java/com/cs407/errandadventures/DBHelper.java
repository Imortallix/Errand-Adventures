package com.cs407.errandadventures;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBHelper {
    static SQLiteDatabase database;
    public DBHelper(SQLiteDatabase database) {this.database = database;}

    public static void createTable() {
        database.execSQL("CREATE TABLE IF NOT EXISTS toDoDB"+
                "(id INTEGER PRIMARY KEY, taskID INTEGER, username TEXT, task TEXT, location TEXT, latlng TEXT)");
    }

    public void saveStop(String username, String task, String location, String latlng) {
        createTable();
        database.execSQL("INSERT INTO toDoDB(username, task, location, latlng) VALUES (?,?,?,?)",
                new String[]{username, task, location, latlng});
    }

    public ArrayList<Stop> readList(String username) {
        createTable();
        Cursor c = database.rawQuery("SELECT * FROM toDoDB WHERE username LIKE ?",
                new String[] {"%"+username+"%"});;
        int taskIndex = c.getColumnIndex("task");
        int locationIndex = c.getColumnIndex("location");
        int latlngIndex = c.getColumnIndex("latlng");

        //int titleIndex = c.getColumnIndex("title");
        //int contextIndex = c.getColumnIndex("content");
        c.moveToFirst();
        ArrayList<Stop> toDoList = new ArrayList<>();
        while (!c.isAfterLast()) {
            String task = c.getString(taskIndex);
            String location = c.getString(locationIndex);
            String latlng = c.getString(latlngIndex);
            Stop stop = new Stop(username, task, location, latlng);
            toDoList.add(stop);
            c.moveToNext();
        }
        c.close();
        //database.close();

        return toDoList;
    }
}

