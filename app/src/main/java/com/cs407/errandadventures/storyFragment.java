package com.cs407.errandadventures;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
    // ending paragraph
    private String ending;

    // the text displayed to the user
    private TextView text;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // generate new text if we haven't already and we need it
        switch (stage) {
            case INTRO:
                // intro will be generated here
                if (intro == null || intro.isEmpty()) {
                    intro = "PLACEHOLDER INTRO";
                }
                break;
            case BODY:
                // body will be generated here
                if (body == null) {
                    body = new ArrayList<>();
                    body.add("PLACEHOLDER BODY 1");
                    body.add("PLACEHOLDER BODY 2");
                    body.add("PLACEHOLDER BODY 3");
                }
                break;
            case ENDING:
                // ending will be generated here
                if (ending == null || ending.isEmpty()) {
                    ending = "PLACEHOLDER ENDING";
                }
                break;
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        text = view.findViewById(R.id.storyText);
        // set the text of the textview to the appropriate text
        switch (stage) {
            case INTRO:
                // set the text to the intro
                text.setText(intro);
                break;
            case BODY:
                // set the text to the body
                text.setText(body.get(0));
                break;
            case ENDING:
                // set the text to the ending
                text.setText(ending);
                break;
        }
    }

}