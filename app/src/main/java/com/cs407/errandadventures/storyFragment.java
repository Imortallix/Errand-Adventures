package com.cs407.errandadventures;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;


public class storyFragment extends Fragment {
    // indicates the stage of the story we are in
    public enum Stage {
        INTRO,
        BODY,
        ENDING
    }

    // holds our current stage
    public static Stage stage = Stage.INTRO;

    // introduction paragraph
    private String intro;
    // each paragraph is separated into their own string.  This should make it easier to reveal
    // paragraphs one at a time as the user progresses through the story
    private ArrayList<String> body;
    private int bodyIdx = 0;
    // ending paragraph
    private String ending;

    // list of the stops
    private ArrayList<Stop> stops;

    // the text displayed to the user
    private TextView text;

    // location provider
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback callback;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_story, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // get the stops from the database
        Context context = getActivity().getApplicationContext();
        DBHelper helper = new DBHelper(context.openOrCreateDatabase("toDo", Context.MODE_PRIVATE, null));
        stops = helper.readList(context.getSharedPreferences("com.cs407.errandadventures", Context.MODE_PRIVATE).getString("username", ""));

        // if stops were added, generate body paragraphs
        if (stops.size() > 0 && (body == null || stops.size() > body.size())) {
            genBodyParagraph();
        }

        // grab the current location
        getLocation();

        updateText(view);
    }

    public void onDestroyView() {
        super.onDestroyView();
        mFusedLocationProviderClient.removeLocationUpdates(callback);
    }

    private void getLocation() {
        if (mFusedLocationProviderClient == null) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        }

        // create the request, update every second
        LocationRequest request = new LocationRequest.Builder(1000).build();

        // callback for handling location updates
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                // just in case something goes horribly wrong
                if (locationResult == null) {
                    return;
                }
                Location location = locationResult.getLastLocation();

                for (int i = 0; i < stops.size(); i++) {
                    // grab lat and lng for the stop
                    String clean1 = stops.get(i).getLatLng().replace("lat/lng:", "");
                    String clean2 = clean1.replace("(", "");
                    String clean3 = clean2.replace(")", "").trim();
                    String[] latlng = clean3.split(",");
                    double lat = Double.parseDouble(latlng[0]);
                    double lng = Double.parseDouble(latlng[1]);

                    // if we are within 100 meters of a stop, change the paragraph to the correct body
                    if (getDistance(location, lat, lng) < 100) {
                        bodyIdx = i;
                        stage = Stage.BODY;
                        break;
                    }
                }

            }
        };

        int permission = ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        mFusedLocationProviderClient.requestLocationUpdates(request, callback, Looper.getMainLooper());

    }

    private void updateText(View view) {
        // set the text of the textview to the appropriate text
        text = view.findViewById(R.id.storyText);
        switch (stage) {
            case INTRO:
                // set the text to the intro
                if (intro == null || intro.isEmpty()) {
                    intro = "PLACEHOLDER INTRO";
                }
                text.setText(intro);
                break;
            case BODY:
                text.setText(body.get(bodyIdx));
                break;
            case ENDING:
                // set the text to the ending
                if (ending == null || ending.isEmpty()) {
                    ending = "PLACEHOLDER ENDING";
                }
                text.setText(ending);
                break;
        }
    }

    private double getDistance(Location location, double lat, double lng) {
        return Math.sqrt(Math.pow(location.getLatitude() - lat, 2) + Math.pow(location.getLongitude() - lng, 2));
    }

    private void genBodyParagraph() {
        if (body == null) {
            body = new ArrayList<>();
        }

        // add body paragraphs for each new stop\
        // we assume that there are fewer body paragraphs than stops
        for (int i = body.size(); i < stops.size(); i++) {
            body.add("PLACEHOLDER BODY " + (i + 1));
        }

    }

}