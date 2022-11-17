package com.example.logbook.dbcontext.contract;

import android.provider.BaseColumns;

public final class PictureContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private PictureContract() {}

    /* Inner class that defines the table contents */
    public static class PictureEntry implements BaseColumns {
        public static final String TABLE_NAME = "picture";
        public static final String COLUMN_NAME_URL = "url";
    }
}
