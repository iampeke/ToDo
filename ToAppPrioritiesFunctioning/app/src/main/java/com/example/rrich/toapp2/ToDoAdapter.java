package com.example.rrich.toapp2;

import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

/**
 * Created by rrich on 8/28/14.
 */
public class ToDoAdapter extends ArrayAdapter<ToDoItem> {

    private ArrayList<ToDoItem> objects;

    public ToDoAdapter (Context context, int textViewResourceId, ArrayList<ToDoItem> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.to_do_item, parent, false);
        }

        ToDoItem currentItem = objects.get(position);

        TextView item = (TextView) convertView.findViewById(R.id.item);
        TextView priority = (TextView) convertView.findViewById(R.id.priority);

        item.setText(currentItem.getToDoTask());
        if (currentItem.getHighPriority()){
            priority.setText("HIGH");
        } else {
            priority.setText("LOW");
        }

        return convertView;
    }

}
