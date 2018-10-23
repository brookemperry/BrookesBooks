package com.example.android.brookesbooks;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.android.brookesbooks.data.BooksContract.BookEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int BOOK_LOADER = 0;
    BooksCursorAdapter mCursorAdapter;

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
        ListView bookListView = findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);

        //Set up an Adapter to create a list item for each row of data in the Cursor.
        mCursorAdapter = new BooksCursorAdapter(this, null);
        bookListView.setAdapter(mCursorAdapter);

        //Start the loader
        //noinspection deprecation
        getSupportLoaderManager().initLoader(BOOK_LOADER, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // to insert harcoded data (hardcoded values need to be replaced when UI is added to get user input)
    private void insertBook() {
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_ISBN, getString(R.string.sample_isbn));
        values.put(BookEntry.COLUMN_BOOK_NAME, getString(R.string.sample_book_name));
        values.put(BookEntry.COLUMN_BOOK_PRICE, 1595);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, 5);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, getString(R.string.sample_supplier));
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, getString(R.string.sample_phone));

        //Insert a new row for Sag Harbor & get a new content URI
        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
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
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY};
        CursorLoader cursorLoader = new CursorLoader(this,
                BookEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        //Swap the new cursor in (The framework will close the loader when we return
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        //Swap the new cursor in (The framework will close the loader when we return
        mCursorAdapter.swapCursor(null);
    }
}
