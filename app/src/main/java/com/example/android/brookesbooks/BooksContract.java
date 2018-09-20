package com.example.android.brookesbooks;

import android.provider.BaseColumns;

public final class BooksContract {

    //Contractor is purposely left empty. It serves to prevent someone from instantiating the contract class
    private BooksContract(){}

    //This Inner class defines the contant values for the books database table.
    public static final class BookEntry implements BaseColumns{

        public final static String TABLE_NAME = "books";

        //An ISBN number is a unique 13 digit identifier for all commercially printed books. Type INTEGER
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
