package com.cs407.errandadventures;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Destinations#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Destinations extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ListView listView;
    //public ArrayAdapter<String> adapter;
    ArrayList<Stop> toDo = new ArrayList<>();
    SQLiteDatabase database;
    String s;
    Context context;


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
        return inflater.inflate(R.layout.fragment_destinations, container, false);
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        SharedPreferences sp = context.getSharedPreferences("com.cs407.errandadventures", Context.MODE_PRIVATE);
        s = sp.getString("username", "");

        database = context.openOrCreateDatabase("toDo", Context.MODE_PRIVATE, null);


        Button add = v.findViewById(R.id.addDestination);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddStop.class);
                intent.putExtra("username", s);
                startActivity(intent);
            }
        });

        DBHelper helper = new DBHelper(database);


        ArrayList<String> display = new ArrayList<>();
        ArrayList<String> checkList = new ArrayList<>();

        toDo = helper.readList(s);
        for (Stop stop:toDo) {
            display.add(String.format("Task: %s \n Location: %s", stop.getTask(), stop.getLocation()));
            Log.i("info", stop.isChecked());
            checkList.add(stop.isChecked());
        }

        listView = v.findViewById(R.id.listView);
        CustomAdapter adapter = new CustomAdapter(context, display, checkList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new DoubleClickListener() {

            @Override
            public void onDoubleClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i("info", "double");
                Stop selected = toDo.get(position);
                helper.deleteNote(selected.getTask(), selected.getLocation());
                onViewCreated(v, null);
            }
            @Override
            public void onSingleClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView image = (ImageView) view.getTag();
                Stop selected = toDo.get(position);
                if(selected.isChecked().equals( "false")) {
                    sp.edit().putInt("completed", sp.getInt("completed", 0) + 1).apply();
                    helper.setCheck("true", selected.getTask(), selected.getLocation(),s);
                    image.setImageDrawable(getActivity().getDrawable(R.drawable.ic_check_foreground));
                    Toast.makeText(getActivity().getApplicationContext(),"Congratulations, you have finished " + toDo.get(position).getTask(),Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("info", "single not go through");
                    helper.setCheck("false", selected.getTask(), selected.getLocation(),s);
                    image.setImageDrawable(getActivity().getDrawable(R.drawable.ic_uncheck_foreground));
                }
                onViewCreated(v, null);

            }
        });
    }

}