package com.cs407.errandadventures;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
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
    private int bodyIndex;
    private int maxIndex;

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
        maxIndex = context.getSharedPreferences("com.cs407.errandadventures", Context.MODE_PRIVATE).getInt("completed", 0);
        bodyIndex = maxIndex - 1;
        stage = Stage.INTRO;

        // if stops were added, generate body paragraphs
        if (stops.size() > 0 && (body == null || stops.size() > body.size())) {
            genBodyParagraph();
        }

        intro = "Mighty warrior! Thank you for coming to us in our time of need. The fire dragon " +
                "Balthazar is terrorizing the city of Nosidam. You'll need to gather as many " +
                "magical artifacts as you can before you can take it down.";
        body = new ArrayList<String>();
        body.add("First you must delve into the Mines of Thoria. It is said they hold a " +
                "weapon powerful enough to slay our foe.");
        body.add("Aha! You've recovered Dragonbane, a sword sharp enough to pierce the " +
                "thick hide of a drake! I doubt Balthazar will be happy to see this. " +
                "Next you'll need to bargain with the elf smiths in Lorthen to forge " +
                "you armor that will protect you in your battle.");
        body.add("Wow! A shimmering set of Everscale! That should keep you safe from " +
                "Balthazar's claws. I don't want to know what price you paid the elves " +
                "for such a treasure. To protect you from his fire you'll need to seek " +
                "out the Amulet of the Sunstone. Last I heard, it's hidden deep in the " +
                "vaults of the mage guild.");
        body.add("So you recovered the amulet! It's not as bright as I would have thought. " +
                "I won't ask how you got it, but I doubt you should expect any help from " +
                "those spellcasters in the future. The next thing you'll need is a potion " +
                "of strength. Any potionmaker should be able to brew it, but I've been " +
                "told the ingredients are tricky to acquire.");
        body.add("What went into this thing? It smells like giant's toes and spider hair. " +
                "Another artifcat that'll help you is the Ring of Quickwit, dragons are " +
                "famous for their love of games and puzzles. It's the prize possession of " +
                "the king's jester, and I imagine he'll challenge you to a game of " +
                "riddles for it.");
        body.add("Quite the shine you got on your hand now! I doubt I'll be exchanging " +
                "banter with you anytime soon. If you really want to escape the battle " +
                "with all your limbs, you could use the Shield of Galadon. It's the heirloom " +
                "of the House of Galadon and said to protect its bearer against even the " +
                "mightiest of blows. Only problem is its been missing for 500 years. With " +
                "your skills though, I'm sure you can track it down; start by talking to the " +
                "Lady Galadon, she might know where to start looking.");
        body.add("Well I'll be! You found the shield! I'm sure House Galadon will be happy " +
                "to see it protecting warriors in battle once more. If you're really serious " +
                "about taking down Balthazar, you should read the Dragon Tome. It contains " +
                "all sorts of secret, dragon-slaying knowledge. It was held in the Great " +
                "Library until it was raided by goblins. You'll have to retrieve it from " +
                "their stronghold in the Darkwood.");
        body.add("I'm sure goblins are no problem for such a great warrior as yourself, " +
                "but you're truly exceptional to storm their stronghold singlehandedly! " +
                "The next items that will help you on your quest are the Boots of the " +
                "Rivertreader. They'll ensure you're always quick and light on your feet. " +
                "The river nayads hold them, waiting for a worthy bearer to pass their trials.");
        body.add("Woah! Didn't hear you creep up on me with those new treads on! The " +
                "next item to help you complete your quest is a belt from Colan the " +
                "Leatherworker. They're not magic, but they look great and keep your pants up!");

        // set the back and next buttons
        Button nextButton = view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> {
            Context context2 = getActivity().getApplicationContext();
            maxIndex = context2.getSharedPreferences("com.cs407.errandadventures", Context.MODE_PRIVATE).getInt("completed", 0);
            if (bodyIndex < maxIndex) {
                bodyIndex++;
                stage = Stage.BODY;
            } else {
                stage = Stage.ENDING;
            }
            updateText();
        });
        Button backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            if (bodyIndex > -1) {
                bodyIndex--;
            }
            if (bodyIndex < 0){
                stage = Stage.INTRO;
            } else {
                stage = Stage.BODY;
            }
            updateText();
        });

        // set the text of the textview to the appropriate text
        text = view.findViewById(R.id.storyText);

        updateText();
    }

    private void getLocation() {
        if (mFusedLocationProviderClient == null) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        }

        // create the request, update every second
        CurrentLocationRequest request = new CurrentLocationRequest.Builder().build();

        int permission = ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        mFusedLocationProviderClient.getCurrentLocation(request, null).addOnCompleteListener(getActivity(), task -> {
            Location location = task.getResult();

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
                    bodyIndex = i;
                    stage = Stage.BODY;
                    break;
                }
            }
            updateText();
        });

    }

    private void updateText() {
        switch (stage) {
            case INTRO:
                // set the text to the intro
                if (intro == null || intro.isEmpty()) {
                    intro = "PLACEHOLDER INTRO";
                }
                text.setText(intro);
                break;
            case BODY:
                if (bodyIndex < body.size()) {
                    text.setText(body.get(bodyIndex));
                } else {
                    text.setText("You've done a lot of adventuring today. I think you're ready for the end of your quest!");
                }
                break;
            case ENDING:
                // set the text to the ending
                if (ending == null || ending.isEmpty()) {
                    ending = "PLACEHOLDER ENDING";
                }
                if (bodyIndex < 2) {
                    ending = "You haven't done much adventuring yet, You'll have to collect more artifacts before facing Balthazar.";
                } else {
                    ending = "You did it! You used your magical artifacts and skill as a warrior to slay the " +
                            "dragon Balthazar! You've saved the city of Nosidam and all its residents. " +
                            "May your name echo through the ages in songs and epics!";
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