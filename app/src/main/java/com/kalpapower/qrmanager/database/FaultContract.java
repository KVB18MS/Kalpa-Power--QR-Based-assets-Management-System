package com.kalpapower.qrmanager.database;

import android.provider.BaseColumns;

/**
 * Contract class for the Fault table
 * Defines column names and SQL statements for creating and dropping the table
 */
public final class FaultContract {
    // Private constructor to prevent instantiation
    private FaultContract() {}
    
    // Inner class that defines the table contents
    public static class FaultEntry implements BaseColumns {
        public static final String TABLE_NAME = "faults";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_FAULT_DESCRIPTION = "fault_description";
        public static final String COLUMN_FAULT_DATE = "fault_date";
        public static final String COLUMN_SEVERITY = "severity";
        public static final String COLUMN_TECHNICIAN_NAME = "technician_name";
        public static final String COLUMN_REPORT_DATE = "report_date";
    }
    
    // SQL statement to create the faults table
    public static final String SQL_CREATE_FAULTS_TABLE =
            "CREATE TABLE " + FaultEntry.TABLE_NAME + " (" +
            FaultEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FaultEntry.COLUMN_PRODUCT_ID + " INTEGER NOT NULL, " +
            FaultEntry.COLUMN_FAULT_DESCRIPTION + " TEXT NOT NULL, " +
            FaultEntry.COLUMN_FAULT_DATE + " TEXT NOT NULL, " +
            FaultEntry.COLUMN_SEVERITY + " TEXT NOT NULL, " +
            FaultEntry.COLUMN_TECHNICIAN_NAME + " TEXT NOT NULL, " +
            FaultEntry.COLUMN_REPORT_DATE + " TEXT NOT NULL, " +
            "FOREIGN KEY (" + FaultEntry.COLUMN_PRODUCT_ID + ") REFERENCES " +
            ProductContract.ProductEntry.TABLE_NAME + "(" + ProductContract.ProductEntry._ID + "));";
    
    // SQL statement to delete the faults table
    public static final String SQL_DELETE_FAULTS_TABLE =
            "DROP TABLE IF EXISTS " + FaultEntry.TABLE_NAME;
}
