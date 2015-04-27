package com.andriod.simpletodoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ToDoItemAdapter extends ArrayAdapter<Item> {

    public ToDoItemAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Item todoItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.complexrow, parent, false);
        }

        // Lookup view for data population
        TextView tvItemDescription = (TextView) convertView.findViewById(R.id.tvItemDescription);
        TextView tvItemPriority = (TextView) convertView.findViewById(R.id.tvItemPriority);
        TextView tvItemDueDate = (TextView) convertView.findViewById(R.id.tvItemDueDate);

        // Populate the data into the template view using the data object
        tvItemDescription.setText(todoItem.getDescription());
        tvItemPriority.setText(Integer.toString(todoItem.getPriority()));
        tvItemDueDate.setText(todoItem.getDuedate().toString());

        // Return the completed view to render on screen
        return convertView;
    }
}
