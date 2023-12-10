package com.cs407.errandadventures;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class Maps extends FragmentActivity {

    private GoogleMap gmap;

    private final LatLng pos = new LatLng(43.07585077745941, -89.40407425095019);
    private FusedLocationProviderClient fLoc;

    private static final int ACCESS_LOCATION = 12;

    public Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        refresh = findViewById(R.id.button);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(googleMap -> {
            gmap = googleMap;
            googleMap.addMarker(new MarkerOptions().position(pos).title("Destination"));
            displayLoc();
        });

        fLoc = LocationServices.getFusedLocationProviderClient(this);
    }


    private void displayLoc() {
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);

        if(permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},ACCESS_LOCATION);
        } else {
            fLoc.getLastLocation().addOnCompleteListener(this, task -> {
                Location lastKnow = task.getResult();
                if (task.isSuccessful() && lastKnow != null) {
                    Log.i("info", "success");
                    gmap.addMarker(new MarkerOptions().position(new LatLng(lastKnow.getLatitude(), lastKnow.getLongitude())).title("Your Position"));
                    gmap.addPolyline(new PolylineOptions().add(new LatLng(lastKnow.getLatitude(), lastKnow.getLongitude()), pos));
                } else {
                    //Log.i("last known", lastKnow.toString());
                    Log.i("info", "failed");
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult (int code, @NonNull String[] permissions, @NonNull int[] result) {
        super.onRequestPermissionsResult(code, permissions, result);
        if (code == ACCESS_LOCATION) {
            if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
                displayLoc();
            }
        }
    }
}