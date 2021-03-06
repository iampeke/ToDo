package com.example.rrich.toapp2;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;


public class EditItemActivity extends ActionBarActivity {
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        position = getIntent().getIntExtra("position", 0);
        String value = getIntent().getStringExtra("value");
        boolean highPriority = getIntent().getBooleanExtra("priority", false);
        EditText et = (EditText) findViewById(R.id.editText);
        et.setText(value);
        et.setSelection(et.getText().length());
        et.requestFocus();
        CheckBox cb = (CheckBox) findViewById(R.id.priorityCheckBox);
        cb.setChecked(highPriority);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds itemsThenPriorities to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_item, menu);
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

    public void onSave(View v) {
        EditText et = (EditText) findViewById(R.id.editText);
        CheckBox cb = (CheckBox) findViewById(R.id.priorityCheckBox);
        Intent data = new Intent();
        data.putExtra("value", et.getText().toString());
        data.putExtra("position", position);
        data.putExtra("priority", cb.isChecked());
        setResult(RESULT_OK, data);
        finish();
    }
}
