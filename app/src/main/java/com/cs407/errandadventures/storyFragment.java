package com.cs407.errandadventures;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_story, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // TODO: update the story based on the user's location.  Need to implement callback
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        LocationRequest request = new LocationRequest.Builder(1000).build(); // update every second

        // get the stops from the database
        Context context = getActivity().getApplicationContext();
        DBHelper helper = new DBHelper(context.openOrCreateDatabase("toDo", Context.MODE_PRIVATE, null));
        stops = helper.readList(context.getSharedPreferences("com.cs407.errandadventures", Context.MODE_PRIVATE).getString("username", ""));

        // if stops were added, generate body paragraphs
        if (stops.size() > 0 && (body == null || stops.size() > body.size())) {
            genBodyParagraph();
        }

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
                assert body != null;
                assert body.size() <= bodyIdx && bodyIdx >= 0;
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

    private void changeStage() {
        switch (stage) {
            case INTRO:
                stage = Stage.BODY;
                break;
            case BODY:
                stage = Stage.ENDING;
                break;
            case ENDING:
                stage = Stage.INTRO;
                break;
        }
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