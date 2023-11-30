package com.cs407.errandadventures;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
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

    private ListView listView;
    public ArrayAdapter<String> adapter;
    ArrayList<Stop> toDo = new ArrayList<>();

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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen
        return inflater.inflate(R.layout.fragment_destinations, container, false);
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {

        Context context = getActivity().getApplicationContext();
        SharedPreferences sp = context.getSharedPreferences("com.cs407.errandadventures", Context.MODE_PRIVATE);
        String s = sp.getString("username", "");

        Button add = v.findViewById(R.id.addDestination);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddStop.class);
                intent.putExtra("username", s);
                startActivity(intent);
            }
        });


        SQLiteDatabase database = context.openOrCreateDatabase("toDo", Context.MODE_PRIVATE, null);
        DBHelper helper = new DBHelper(database);

        ArrayList<String> display = new ArrayList<>();

        toDo = helper.readList(s);
        for (Stop stop:toDo) {
            display.add(String.format("%s", stop.getTask()));
        }


        listView = v.findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_checked, display);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView v = (CheckedTextView) view;
                v.setChecked(!v.isChecked());
            }
        });


    }


}