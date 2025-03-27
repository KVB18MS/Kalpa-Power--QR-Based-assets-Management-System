package com.kalpapower.qrmanager.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.kalpapower.qrmanager.model.Product;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Utility class for exporting data to various formats
 */
public class ExportUtils {
    private static final String TAG = "ExportUtils";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat TIMESTAMP_FORMAT = 
            new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
    
    /**
     * Export a list of products to a CSV file
     * @param context The application context
     * @param products The list of products to export
     * @return The file URI of the exported CSV, or null if export failed
     */
    public static Uri exportProductsToCSV(Context context, List<Product> products) {
        if (context == null || products == null || products.isEmpty()) {
            Log.e(TAG, "Invalid parameters for export");
            return null;
        }
        
        String timestamp = TIMESTAMP_FORMAT.format(new Date());
        String fileName = "kalpa_power_inventory_" + timestamp + ".csv";
        
        File exportDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "exports");
        if (!exportDir.exists() && !exportDir.mkdirs()) {
            Log.e(TAG, "Failed to create export directory");
            return null;
        }
        
        File csvFile = new File(exportDir, fileName);
        
        try {
            FileWriter writer = new FileWriter(csvFile);
            
            // Write CSV header
            writer.append("ID,Product Name,Category,Serial Number,Manufacturer,Manufacturing Date,")
                  .append("Vendor,Warranty (Years),Sell Date,Technical Specs,Notes,")
                  .append("QR Code Created,Last Scanned,Warranty Status\n");
            
            // Write product data
            for (Product product : products) {
                // Basic product info
                writer.append(String.valueOf(product.getId())).append(",");
                writer.append(escapeCsvField(product.getProductName())).append(",");
                writer.append(escapeCsvField(product.getProductCategory())).append(",");
                writer.append(escapeCsvField(product.getSerialNumber())).append(",");
                writer.append(escapeCsvField(product.getManufacturerName())).append(",");
                
                // Manufacturing date
                if (product.getManufacturingDate() != null) {
                    writer.append(DATE_FORMAT.format(product.getManufacturingDate()));
                }
                writer.append(",");
                
                // Additional fields
                writer.append(escapeCsvField(product.getVendorName())).append(",");
                writer.append(String.valueOf(product.getWarrantyPeriod())).append(",");
                
                // Sell date
                if (product.getSellDate() != null) {
                    writer.append(DATE_FORMAT.format(product.getSellDate()));
                }
                writer.append(",");
                
                // Technical specs and notes
                writer.append(escapeCsvField(product.getTechnicalSpecs())).append(",");
                writer.append(escapeCsvField(product.getAdditionalNotes())).append(",");
                
                // QR code created date
                if (product.getQrCodeCreatedDate() != null) {
                    writer.append(DATE_FORMAT.format(product.getQrCodeCreatedDate()));
                }
                writer.append(",");
                
                // Last scanned date
                if (product.getLastScanned() != null) {
                    writer.append(DATE_FORMAT.format(product.getLastScanned()));
                }
                writer.append(",");
                
                // Calculate warranty status
                boolean warrantyValid = isWarrantyValid(product);
                writer.append(warrantyValid ? "Active" : "Expired");
                
                // New line for next record
                writer.append("\n");
            }
            
            writer.flush();
            writer.close();
            
            Log.i(TAG, "CSV export successful: " + csvFile.getAbsolutePath());
            
            // Return the URI for the file
            return FileProvider.getUriForFile(
                    context,
                    "com.kalpapower.qrmanager.fileprovider",
                    csvFile
            );
            
        } catch (IOException e) {
            Log.e(TAG, "Error exporting products to CSV", e);
            return null;
        }
    }
    
    /**
     * Share the exported CSV file
     * @param context The application context
     * @param fileUri The URI of the file to share
     */
    public static void shareCSVFile(Context context, Uri fileUri) {
        if (context == null || fileUri == null) {
            return;
        }
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/csv");
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        
        // Start the share activity
        context.startActivity(Intent.createChooser(
                shareIntent,
                "Share Inventory Data"
        ));
    }
    
    /**
     * Check if a product is still under warranty
     * @param product The product to check
     * @return true if warranty is valid, false otherwise
     */
    private static boolean isWarrantyValid(Product product) {
        if (product.getManufacturingDate() == null || product.getWarrantyPeriod() <= 0) {
            return false;
        }
        
        // Calculate warranty end date
        java.util.Calendar warrantyEnd = java.util.Calendar.getInstance();
        warrantyEnd.setTime(product.getManufacturingDate());
        warrantyEnd.add(java.util.Calendar.YEAR, product.getWarrantyPeriod());
        
        // Compare with current date
        Date now = new Date();
        return now.before(warrantyEnd.getTime());
    }
    
    /**
     * Escape special characters in CSV fields
     * @param field The field to escape
     * @return The escaped field
     */
    private static String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        
        // If the field contains commas, quotes, or newlines, wrap it in quotes
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            // Double up any quotes
            field = field.replace("\"", "\"\"");
            // Wrap in quotes
            return "\"" + field + "\"";
        }
        
        return field;
    }
}