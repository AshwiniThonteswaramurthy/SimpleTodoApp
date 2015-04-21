package com.andriod.simpletodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class EditItemActivity extends ActionBarActivity {

    private static final String OLD_ITEM = "oldItem";
    private static final String NEW_ITEM = "newItem";
    private static final int RETURN_CODE = 143;
    private EditText editingItem;
    private ToDoOpenHelper dbHelper = new ToDoOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        editingItem = (EditText) findViewById(R.id.etEditThisItem);
        editingItem.setText(getIntent().getExtras().getString(OLD_ITEM));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateItem(View view) {
        String oldValue = getIntent().getStringExtra(OLD_ITEM);
        String updatedValue = editingItem.getText().toString();
        dbHelper.updateItem(oldValue, updatedValue);
        Toast.makeText(this, "Item updated", Toast.LENGTH_LONG).show();
        Intent data = new Intent();
        data.putExtra(OLD_ITEM, oldValue);
        data.putExtra(NEW_ITEM, updatedValue);
        setResult(RETURN_CODE, data);
        finish();
    }
}
