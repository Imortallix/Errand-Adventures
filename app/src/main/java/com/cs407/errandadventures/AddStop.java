package com.cs407.errandadventures;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class AddStop extends AppCompatActivity {

    private String username;
    private String location;
    private String latlng;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stop2);

        EditText edit = findViewById(R.id.task);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        Places.initialize(getApplicationContext(), "AIzaSyCexdL1RcVZxgHsJSg2jeaK889V0EgQL6g");

    }

    // cade adapted from https://developers.google.com/maps/documentation/places/android-sdk/autocomplete
    public void startAutocompleteActivity(View view) {
        List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(this);
        startAutocomplete.launch(intent);
    }

    private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                TextView name = (TextView)findViewById(R.id.name);
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Place place = Autocomplete.getPlaceFromIntent(intent);
                        Log.i(TAG, "Place: " + place.getName() + " " + place.getLatLng());
                        name.setText(place.getName());
                        location = place.getName();
                        latlng = place.getLatLng().toString();
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i(TAG, "User canceled autocomplete");
                }
            });


    public void cancel(View view) {
        Intent intent = new Intent(this, ErrandAdventureActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void done(View view) {

        EditText edit = findViewById(R.id.task);
        String task = edit.getText().toString();

        Context context = getApplicationContext();
        SQLiteDatabase database =context.openOrCreateDatabase("toDo", Context.MODE_PRIVATE, null);
        DBHelper helper = new DBHelper(database);

        TextView cancelMessage = (TextView)findViewById(R.id.cmessage);

        if(location != null && task != null) {
            helper.saveStop(username, task, location, latlng, "false");
            helper.database.close();
            Intent intent = new Intent(this, ErrandAdventureActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            cancelMessage.setText("");
        } else {
            cancelMessage.setText("Location or Task is empty.");
        }
    }
}