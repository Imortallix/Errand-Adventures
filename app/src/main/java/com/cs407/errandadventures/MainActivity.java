package com.cs407.errandadventures;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("com.cs407.lab5_milestone1", Context.MODE_PRIVATE);
        String un = sp.getString("username", "");
        if(un != "") {
            secondPage(un);
        } else {
            setContentView(R.layout.activity_main);
        }
    }

    public void clickFunction(View view) {
        EditText edit = (EditText) findViewById(R.id.editTextText);
        String s = edit.getText().toString();
        SharedPreferences sp = getSharedPreferences("com.cs407.lab5_milestone1", Context.MODE_PRIVATE);
        sp.edit().putString("username", s).apply();
        secondPage(s);
    }

    public void secondPage(String s) {
        Intent intent = new Intent(this, ErrandAdventureActivity.class);
        intent.putExtra("message", s);
        startActivity(intent);
    }
}