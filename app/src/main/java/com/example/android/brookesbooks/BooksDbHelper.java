package com.example.android.brookesbooks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BooksDbHelper extends SQLiteOpenHelper {

    //Name of the database file
    private static final String DATABASE_NAME = "store.db";

    //Database version--increment if you change the database schema
    private static final int DATABASE_VERSION = 1;

    public BooksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //TODO create a string that contains the SQL statement to create the books table
        //todo execute the SQL statement
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //todo ???
    }
}
