package com.andriod.simpletodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class SimpleTodoActivity extends ActionBarActivity {

    private ListView lvItems;
    private ToDoStore store;
    private static final int EDIT_RETURN_CODE = 143;
    private static final int ADD_RETURN_CODE = 144;
    private ArrayList<Item> listOfItems;
    ToDoItemAdapter listAdapter;
    static final String OLD_DESCRIPTION = "old_description";
    static final String OLD_PRIORITY = "old_priority";
    static final String OLD_DUE_DATE = "old_due_date";
    static final String NEW_DESCRIPTION = "new_description";
    static final String NEW_PRIORITY = "new_priority";
    static final String NEW_DUE_DATE = "new_due_date";
    static final String ADD_DESCRIPTION = "description";
    static final String ADD_PRIORITY = "priority";
    static final String ADD_DUE_DATE = "duedate";
    static final String ADD_ITEM = "additem";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_todo);
        lvItems = (ListView) findViewById(R.id.lvItems);
        store = new ToDoStore(this);
        listOfItems = store.getAllItems();
        listAdapter = new ToDoItemAdapter(this, R.layout.complexrow, listOfItems);
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
        if (resultCode == EDIT_RETURN_CODE) {
            Date oldDate = (Date) data.getExtras().get(OLD_DUE_DATE);
            Item oldItem = new Item(data.getStringExtra(OLD_DESCRIPTION),
                    (Integer) data.getExtras().get(OLD_PRIORITY), oldDate);
            listOfItems.remove(oldItem);
            String description = data.getExtras().getString(NEW_DESCRIPTION);
            int priority = (Integer) data.getExtras().get(NEW_PRIORITY);
            Date dueDate = (Date) data.getExtras().get(NEW_DUE_DATE);
            listOfItems.add(new Item(description, priority, dueDate));
            listAdapter.notifyDataSetChanged();
        } else {
            if (resultCode == ADD_RETURN_CODE) {
                Date duedate = null;
                try {
                    duedate = dateFormat.parse(data.getStringExtra(SimpleTodoActivity.ADD_DUE_DATE));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Item addedItem = new Item(data.getStringExtra(SimpleTodoActivity.ADD_DESCRIPTION)
                        , Integer.parseInt(data.getStringExtra(SimpleTodoActivity.ADD_PRIORITY))
                        , duedate);
                listOfItems.add(addedItem);
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    public void addItem(View view) {
        Intent intent = new Intent(SimpleTodoActivity.this, AddItemActivity.class);
        startActivityForResult(intent, ADD_RETURN_CODE);
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                store.deleteItem((Item) lvItems.getItemAtPosition(position));
                listOfItems.remove(lvItems.getItemAtPosition(position));
                listAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void editListViewListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item editItem = (Item) lvItems.getItemAtPosition(i);
                listOfItems.remove(editItem);
                Bundle bundle = new Bundle();
                bundle.putString(OLD_DESCRIPTION, editItem.getDescription());
                bundle.putString(OLD_PRIORITY, String.valueOf(editItem.getPriority()));
                bundle.putString(OLD_DUE_DATE, dateFormat.format(editItem.getDuedate()));
                Intent intent = new Intent(SimpleTodoActivity.this, EditItemActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, EDIT_RETURN_CODE);
            }
        });
        lvItems.setLongClickable(true);
    }


}
