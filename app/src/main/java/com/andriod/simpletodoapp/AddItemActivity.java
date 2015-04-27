package com.andriod.simpletodoapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AddItemActivity extends ActionBarActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private EditText etAddItem;
    private TextView tvDueDate;
    private TextView tvDueTime;
    private EditText priority;
    private ToDoStore store;
    private static final int ADD_RETURN_CODE = 144;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        etAddItem = (EditText) findViewById(R.id.etAddItemDescription);
        priority = (EditText) findViewById(R.id.etTodoItemPriority);
        tvDueDate = (TextView) findViewById(R.id.tvDueDate);
        tvDueTime = (TextView) findViewById(R.id.tvDueTime);
        store = new ToDoStore(this);
        Date currentDate = new Date();
        tvDueDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate));
        tvDueTime.setText(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(currentDate));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
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

    private boolean checkForDuplicateItems(Item item) {
        return (store.getNumberOfRowsForAData(item) == 0);
    }

    public void putItem(View view) {
        String description = etAddItem.getText().toString();
        int priorityNum = Integer.parseInt(priority.getText().toString());
        String date = tvDueDate.getText().toString();
        String time = tvDueTime.getText().toString();
        Date dueDate = null;
        try {
            dueDate = dateFormat.parse(date + " " + time + ":00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Item newItem = new Item(description, priorityNum, dueDate);
        if (checkForDuplicateItems(newItem)) {
            store.insertTodo(newItem);
            Intent data = new Intent();
            data.putExtra(SimpleTodoActivity.ADD_DESCRIPTION, description);
            data.putExtra(SimpleTodoActivity.ADD_PRIORITY, priority.getText().toString());
            data.putExtra(SimpleTodoActivity.ADD_DUE_DATE, dateFormat.format(dueDate));
            setResult(ADD_RETURN_CODE, data);
            finish();
        } else {
            Toast.makeText(this, description + " already exists", Toast.LENGTH_LONG).show();
        }
    }

    public void selectDate(View view) {
        DialogFragment dateFragment = new DatePickerDialogFragment();
        dateFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void selectTime(View view) {
        DialogFragment timeFragment = new TimePickerDialogFragment();
        timeFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
        tvDueTime.setText(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        tvDueDate.setText(year + "-" + (month + 1) + "-" + day);
    }
}
