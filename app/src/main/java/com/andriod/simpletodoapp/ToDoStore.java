package com.andriod.simpletodoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ToDoStore extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todo.db.v2";
    private static final int DATABASE_VERSION = 2;
    private static final String TODO_TABLE_NAME = "todo";
    private static final String ITEM_COLUMN_DESCRIPTION = "description";
    private static final String ITEM_COLUMN_PRIORITY = "priority";
    private static final String ITEM_COLUMN_DUE_DATE = "duedate";
    private static final String ITEM_COLUMN_ID = "_id";
    private static final String TODO_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TODO_TABLE_NAME + " (" +
                    ITEM_COLUMN_ID + " integer primary key autoincrement, " +
                    ITEM_COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                    ITEM_COLUMN_PRIORITY + " integer,  " +
                    ITEM_COLUMN_DUE_DATE + " date );";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public ToDoStore(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TODO_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public boolean insertTodo(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_COLUMN_DESCRIPTION, item.getDescription());
        contentValues.put(ITEM_COLUMN_PRIORITY, item.getPriority());
        contentValues.put(ITEM_COLUMN_DUE_DATE, dateFormat.format(item.getDuedate()));
        db.insert(TODO_TABLE_NAME, null, contentValues);
        return true;
    }

    public int getNumberOfRowsForAData(Item item) {
        SQLiteDatabase db = this.getReadableDatabase();
        String clause = ITEM_COLUMN_DESCRIPTION + " =\"" + item.getDescription() + "\"" + " and "
                + ITEM_COLUMN_PRIORITY + " =\"" + item.getPriority() + "\"" + " and "
                + ITEM_COLUMN_DUE_DATE + " =\'" + item.getDuedate() + "\';";
        return (int) DatabaseUtils.queryNumEntries(db, TODO_TABLE_NAME, clause);
    }

    public Integer deleteItem(Item itemName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TODO_TABLE_NAME,
                ITEM_COLUMN_DESCRIPTION + " = ? and " + ITEM_COLUMN_PRIORITY + " = ? and " + ITEM_COLUMN_DUE_DATE + " = ?",
                new String[]{itemName.getDescription(), String.valueOf(itemName.getPriority()), dateFormat.format(itemName.getDuedate())});
    }

    public ArrayList<Item> getAllItems() {
        ArrayList<Item> allItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TODO_TABLE_NAME, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            int id = res.getInt(res.getColumnIndex(ITEM_COLUMN_ID));
            String description = res.getString(res.getColumnIndex(ITEM_COLUMN_DESCRIPTION));
            int priority = res.getInt(res.getColumnIndex(ITEM_COLUMN_PRIORITY));
            Date duedate = null;
            try {
                duedate = dateFormat.parse(res.getString(res.getColumnIndex(ITEM_COLUMN_DUE_DATE)));
            } catch (ParseException exception) {

            }
            allItems.add(new Item(id, description, priority, duedate));
            res.moveToNext();
        }
        return allItems;
    }

    public int updateItem(Item oldItem, Item newItem) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_COLUMN_DESCRIPTION, newItem.getDescription());
        contentValues.put(ITEM_COLUMN_PRIORITY, newItem.getPriority());
        String clause = ITEM_COLUMN_DESCRIPTION + " =\"" + oldItem.getDescription() + "\"" + " and "
                + ITEM_COLUMN_PRIORITY + " =\"" + oldItem.getPriority() + "\"" + " and "
                + ITEM_COLUMN_DUE_DATE + " =\'" + oldItem.getDuedate() + "\';";
        return db.update(TODO_TABLE_NAME, contentValues, clause, null);
    }
}
