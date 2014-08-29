package com.example.rrich.toapp2;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import android.widget.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.view.View;
import android.content.Intent;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;


public class ToDoActivity extends ActionBarActivity {
    ArrayList<ToDoItem> toDos;
    ToDoAdapter itemsAdapter;
    ListView lvItems;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        lvItems = (ListView) findViewById(R.id.lvItems);

        readItems();

        // New ArrayAdapter Extension:
        itemsAdapter = new ToDoAdapter(this, android.R.layout.simple_list_item_1, toDos);

        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        setupListViewClickListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds itemsThenPriorities to the action bar if it is present.
        getMenuInflater().inflate(R.menu.to_do, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addToDoItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        ToDoItem newItemToAdd = new ToDoItem(etNewItem.getText().toString(), false); //right now defaults to low priority
        itemsAdapter.add(newItemToAdd);
        etNewItem.setText("");
        saveItems();
    }

    private void setupListViewListener() { //Command-Shift-O gets rid of the red line errors, prv.
        lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long rowId) {
                toDos.remove(position);
                itemsAdapter.notifyDataSetChanged(); //Change array data, so you need to notify the adapter, or change via adapter.
                saveItems();
                return true;
            }
        });
    }

    /*
     * Array List only lives in memory, app starts in the same data state always
     * Writing files to a disk and reading them back from that file, holds persistence.
     * */
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile  = new File(filesDir, "todo.txt");
        try {
            // First just read the file as we did before, then parse out the items.
            ArrayList<String> itemsThenPriorities = new ArrayList<String>(FileUtils.readLines(todoFile));
            toDos = buildListOfItems(itemsThenPriorities);
        } catch (IOException e) {
            // Empty List of ToDos
            toDos = new ArrayList<ToDoItem>();
            e.printStackTrace();
        }
    }

    private ArrayList<ToDoItem> buildListOfItems(ArrayList<String> fragment) {
        ArrayList<ToDoItem> items = new ArrayList<ToDoItem>();
        if (fragment != null && !fragment.isEmpty()) {
            // Builds the list of To Do Items:
            int numberOfItems = fragment.size() / 2;
            for(int i=0; i < numberOfItems; i++){
                String toDoText = fragment.get(i);
                String toDoPriority = fragment.get(i+numberOfItems);
                boolean toDoPriorityBoolean = false;
                if (toDoPriority.equals("1")) {
                    toDoPriorityBoolean = true;
                }
                ToDoItem newItem = new ToDoItem(toDoText, toDoPriorityBoolean);
                items.add(newItem);
            }
        }
        return items;
    }

    private void saveItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            // Convert toDos list of To Do Items back into an array formatted correctly for file.
            ArrayList<String> itemsThenPriorities = new ArrayList<String>();
            for (int i=0; i < toDos.size(); i++) {
                itemsThenPriorities.add(toDos.get(i).getToDoTask());
            }
            for (int i=0; i < toDos.size(); i++) {
                if (toDos.get(i).getHighPriority()) {
                    itemsThenPriorities.add("1");
                } else {
                    itemsThenPriorities.add("0");
                }
            }
            FileUtils.writeLines(todoFile, itemsThenPriorities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupListViewClickListener() { //Command-Shift-O gets rid of the red line errors, prv.
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(view.getContext(), EditItemActivity.class);
                i.putExtra("position", position);
                i.putExtra("value", toDos.get(position).getToDoTask());
                i.putExtra("priority", toDos.get(position).getHighPriority());
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String value = data.getExtras().getString("value");
            boolean priority = data.getExtras().getBoolean("priority");
            ToDoItem modifiedItem = new ToDoItem(value, priority); // For now all high priority
            int position = data.getExtras().getInt("position");
            toDos.set(position, modifiedItem);
            itemsAdapter.notifyDataSetChanged();
            saveItems();
        }
    }

}
