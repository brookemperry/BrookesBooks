package com.example.android.brookesbooks;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.brookesbooks.data.BooksContract.BookEntry;
import com.example.android.brookesbooks.data.BooksDbHelper;

public class MainActivity extends AppCompatActivity {
    private BooksDbHelper mDbHelper;
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        //instantiate our subclass of SQLiteOpenHelper & pass in the context
        mDbHelper = new BooksDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {



        //Define a projection--the projection specifies which columny you use in the query
        String[] projection = {
              BookEntry._ID,
              BookEntry.COLUMN_BOOK_ISBN,
              BookEntry.COLUMN_BOOK_NAME,
              BookEntry.COLUMN_BOOK_PRICE,
              BookEntry.COLUMN_BOOK_QUANTITY,
              BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
              BookEntry.COLUMN_BOOK_SUPPLIER_PHONE };

        //Perform a query on the books table
        Cursor cursor = getContentResolver().query(
                BookEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

        TextView displayView = findViewById(R.id.display_text_view);

        try{


            //This finds the index of each column. You need this to iterate through each row
            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
            int isbnColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_ISBN);
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE);

            //Iterate through all the rows in the cursor (query performed)
            while (cursor.moveToNext()){
                int currentId = cursor.getInt(idColumnIndex);
                String currentIsbn = cursor.getString(isbnColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);

                //Display the the values of each row in the TextView
                displayView.append(("\n" + currentId + "-" +
                    currentIsbn + "-" +
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
    // to insert harcoded data (hardcoded values need to be replaced when UI is added to get user input)
    private void insertBook(){
        SQLiteDatabase db =mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_ISBN,getString(R.string.sample_isbn));
        values.put(BookEntry.COLUMN_BOOK_NAME,getString(R.string.sample_book_name));
        values.put (BookEntry.COLUMN_BOOK_PRICE, 1595);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, 5);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, getString(R.string.sample_supplier));
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, getString(R.string.sample_phone));

        long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);

        if(newRowId == -1){
            Log.d(LOG_TAG,"Problem inserting data");
        }else{
            Log.d(LOG_TAG,newRowId + "Rows inserted successfully");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertBook();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
