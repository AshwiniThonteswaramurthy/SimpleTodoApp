package com.andriod.simpletodoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ToDoOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TODO_TABLE_NAME = "todo";
    private static final String ITEM_COLUMN_NAME = "item";
    private static final String ITEM_COLUMN_ID = "_id";
    private static final String TODO_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TODO_TABLE_NAME + " (" +
                    ITEM_COLUMN_ID + " integer primary key autoincrement, " + ITEM_COLUMN_NAME + " TEXT NOT NULL);";

    public ToDoOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TODO_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public boolean insertTodo(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_COLUMN_NAME, item);
        db.insert(TODO_TABLE_NAME, null, contentValues);
        return true;
    }

    public int getNumberOfRowsForAData(String item) {
        SQLiteDatabase db = this.getReadableDatabase();
        String clause = ITEM_COLUMN_NAME + " =\"" + item + "\"";
        return (int) DatabaseUtils.queryNumEntries(db, TODO_TABLE_NAME, clause);
    }

    public Integer deleteItem(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TODO_TABLE_NAME,
                ITEM_COLUMN_NAME + " = ? ",
                new String[]{item});
    }

    public ArrayList getAllItems() {
        ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TODO_TABLE_NAME, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(ITEM_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public int updateItem(String oldItem, String newItem) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_COLUMN_NAME, newItem);
        String clause = ITEM_COLUMN_NAME + " = \"" + oldItem + "\"";
        return db.update(TODO_TABLE_NAME, contentValues, clause, null);
    }
}
