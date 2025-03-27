package com.kalpapower.qrmanager.database;

import android.provider.BaseColumns;

/**
 * Contract class for the Product table
 * Defines column names and SQL statements for creating and dropping the table
 */
public final class ProductContract {
    // Private constructor to prevent instantiation
    private ProductContract() {}
    
    // Inner class that defines the table contents
    public static class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_PRODUCT_NAME = "product_name";
        public static final String COLUMN_PRODUCT_CATEGORY = "product_category";
        public static final String COLUMN_SERIAL_NUMBER = "serial_number";
        public static final String COLUMN_MANUFACTURER_NAME = "manufacturer_name";
        public static final String COLUMN_MANUFACTURING_DATE = "manufacturing_date";
        public static final String COLUMN_VENDOR_NAME = "vendor_name";
        public static final String COLUMN_WARRANTY_PERIOD = "warranty_period";
        public static final String COLUMN_SELL_DATE = "sell_date";
        public static final String COLUMN_TECHNICAL_SPECS = "technical_specs";
        public static final String COLUMN_ADDITIONAL_NOTES = "additional_notes";
        public static final String COLUMN_QR_CODE_CREATED_DATE = "qr_code_created_date";
        public static final String COLUMN_LAST_SCANNED = "last_scanned";
    }
    
    // SQL statement to create the products table
    public static final String SQL_CREATE_PRODUCTS_TABLE =
            "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
            ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
            ProductEntry.COLUMN_PRODUCT_CATEGORY + " TEXT NOT NULL, " +
            ProductEntry.COLUMN_SERIAL_NUMBER + " TEXT UNIQUE NOT NULL, " +
            ProductEntry.COLUMN_MANUFACTURER_NAME + " TEXT NOT NULL, " +
            ProductEntry.COLUMN_MANUFACTURING_DATE + " TEXT NOT NULL, " +
            ProductEntry.COLUMN_VENDOR_NAME + " TEXT NOT NULL, " +
            ProductEntry.COLUMN_WARRANTY_PERIOD + " INTEGER NOT NULL, " +
            ProductEntry.COLUMN_SELL_DATE + " TEXT, " +
            ProductEntry.COLUMN_TECHNICAL_SPECS + " TEXT, " +
            ProductEntry.COLUMN_ADDITIONAL_NOTES + " TEXT, " +
            ProductEntry.COLUMN_QR_CODE_CREATED_DATE + " TEXT NOT NULL, " +
            ProductEntry.COLUMN_LAST_SCANNED + " TEXT);";
    
    // SQL statement to delete the products table
    public static final String SQL_DELETE_PRODUCTS_TABLE =
            "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME;
}
