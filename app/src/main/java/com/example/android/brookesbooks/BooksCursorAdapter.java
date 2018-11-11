package com.example.android.brookesbooks;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.brookesbooks.data.BooksContract.BookEntry;

public class BooksCursorAdapter extends CursorAdapter {
    /**
     * Constructs a new {@link BooksCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public BooksCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //inflate a list item using layout in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the book data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current book can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.name);
        TextView priceTextView = view.findViewById(R.id.price);
        final TextView quantityTextView = view.findViewById(R.id.quantity);
        Button saleButton = view.findViewById(R.id.sale_button);

        //Find the columns of attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);

        //extract properties from cursor
        String bookName = cursor.getString(nameColumnIndex);
        String bookPrice = cursor.getString(priceColumnIndex);
        String bookQuantity = cursor.getString(quantityColumnIndex);

        //populate fields with extracted properties
        nameTextView.setText(bookName);
        priceTextView.setText(bookPrice);
        quantityTextView.setText(bookQuantity);
        //Set a onClickListener for the sale button
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(quantityTextView.getText().toString());
                //reviewed lessons for Just Java app! If there are 0 books, the user cannot sell more
                if (currentQuantity == 0){
                    Toast.makeText(context,"There are no books to sell", Toast.LENGTH_SHORT).show();
                }else{
                    //If there are still books to sell, reduce the quantity by 1 each time
                    currentQuantity = currentQuantity - 1;
                    quantityTextView.setText(Integer.toString(currentQuantity));
                    long id = Integer.parseInt(quantityTextView.getText().toString());
                    Uri currentBook = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
                    ContentValues values = new ContentValues();
                   values.put(BookEntry.COLUMN_BOOK_QUANTITY,quantityTextView.getText().toString());
                    //update the database
                     context.getContentResolver().update(currentBook,
                            values,
                            null,
                            null);

                }
            }
        });
    }
}
