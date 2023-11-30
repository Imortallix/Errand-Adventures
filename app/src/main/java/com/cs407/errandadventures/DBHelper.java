package com.cs407.errandadventures;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper {
    static SQLiteDatabase database;
    public DBHelper(SQLiteDatabase database) {this.database = database;}

    public static void createTable() {
        database.execSQL("CREATE TABLE IF NOT EXISTS toDo"+
                "(id INTEGER PRIMARY KEY, taskID INTEGER, username TEXT, task TEXT)");
    }

    public void saveNote(String username, String task) {
        createTable();
        database.execSQL("INSERT INTO toDo(username, task) VALUES (?,?)",
                new String[]{username, task});
    }

    public ArrayList<Stop> readList(String username) {
        createTable();
        Cursor c = database.rawQuery("SELECT * FROM toDo WHERE username LIKE ?",
                new String[] {"%"+username+"%"});
        int taskInde = c.getColumnIndex("username");
        //int taskIndex = c.getColumnIndex("task");
        Log.i("index", ("username index is "+taskInde));
        //int titleIndex = c.getColumnIndex("title");
        //int contextIndex = c.getColumnIndex("content");
        c.moveToFirst();
        ArrayList<Stop> toDoList = new ArrayList<>();
        while (!c.isAfterLast()) {
            String task = c.getString(taskInde);
            //String date = c.getString(dateIndex);
            //String content = c.getString(contextIndex);
            Stop stop = new Stop(username, task);
            toDoList.add(stop);
            c.moveToNext();
        }
        c.close();
        //database.close();

        return toDoList;
    }
}

