package com.cs407.errandadventures;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBHelper {
    static SQLiteDatabase database;
    public DBHelper(SQLiteDatabase database) {this.database = database;}

    public static void createTable() {
        database.execSQL("CREATE TABLE IF NOT EXISTS toDoDB"+
                "(id INTEGER PRIMARY KEY, taskID INTEGER, username TEXT, task TEXT, location TEXT, latlng TEXT, checked TEXT)");
    }

    public void saveStop(String username, String task, String location, String latlng, String checked) {
        createTable();
        database.execSQL("INSERT INTO toDoDB(username, task, location, latlng, checked) VALUES (?,?,?,?,?)",
                new String[]{username, task, location, latlng, checked});
    }

    public void setCheck(String checked, String task, String location, String username) {
        createTable();
        database.execSQL("UPDATE toDoDB set checked=? where task = ? and location = ? and username = ?",
                new String[]{checked, task, location, username});
    }

    public ArrayList<Stop> readList(String username) {
        createTable();
        Cursor c = database.rawQuery("SELECT * FROM toDoDB WHERE username LIKE ?",
                new String[] {"%"+username+"%"});;
        int taskIndex = c.getColumnIndex("task");
        int locationIndex = c.getColumnIndex("location");
        int latlngIndex = c.getColumnIndex("latlng");
        int checkIndex = c.getColumnIndex("checked");

        c.moveToFirst();
        ArrayList<Stop> toDoList = new ArrayList<>();
        while (!c.isAfterLast()) {
            String task = c.getString(taskIndex);
            String location = c.getString(locationIndex);
            String latlng = c.getString(latlngIndex);
            String check = c.getString(checkIndex);
            Stop stop = new Stop(username, task, location, latlng, check);
            toDoList.add(stop);
            c.moveToNext();
        }
        c.close();
        //database.close();

        return toDoList;
    }

    public void deleteNote(String task, String location) {
        createTable();
        String latlng = "";
        Cursor c = database.rawQuery("SELECT latlng FROM toDoDB WHERE location = ? and task = ?",
                new String[]{location, task});
        if (c.moveToNext()) {
            latlng = c.getString(0);
        }
        database.execSQL("DELETE FROM toDoDB WHERE location = ? AND task = ? AND latlng = ?",
                new String[] {location, task, latlng});
        c.close();
    }
}

