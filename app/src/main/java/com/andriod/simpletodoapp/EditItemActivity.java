package com.andriod.simpletodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class EditItemActivity extends ActionBarActivity {

    private static final int RETURN_CODE = 143;
    private EditText editingItem;
    private EditText etPriority;
    private TextView tvDueDate;
    private ToDoStore store = new ToDoStore(this);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        editingItem = (EditText) findViewById(R.id.etEditThisItem);
        etPriority = (EditText) findViewById(R.id.etPriority);
        tvDueDate = (TextView) findViewById(R.id.tvDueDate);
        editingItem.setText(getIntent().getExtras().getString(SimpleTodoActivity.OLD_DESCRIPTION));
        etPriority.setText(getIntent().getExtras().getString(SimpleTodoActivity.OLD_PRIORITY));
        tvDueDate.setText(getIntent().getExtras().getString(SimpleTodoActivity.OLD_DUE_DATE));
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
        Bundle oldItemDetails = getIntent().getExtras();
        String oldDescription = oldItemDetails.getString(SimpleTodoActivity.OLD_DESCRIPTION);
        String updatedDescription = editingItem.getText().toString();
        int oldPriority = Integer.parseInt(oldItemDetails.getString(SimpleTodoActivity.OLD_PRIORITY));
        Date dueDate = null;
        try {
            dueDate = dateFormat.parse(tvDueDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int updatedPriority = Integer.parseInt(etPriority.getText().toString());
        Item oldItem = new Item(oldDescription, oldPriority, dueDate);
        Item newItem = new Item(updatedDescription, updatedPriority, dueDate);
        store.updateItem(oldItem, newItem);
        Intent data = new Intent();
        data.putExtra(SimpleTodoActivity.OLD_DESCRIPTION, oldDescription);
        data.putExtra(SimpleTodoActivity.OLD_PRIORITY, oldPriority);
        data.putExtra(SimpleTodoActivity.OLD_DUE_DATE, dueDate);
        data.putExtra(SimpleTodoActivity.NEW_DESCRIPTION, updatedDescription);
        data.putExtra(SimpleTodoActivity.NEW_PRIORITY, updatedPriority);
        data.putExtra(SimpleTodoActivity.NEW_DUE_DATE, dueDate);
        setResult(RETURN_CODE, data);
        finish();
    }
}
