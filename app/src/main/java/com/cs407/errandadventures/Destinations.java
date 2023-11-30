package com.cs407.errandadventures;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Destinations#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Destinations extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView listView;

    ArrayAdapter<String> adapter;
    ArrayList<String> displayList = new ArrayList<>();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Destinations() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Destinations.
     */
    // TODO: Rename and change types and number of parameters
    public static Destinations newInstance(String param1, String param2) {
        Destinations fragment = new Destinations();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen
        View view = inflater.inflate(R.layout.fragment_destinations, container, false);

        listView = view.findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1, displayList);
        System.out.println(displayList);
        listView.setAdapter(adapter);

        return view;
    }





    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Button addDest = view.findViewById(R.id.button);
        addDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText location = (EditText) getView().findViewById(R.id.location);
                String locationString = location.getText().toString();
                EditText story = (EditText) getView().findViewById(R.id.story);
                String storyString = story.getText().toString();
                System.out.println(locationString);
                displayList.add(String.format("Location:%s\nStory:%s\n", locationString, storyString));
                System.out.println(displayList);
                adapter.notifyDataSetChanged();


            }
        });

    }
/*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
     */
}