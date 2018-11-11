package com.example.android.brookesbooks;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.brookesbooks.data.BooksContract.BookEntry;

public class
EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //EditText field for ISBN
    private EditText mIsbnEditText;

    //EditText field for name of book
    private EditText mNameEditText;

    //EditText field for price
    private EditText mPriceEditText;

    //EditText for Quantity
    private EditText mQuantityEditText;

    //EditText for Supplier name
    private EditText mSupplierNameEditText;

    //EditText for Supplier Phone number
    private EditText mSupplierPhoneEditText;

    //Identifier for book data loader
    private static final int EXISTING_BOOK_LOADER = 0;

    //Content URI for existing book(null if it is a new book)
    private Uri mCurrentBookUri;

    //Boolean flag that keeps track of whether the book has been edited (true) or not (false)
    private boolean mBookHasChanged = false;

    //OnTouchListener that listens for any touches on a view suggesting the user is making changes
    private View.OnTouchListener mTouchlistener = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //Get the intent and figure out which intent was used to launch the activity
        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        //Determine whether we are creating a book (no URI) or editing an existing book (has URI)
        if (mCurrentBookUri == null) {
            setTitle(R.string.add_book_title);
            //Invalidate the options menu so the "Delete" menu option is hidden
            invalidateOptionsMenu();
        } else {
            setTitle(R.string.edit_book_title);

            //Initialize the loader
            //noinspection deprecation
            getSupportLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mIsbnEditText = (EditText) findViewById(R.id.edit_isbn);
        mNameEditText = (EditText) findViewById(R.id.edit_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_quantity);
        mSupplierNameEditText = (EditText) findViewById(R.id.edit_supplier_name);
        mSupplierPhoneEditText = (EditText) findViewById(R.id.edit_supplier_phone_number);

        //Set up OnTouchListeners on all the input fields to see if they have been modified
        mIsbnEditText.setOnTouchListener(mTouchlistener);
        mNameEditText.setOnTouchListener(mTouchlistener);
        mPriceEditText.setOnTouchListener(mTouchlistener);
        mQuantityEditText.setOnTouchListener(mTouchlistener);
        mSupplierNameEditText.setOnTouchListener(mTouchlistener);
        mSupplierPhoneEditText.setOnTouchListener(mTouchlistener);
    }

    // Get user input from editor and save a book into database.
    private void saveBook() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String isbnString = mIsbnEditText.getText().toString().trim();
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneString = mSupplierPhoneEditText.getText().toString().trim();

        //Check to see if all the values are empty. If so, just return w/out saving a new book
        if (mCurrentBookUri == null &&
                TextUtils.isEmpty(isbnString)&&
                TextUtils.isEmpty(nameString)&&
                TextUtils.isEmpty(priceString)&&
                TextUtils.isEmpty(quantityString)&&
                TextUtils.isEmpty(supplierNameString)&&
                TextUtils.isEmpty(supplierPhoneString)){
            return;
        }

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_ISBN, isbnString);
        values.put(BookEntry.COLUMN_BOOK_NAME, nameString);
        int price = 0;
        if (!TextUtils.isEmpty(priceString)){
            price = Integer.parseInt(priceString);
        }
        values.put(BookEntry.COLUMN_BOOK_PRICE, price);
        int quantity = 0;
        if (!TextUtils.isEmpty(quantityString)){
            quantity = Integer.parseInt(quantityString);
        }
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierNameString);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, supplierPhoneString);

        //Determine whether to add a book or update an existing book
        if (mCurrentBookUri == null) {

            // Insert a new book into the provider & return content URI
            Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);


            // Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                // If the row ID is -1, then there was an error with insertion.
                Toast.makeText(this, R.string.error_saving_book, Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(this, "Book saved successfully: ", Toast.LENGTH_SHORT).show();
            }
        }else{
            //change existing book
            int rowsAffected = getContentResolver().update(mCurrentBookUri,
                    values,
                    null,
                    null);
            //Show a toast message showing if the update was successful
            if (rowsAffected == 0){
                Toast.makeText(this,"Update to book failed", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Book updated successflly.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        if (mCurrentBookUri == null){
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save book to database
                saveBook();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                //Responds to a click on the "Delete" menu option
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the book hasn't changed, continue navigating to the patent activity
                if (!mBookHasChanged){
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                //If there are unsaved changes, set up a dialog to warn the user
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //User clicked "Discard" button so navigate to the parent activity
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    //This method is called when the back button is pressed.
    @Override
    public void onBackPressed(){
        //if the book hasn't changed, continue handling the back button press
        if (!mBookHasChanged){
            super.onBackPressed();
            return;
        }
        //If there are unsaved changes, set up a dialog to warn the user.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //User clicked "Discard" button so close the current activity.
                        finish();
                    }
                };
        //Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        //The editor shows all attributes, so the projection must have all columns from the table
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_ISBN,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookEntry.COLUMN_BOOK_SUPPLIER_PHONE};

        //This loader executes the ContentProvider's query method on a background thread
        return new CursorLoader(this,
                mCurrentBookUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        //Exit if the cursor is null/there is <1 row in the curson
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        //Go ahead to the first row of the cursor & read data from it
        if (cursor.moveToFirst()) {
            //Find the columns of book attributes we're concerned with
            int isbnColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_ISBN);
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE);

            //Extract the value from the Cursor for the given column index
            String isbn = cursor.getString(isbnColumnIndex);
            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplier_name = cursor.getString(supplierNameColumnIndex);
            String supplier_phone = cursor.getString(supplierPhoneColumnIndex);

            //Update the views on the screen with the values from the database
            mIsbnEditText.setText(isbn);
            mNameEditText.setText(name);
            mPriceEditText.setText(Integer.toString(price));
            mQuantityEditText.setText(Integer.toString(quantity));
            mSupplierNameEditText.setText(supplier_name);
            mSupplierPhoneEditText.setText(supplier_phone);
        }
    }
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the book.
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        //If the loader is invalidated, clear the data from the input fields
        mIsbnEditText.setText("");
        mNameEditText.setText("");
        mPriceEditText.setText(0);
        mQuantityEditText.setText(0);
        mSupplierNameEditText.setText("");
        mSupplierPhoneEditText.setText("");
    }
    private void deleteBook() {
        // perform if there is an existing book
        if (mCurrentBookUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);

            if (rowsDeleted == 0) {

                Toast.makeText(this, getString(R.string.editor_delete_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
    //Show a dialog that warns the user that there are unsaved changes that will be lost if they leave the editor
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes and quit editing?");
        builder.setPositiveButton("Discard changes", discardButtonClickListener);
        builder.setNegativeButton("Continue editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null){
                    dialog.dismiss();
                }
            }
        });
        //Create and show the Alert Dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}