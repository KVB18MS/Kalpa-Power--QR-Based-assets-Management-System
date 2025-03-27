package com.kalpapower.qrmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kalpapower.qrmanager.model.Fault;
import com.kalpapower.qrmanager.model.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * SQLite Database helper class to manage product and fault databases
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "KalpaPower.db";
    private static final int DATABASE_VERSION = 1;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    private static DatabaseHelper instance;

    // Private constructor to enforce singleton pattern
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Singleton getter
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Product and Fault tables
        db.execSQL(ProductContract.SQL_CREATE_PRODUCTS_TABLE);
        db.execSQL(FaultContract.SQL_CREATE_FAULTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables and recreate them
        db.execSQL(FaultContract.SQL_DELETE_FAULTS_TABLE);
        db.execSQL(ProductContract.SQL_DELETE_PRODUCTS_TABLE);
        onCreate(db);
    }

    /**
     * Add a new product to the database
     */
    public long addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, product.getProductName());
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_CATEGORY, product.getProductCategory());
        values.put(ProductContract.ProductEntry.COLUMN_SERIAL_NUMBER, product.getSerialNumber());
        values.put(ProductContract.ProductEntry.COLUMN_MANUFACTURER_NAME, product.getManufacturerName());
        values.put(ProductContract.ProductEntry.COLUMN_MANUFACTURING_DATE, DATE_FORMAT.format(product.getManufacturingDate()));
        values.put(ProductContract.ProductEntry.COLUMN_VENDOR_NAME, product.getVendorName());
        values.put(ProductContract.ProductEntry.COLUMN_WARRANTY_PERIOD, product.getWarrantyPeriod());
        
        if (product.getSellDate() != null) {
            values.put(ProductContract.ProductEntry.COLUMN_SELL_DATE, DATE_FORMAT.format(product.getSellDate()));
        }
        
        values.put(ProductContract.ProductEntry.COLUMN_TECHNICAL_SPECS, product.getTechnicalSpecs());
        values.put(ProductContract.ProductEntry.COLUMN_ADDITIONAL_NOTES, product.getAdditionalNotes());
        values.put(ProductContract.ProductEntry.COLUMN_QR_CODE_CREATED_DATE, TIMESTAMP_FORMAT.format(new Date()));

        long id = db.insert(ProductContract.ProductEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    /**
     * Get a product by serial number
     */
    public Product getProductBySerialNumber(String serialNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(
                ProductContract.ProductEntry.TABLE_NAME,
                null,
                ProductContract.ProductEntry.COLUMN_SERIAL_NUMBER + " = ?",
                new String[]{serialNumber},
                null,
                null,
                null
        );
        
        Product product = null;
        
        if (cursor != null && cursor.moveToFirst()) {
            product = cursorToProduct(cursor);
            cursor.close();
        }
        
        db.close();
        return product;
    }

    /**
     * Get a product by ID
     */
    public Product getProductById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(
                ProductContract.ProductEntry.TABLE_NAME,
                null,
                ProductContract.ProductEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
        
        Product product = null;
        
        if (cursor != null && cursor.moveToFirst()) {
            product = cursorToProduct(cursor);
            cursor.close();
        }
        
        db.close();
        return product;
    }

    /**
     * Get all products
     */
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        
        String selectQuery = "SELECT * FROM " + ProductContract.ProductEntry.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Product product = cursorToProduct(cursor);
                productList.add(product);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return productList;
    }

    /**
     * Get products by category
     */
    public List<Product> getProductsByCategory(String category) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(
                ProductContract.ProductEntry.TABLE_NAME,
                null,
                ProductContract.ProductEntry.COLUMN_PRODUCT_CATEGORY + " = ?",
                new String[]{category},
                null,
                null,
                null
        );
        
        if (cursor.moveToFirst()) {
            do {
                Product product = cursorToProduct(cursor);
                productList.add(product);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return productList;
    }

    /**
     * Update product's last scanned timestamp
     */
    public void updateProductLastScanned(String serialNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(ProductContract.ProductEntry.COLUMN_LAST_SCANNED, TIMESTAMP_FORMAT.format(new Date()));
        
        db.update(
                ProductContract.ProductEntry.TABLE_NAME,
                values,
                ProductContract.ProductEntry.COLUMN_SERIAL_NUMBER + " = ?",
                new String[]{serialNumber}
        );
        
        db.close();
    }

    /**
     * Add a fault report to the database
     */
    public long addFault(Fault fault) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(FaultContract.FaultEntry.COLUMN_PRODUCT_ID, fault.getProductId());
        values.put(FaultContract.FaultEntry.COLUMN_FAULT_DESCRIPTION, fault.getFaultDescription());
        values.put(FaultContract.FaultEntry.COLUMN_FAULT_DATE, DATE_FORMAT.format(fault.getFaultDate()));
        values.put(FaultContract.FaultEntry.COLUMN_SEVERITY, fault.getSeverity());
        values.put(FaultContract.FaultEntry.COLUMN_TECHNICIAN_NAME, fault.getTechnicianName());
        values.put(FaultContract.FaultEntry.COLUMN_REPORT_DATE, TIMESTAMP_FORMAT.format(new Date()));
        
        long id = db.insert(FaultContract.FaultEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    /**
     * Get all faults for a specific product
     */
    public List<Fault> getFaultsForProduct(long productId) {
        List<Fault> faultList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(
                FaultContract.FaultEntry.TABLE_NAME,
                null,
                FaultContract.FaultEntry.COLUMN_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(productId)},
                null,
                null,
                FaultContract.FaultEntry.COLUMN_REPORT_DATE + " DESC"
        );
        
        if (cursor.moveToFirst()) {
            do {
                Fault fault = cursorToFault(cursor);
                faultList.add(fault);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return faultList;
    }

    /**
     * Convert cursor to Product object
     */
    private Product cursorToProduct(Cursor cursor) {
        Product product = new Product();
        
        product.setId(cursor.getLong(cursor.getColumnIndex(ProductContract.ProductEntry._ID)));
        product.setProductName(cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME)));
        product.setProductCategory(cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_CATEGORY)));
        product.setSerialNumber(cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_SERIAL_NUMBER)));
        product.setManufacturerName(cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_MANUFACTURER_NAME)));
        product.setVendorName(cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_VENDOR_NAME)));
        product.setWarrantyPeriod(cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_WARRANTY_PERIOD)));
        product.setTechnicalSpecs(cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_TECHNICAL_SPECS)));
        product.setAdditionalNotes(cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_ADDITIONAL_NOTES)));
        
        try {
            String manufacturingDateStr = cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_MANUFACTURING_DATE));
            if (manufacturingDateStr != null && !manufacturingDateStr.isEmpty()) {
                product.setManufacturingDate(DATE_FORMAT.parse(manufacturingDateStr));
            }
            
            String sellDateStr = cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_SELL_DATE));
            if (sellDateStr != null && !sellDateStr.isEmpty()) {
                product.setSellDate(DATE_FORMAT.parse(sellDateStr));
            }
            
            String qrCreatedDateStr = cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_QR_CODE_CREATED_DATE));
            if (qrCreatedDateStr != null && !qrCreatedDateStr.isEmpty()) {
                product.setQrCodeCreatedDate(TIMESTAMP_FORMAT.parse(qrCreatedDateStr));
            }
            
            String lastScannedStr = cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_LAST_SCANNED));
            if (lastScannedStr != null && !lastScannedStr.isEmpty()) {
                product.setLastScanned(TIMESTAMP_FORMAT.parse(lastScannedStr));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return product;
    }

    /**
     * Convert cursor to Fault object
     */
    private Fault cursorToFault(Cursor cursor) {
        Fault fault = new Fault();
        
        fault.setId(cursor.getLong(cursor.getColumnIndex(FaultContract.FaultEntry._ID)));
        fault.setProductId(cursor.getLong(cursor.getColumnIndex(FaultContract.FaultEntry.COLUMN_PRODUCT_ID)));
        fault.setFaultDescription(cursor.getString(cursor.getColumnIndex(FaultContract.FaultEntry.COLUMN_FAULT_DESCRIPTION)));
        fault.setSeverity(cursor.getString(cursor.getColumnIndex(FaultContract.FaultEntry.COLUMN_SEVERITY)));
        fault.setTechnicianName(cursor.getString(cursor.getColumnIndex(FaultContract.FaultEntry.COLUMN_TECHNICIAN_NAME)));
        
        try {
            String faultDateStr = cursor.getString(cursor.getColumnIndex(FaultContract.FaultEntry.COLUMN_FAULT_DATE));
            if (faultDateStr != null && !faultDateStr.isEmpty()) {
                fault.setFaultDate(DATE_FORMAT.parse(faultDateStr));
            }
            
            String reportDateStr = cursor.getString(cursor.getColumnIndex(FaultContract.FaultEntry.COLUMN_REPORT_DATE));
            if (reportDateStr != null && !reportDateStr.isEmpty()) {
                fault.setReportDate(TIMESTAMP_FORMAT.parse(reportDateStr));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return fault;
    }

    /**
     * Check if warranty is still valid
     */
    public boolean isWarrantyValid(Product product) {
        if (product.getManufacturingDate() == null || product.getWarrantyPeriod() <= 0) {
            return false;
        }
        
        Calendar warrantyEnd = Calendar.getInstance();
        warrantyEnd.setTime(product.getManufacturingDate());
        warrantyEnd.add(Calendar.YEAR, product.getWarrantyPeriod());
        
        Date now = new Date();
        return now.before(warrantyEnd.getTime());
    }

    /**
     * Get warranty end date
     */
    public Date getWarrantyEndDate(Product product) {
        if (product.getManufacturingDate() == null || product.getWarrantyPeriod() <= 0) {
            return null;
        }
        
        Calendar warrantyEnd = Calendar.getInstance();
        warrantyEnd.setTime(product.getManufacturingDate());
        warrantyEnd.add(Calendar.YEAR, product.getWarrantyPeriod());
        
        return warrantyEnd.getTime();
    }
}
