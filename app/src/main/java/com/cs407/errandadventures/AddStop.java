package com.cs407.errandadventures;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddStop extends AppCompatActivity {

    private int id = -1;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stop2);

        EditText edit = findViewById(R.id.task);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

    }

    public void done(View view) {

        EditText edit = findViewById(R.id.task);
        String task = edit.getText().toString();

        Context context = getApplicationContext();
        SQLiteDatabase database =context.openOrCreateDatabase("toDo", Context.MODE_PRIVATE, null);
        DBHelper helper = new DBHelper(database);

        if(id == -1) {

            helper.saveNote(username, task);

        } else {
            //helper.update(username, date, title, content);
        }

        helper.database.close();
        Intent intent = new Intent(this, ErrandAdventureActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);

    }
}