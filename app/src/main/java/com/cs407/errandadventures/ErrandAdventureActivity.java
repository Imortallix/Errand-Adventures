package com.cs407.errandadventures;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ErrandAdventureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.errand_adventure_activity);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    //still need the edit adverture part
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        SharedPreferences sp = getSharedPreferences("com.cs407.lab5_milestone1", Context.MODE_PRIVATE);
        if (id == R.id.logout) {
            sp.edit().clear().apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if(id == R.id.edit) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}