package com.example.mali.contextualactionbarexample;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.TreeSet;

public class ListAdapter extends ArrayAdapter<String> {

    private Context context;
    private TreeSet<Integer> selectedItemsPosition;

    public ListAdapter(Context context, int resource, int textViewResourceId, ArrayList<String> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView != null) {
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageView.setColorFilter(null);
        }

        View view = super.getView(position, convertView, parent);

        if (selectedItemsPosition != null && selectedItemsPosition.contains(position)) {
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            imageView.setColorFilter(
                    ContextCompat.getColor(context, R.color.colorAccent),
                    android.graphics.PorterDuff.Mode.MULTIPLY
            );
        }

        return view;
    }

    public void setCheckedItems(TreeSet<Integer> selectedPositions) {
        this.selectedItemsPosition = selectedPositions;
    }
}