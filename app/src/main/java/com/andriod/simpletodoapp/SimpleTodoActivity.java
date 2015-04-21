package com.andriod.simpletodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class SimpleTodoActivity extends ActionBarActivity {

    private ListView lvItems;
    private EditText etAddItem;
    private ToDoOpenHelper dbHelper;
    private static final int RETURN_CODE = 143;
    ArrayList listOfItems;
    ArrayAdapter listAdapter;
    private static final String OLD_ITEM = "oldItem";
    private static final String NEW_ITEM = "newItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_todo);
        etAddItem = (EditText) findViewById(R.id.etAddItem);
        lvItems = (ListView) findViewById(R.id.lvItems);
        dbHelper = new ToDoOpenHelper(this);
        listOfItems = dbHelper.getAllItems();
        listAdapter = new ArrayAdapter(this, R.layout.simplerow, listOfItems);
        lvItems.setAdapter(listAdapter);
        setupListViewListener();
        editListViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simple_todo, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        listOfItems.remove(data.getExtras().getString(OLD_ITEM));
        listOfItems.add(data.getExtras().getString(NEW_ITEM));
        listAdapter.notifyDataSetChanged();
    }

    public void addItem(View view) {
        String item = etAddItem.getText().toString();
        if (checkForDuplicateItems(item)) {
            dbHelper.insertTodo(item);
            listOfItems.add(item);
            listAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, item + " already exists", Toast.LENGTH_LONG).show();
        }
        etAddItem.setText("");
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                dbHelper.deleteItem((String) lvItems.getItemAtPosition(i));
                listOfItems.remove(lvItems.getItemAtPosition(i));
                listAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void editListViewListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString(OLD_ITEM, String.valueOf(lvItems.getItemAtPosition(i)));
                Intent intent = new Intent(SimpleTodoActivity.this, EditItemActivity.class);
                intent.getStringExtra((String) lvItems.getItemAtPosition(i));
                intent.putExtras(bundle);
                startActivityForResult(intent, RETURN_CODE);
            }
        });
        lvItems.setLongClickable(true);
    }

    private boolean checkForDuplicateItems(String item) {
        return (dbHelper.getNumberOfRowsForAData(item) == 0);
    }
}
