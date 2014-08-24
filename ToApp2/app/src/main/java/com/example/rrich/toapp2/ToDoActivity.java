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
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        setupListViewClickListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.to_do, menu);
        return true;
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
        itemsAdapter.add(etNewItem.getText().toString());
        etNewItem.setText("");
        saveItems();
    }

    private void setupListViewListener() { //Command-Shift-O gets rid of the red line errors, prv.
        lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long rowId) {
                items.remove(position);
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
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
            e.printStackTrace();
        }
    }

    private void saveItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
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
                i.putExtra("value", items.get(position));
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String value = data.getExtras().getString("value");
            //Toast.makeText(this, data.getExtras().getString("value"), Toast.LENGTH_SHORT).show();
            int position = data.getExtras().getInt("position");
            items.set(position, value);
            itemsAdapter.notifyDataSetChanged();
            saveItems();
        }
    }

}
