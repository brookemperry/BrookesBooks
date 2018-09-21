package com.example.android.brookesbooks;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.brookesbooks.BooksContract.BookEntry;
import  com.example.android.brookesbooks.BooksDbHelper;

public class MainActivity extends AppCompatActivity {
    private BooksDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate our subclass of SQLiteOpenHelper & pass in the context
        mDbHelper = new BooksDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //Define a projection--the projection specifies which columny you use in the query
        String[] projection = {
              BookEntry.COLUMN_BOOK_ISBN,
              BookEntry.COLUMN_BOOK_NAME,
              BookEntry.COLUMN_BOOK_PRICE,
              BookEntry.COLUMN_BOOK_QUANTITY,
              BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
              BookEntry.COLUMN_BOOK_SUPPLIER_PHONE };

        //Perform a query on the books table
        Cursor cursor = db.query(
                BookEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        TextView displayView = findViewById(R.id.display_text_view);

        try{
            displayView.append(BookEntry.COLUMN_BOOK_ISBN + " - " +
            BookEntry.COLUMN_BOOK_NAME + " - " +
            BookEntry.COLUMN_BOOK_PRICE + " - " +
            BookEntry.COLUMN_BOOK_QUANTITY + " - " +
            BookEntry.COLUMN_BOOK_SUPPLIER_NAME + " - " +
            BookEntry.COLUMN_BOOK_SUPPLIER_PHONE + "\n");

            //This finds the index of each column. You need this to itarate through each row
            int isbnColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_ISBN);
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE);

            //Iterate through all the rows in the cursor (query performed)
            while (cursor.moveToNext()){
                long currentIsbn = cursor.getLong(isbnColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                int currentSupplierPhone = cursor.getInt(supplierPhoneColumnIndex);

                //Display the the values of each row in the TextView
                displayView.append(("\n" + currentIsbn + "-" +
                    currentName + "-" +
                    currentPrice + "-" +
                    currentQuantity + "-" +
                    currentSupplierName + "-" +
                    currentSupplierPhone));
            }
        } finally {
            cursor.close();
        }
    }
    // to insert harcoded data (hardcoded values need to be replaced when UI is added)
    private void insertBook(){
        SQLiteDatabase db =mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_ISBN,9780307455161);
        values.put(BookEntry.COLUMN_BOOK_NAME,"Sag Harbor");
        values.put (BookEntry.COLUMN_BOOK_PRICE, 1595);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, 5);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, "Apex");
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, 4043735555);
    }

}
