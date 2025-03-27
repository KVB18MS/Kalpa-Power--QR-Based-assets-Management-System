package com.kalpapower.qrmanager.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.Result;
import com.kalpapower.qrmanager.database.DatabaseHelper;
import com.kalpapower.qrmanager.model.Product;

import org.json.JSONException;

/**
 * Enhanced utility class for QR code scanning functionality
 */
public class QRCodeScanner {
    private static final String TAG = "QRCodeScanner";
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    public static final int STORAGE_PERMISSION_REQUEST_CODE = 101;
    
    /**
     * Scan result listener interface
     */
    public interface ScanResultListener {
        void onScanSuccess(Product product);
        void onScanError(String errorMessage);
    }
    
    /**
     * Check if camera permission is granted
     * @param context The application context
     * @return true if permission is granted, false otherwise
     */
    public static boolean hasCameraPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == 
                PackageManager.PERMISSION_GRANTED;
    }
    
    /**
     * Check if storage permission is granted (only needed on Android < 10)
     * @param context The application context
     * @return true if permission is granted or not needed, false otherwise
     */
    public static boolean hasStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ uses scoped storage, no need for this permission
            return true;
        }
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == 
                PackageManager.PERMISSION_GRANTED;
    }
    
    /**
     * Request camera permission
     * @param activity The activity requesting permission
     */
    public static void requestCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST_CODE
        );
    }
    
    /**
     * Request storage permission (only needed on Android < 10)
     * @param activity The activity requesting permission
     */
    public static void requestStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ uses scoped storage, no need for this permission
            return;
        }
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_REQUEST_CODE
        );
    }
    
    /**
     * Check if the device has a camera
     * @param context The application context
     * @return true if a camera is available, false otherwise
     */
    public static boolean deviceHasCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }
    
    /**
     * Process QR code scan result and update product's last scanned timestamp
     * @param result The scan result from ZXing library
     * @param context The application context for database access
     * @param listener Callback for scan results
     */
    public static void processQRCodeResult(@NonNull Result result, Context context, ScanResultListener listener) {
        if (result.getText() == null || result.getText().isEmpty()) {
            if (listener != null) {
                listener.onScanError("QR code contains no data");
            }
            return;
        }
        
        try {
            // Parse product data from QR code
            Product product = Product.fromJSON(result.getText());
            
            if (product == null) {
                if (listener != null) {
                    listener.onScanError("Could not parse product data from QR code");
                }
                return;
            }
            
            // Check if this product exists in database by serial number
            if (context != null) {
                DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
                
                // Look up the product in the database
                Product dbProduct = dbHelper.getProductBySerialNumber(product.getSerialNumber());
                
                if (dbProduct != null) {
                    // Use the product from the database (with full data including ID)
                    product = dbProduct;
                    
                    // Update the last scanned timestamp
                    dbHelper.updateProductLastScanned(product.getSerialNumber());
                    Log.i(TAG, "Updated last scanned timestamp for product " + product.getSerialNumber());
                } else {
                    Log.w(TAG, "Scanned product not found in database: " + product.getSerialNumber());
                }
            }
            
            // Notify listener of success
            if (listener != null) {
                listener.onScanSuccess(product);
            }
            
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing QR code data", e);
            if (listener != null) {
                listener.onScanError("Invalid QR code format: " + e.getMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error processing QR code", e);
            if (listener != null) {
                listener.onScanError("Error processing QR code: " + e.getMessage());
            }
        }
    }
    
    /**
     * Backwards compatibility method
     * @param result The scan result
     * @return The parsed product or null if parsing failed
     */
    public static Product processQRCodeResult(Result result) {
        if (result == null || result.getText() == null) {
            return null;
        }
        
        try {
            return Product.fromJSON(result.getText());
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing QR code data", e);
            return null;
        }
    }
}
