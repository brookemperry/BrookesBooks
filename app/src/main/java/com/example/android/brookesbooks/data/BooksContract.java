package com.example.android.brookesbooks.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.content.ContentProvider;

public final class BooksContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.brookesbooks";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BOOKS = "books";

    //Contractor is purposely left empty. It serves to prevent someone from instantiating the contract class
    private BooksContract() {
    }

    //This Inner class defines the contant values for the books database table.
    public static final class BookEntry implements BaseColumns {

        //The content URI to access the book data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);


        // The MIME type of the {@link #CONTENT_URI} for a list of books.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;


        //The MIME type of the {@link #CONTENT_URI} for a single book.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public final static String TABLE_NAME = "books";

        public final static String _ID = BaseColumns._ID;

        //An ISBN number is a unique 13 digit identifier for all commercially printed books. Type I
        public final static String COLUMN_BOOK_ISBN = "isbn";

        //Name of book. Type=TEXT
        public final static String COLUMN_BOOK_NAME = "name";

        //Price of book. Type = INTEGER. $1.00 will be stored as 100--you have to multiply user input
        //by 100 to store and divide by 100 to display the price in the ui
        public final static String COLUMN_BOOK_PRICE = "price";

        //quantity of the particular book in stock. Type = INTEGER
        public final static String COLUMN_BOOK_QUANTITY = "quantity";

        //Name of the book supplier Type = TEXT
        public final static String COLUMN_BOOK_SUPPLIER_NAME = "supplier_name";

        //Phone number for the book supplier. Type = INTEGER
        public final static String COLUMN_BOOK_SUPPLIER_PHONE = "supplier_phone";

    }

}
