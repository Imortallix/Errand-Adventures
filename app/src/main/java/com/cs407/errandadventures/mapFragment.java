package com.cs407.errandadventures;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class mapFragment extends Fragment {

    ArrayList<Stop> locations = new ArrayList<>();

    private GoogleMap mMap;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map_fragment);

        mapFragment.getMapAsync(googleMap -> {
            displayMyLocation();
        });
        mFusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(getActivity());

        // Return view
        return view;
    }

    private void displayMyLocation() {
        int permission = ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            mFusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(getActivity(), task -> {
                        Location mLastKnownLocation = task.getResult();
                        if (task.isSuccessful() && mLastKnownLocation != null) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()))
                                    .title("Current")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), 10));
                        }
                    });
        }
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        Context context = getActivity().getApplicationContext();
        SharedPreferences sp = context.getSharedPreferences("com.cs407.errandadventures", Context.MODE_PRIVATE);
        String s = sp.getString("username", "");
        System.out.println("username: " + s);

        SQLiteDatabase database = context.openOrCreateDatabase("toDo", Context.MODE_PRIVATE, null);
        DBHelper helper = new DBHelper(database);

        ArrayList<String> display = new ArrayList<>();

        locations = helper.readList(s);
        System.out.println("locations: " + locations);

        SupportMapFragment mapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map_fragment);

        if (locations.size() != 0) {
        for (Stop stop:locations) {
            if (stop.getLatLng() != (null)) {
                String clean1 = stop.getLatLng().replace("lat/lng:", "");
                String clean2 = clean1.replace("(", "");
                String clean3 = clean2.replace(")", "").trim();
                String[] latlng = clean3.split(",");
                System.out.println("latlng: " + latlng[0] + " " + latlng[1]);

                mapFragment.getMapAsync(googleMap -> {
                    mMap = googleMap;
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1])))
                            .title(stop.getLocation()));
                });
            }
        }
        } else {
                mapFragment.getMapAsync(googleMap -> {
                    mMap = googleMap;

                });
            }
        }








    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayMyLocation();
            }
        }
    }

}