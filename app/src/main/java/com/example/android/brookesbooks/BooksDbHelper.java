package com.example.android.brookesbooks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.brookesbooks.BooksContract.BookEntry;

public class BooksDbHelper extends SQLiteOpenHelper {

    //Name of the database file
    private static final String DATABASE_NAME = "store.db";

    //Database version--increment if you change the database schema
    private static final int DATABASE_VERSION = 1;

    public BooksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a string that contains the SQL statement to create the books table
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME+ " ("
                + BookEntry.COLUMN_BOOK_ISBN + " BIGINT PRIMARY KEY NOT NULL,"
                + BookEntry.COLUMN_BOOK_NAME + " TEXT NOT NULL,"
                + BookEntry.COLUMN_BOOK_PRICE + " INTEGER NOT NULL,"
                + BookEntry.COLUMN_BOOK_QUANTITY + " INTEGER NOT NULL DEFAULT 0,"
                + BookEntry.COLUMN_BOOK_SUPPLIER_NAME +" TEXT,"
                + BookEntry.COLUMN_BOOK_SUPPLIER_PHONE + " INTEGER);";

        //execute the SQL statement
        db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //todo ???
    }
}
