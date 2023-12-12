package com.cs407.errandadventures;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {

    ArrayList<String> names;
    ArrayList<String> check;
    Context context;
    LayoutInflater inflter;
    String value;


    public CustomAdapter(Context context, ArrayList<String> names, ArrayList<String> check) {
        super(context, 0, names);
        this.context = context;
        this.names = names;
        inflter = (LayoutInflater.from(context));
        this.check = check;

    }


    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = inflter.inflate(R.layout.row, parent, false);
        }
        //Holder holder = new Holder();
        ImageView image = view.findViewById(R.id.imageView);

        if(check.get(position).equals("true")) {
            image.setImageDrawable(context.getDrawable(R.drawable.ic_check_foreground));
        } else {
            image.setImageDrawable(context.getDrawable(R.drawable.ic_uncheck_foreground));
        }

        TextView item = view.findViewById(R.id.item);
        item.setText(names.get(position));
        view.setTag(image);
        return view;
    }
    static class Holder {
        ImageView image;
        TextView item;
    }

}
