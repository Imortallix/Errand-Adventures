package com.cs407.errandadventures;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("com.cs407.errandadventures", Context.MODE_PRIVATE);
        String un = sp.getString("username", "");
        if(un != "") {
            secondPage(un);
        } else {
            setContentView(R.layout.activity_main);
        }
        ImageButton duck = findViewById(R.id.duck);
        ImageButton pen = findViewById(R.id.penguin);
        duck.setImageDrawable(getDrawable(R.mipmap.ic_duck_foreground));
        duck.setBackgroundColor(Color.TRANSPARENT);
        pen.setImageDrawable(getDrawable(R.mipmap.ic_peng_foreground));
        pen.setBackgroundColor(Color.TRANSPARENT);
        duck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duck.setBackgroundColor(Color.GRAY);
                pen.setBackgroundColor(Color.TRANSPARENT);
                sp.edit().putString("avatar", "duck").apply();
            }
        });
        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pen.setBackgroundColor(Color.GRAY);
                duck.setBackgroundColor(Color.TRANSPARENT);
                sp.edit().putString("avatar", "pen").apply();
            }
        });
    }

    public void clickFunction(View view) {
        EditText edit = (EditText) findViewById(R.id.editTextText);
        String s = edit.getText().toString();
        SharedPreferences sp = getSharedPreferences("com.cs407.errandadventures", Context.MODE_PRIVATE);
        sp.edit().putString("username", s).apply();
        sp.edit().putInt("completed", 0).apply();
        secondPage(s);
    }

    public void secondPage(String s) {
        Intent intent = new Intent(this, ErrandAdventureActivity.class);
        intent.putExtra("message", s);
        startActivity(intent);
    }
}