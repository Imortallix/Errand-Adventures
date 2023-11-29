package com.cs407.errandadventures;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class ErrandAdventureActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.errand_adventure_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Button destinationstButton = findViewById(R.id.destinationsButton);
        destinationstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, Destinations.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("Showing First")
                        .commit();
            }
        });

        Button mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, mapFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("Showing Second")
                        .commit();
            }
        });

        Button storyButton = findViewById(R.id.storyButton);
        storyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, storyFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("Showing Third")
                        .commit();
            }
        });
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