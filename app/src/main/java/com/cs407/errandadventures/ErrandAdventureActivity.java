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
        FragmentManager fg = getSupportFragmentManager();
        Button frag1 = findViewById(R.id.destinationsButton);
        Button frag2 = findViewById(R.id.mapButton);
        Button frag3 = findViewById(R.id.storyButton);

        frag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fg.beginTransaction()
                        .replace(R.id.fragmentContainerView, Destinations.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("showingDestination")
                        .commit();
            }
        });

        frag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fg.beginTransaction()
                        .replace(R.id.fragmentContainerView, mapFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("showingMap")
                        .commit();
            }
        });

        frag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fg.beginTransaction()
                        .replace(R.id.fragmentContainerView, storyFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("showingStory")
                        .commit();
            }
        });
        fg.beginTransaction()
                .replace(R.id.fragmentContainerView, Destinations.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("showingDestination")
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        SharedPreferences sp = getSharedPreferences("com.cs407.errandadventures", Context.MODE_PRIVATE);
        if (id == R.id.logout) {
            sp.edit().clear().apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.restart) {
            getApplicationContext().deleteDatabase("toDo");
        }
        return super.onOptionsItemSelected(item);
    }
}