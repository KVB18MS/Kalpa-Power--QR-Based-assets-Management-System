package com.kalpapower.qrmanager.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.Result;
import com.kalpapower.qrmanager.model.Product;

import org.json.JSONException;

/**
 * Utility class for QR code scanning functionality
 */
public class QRCodeScanner {
    private static final String TAG = "QRCodeScanner";
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    
    /**
     * Check if camera permission is granted
     */
    public static boolean hasCameraPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    
    /**
     * Request camera permission
     */
    public static void requestCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST_CODE
        );
    }
    
    /**
     * Process QR code scan result
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
