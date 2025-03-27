package com.kalpapower.qrmanager.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.kalpapower.qrmanager.model.Product;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

/**
 * Enhanced utility class for generating QR codes from product data
 */
public class QRCodeGenerator {
    private static final String TAG = "QRCodeGenerator";
    private static final int QR_CODE_SIZE = 800; // Size in pixels, increased for better quality
    private static final int QR_CODE_MARGIN = 50; // Margin for adding text
    private static final int LABEL_TEXT_SIZE = 30; // Size for labels
    
    /**
     * Generate a QR code bitmap for a product
     * @param product The product to encode in the QR code
     * @return Bitmap containing the QR code
     * @throws Exception if generation fails
     */
    public static Bitmap generateQRCode(Product product) throws Exception {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        
        try {
            String qrCodeData = product.toJSON().toString();
            Bitmap qrBitmap = generateQRCode(qrCodeData, QR_CODE_SIZE);
            
            // Create a bitmap with space for labels
            return addProductInfoToQrCode(qrBitmap, product);
        } catch (WriterException e) {
            Log.e(TAG, "QR code generation failed", e);
            throw new Exception("Failed to generate QR code: " + e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, "JSON conversion failed", e);
            throw new Exception("Failed to convert product to JSON: " + e.getMessage());
        }
    }
    
    /**
     * Generate a QR code bitmap from a string
     * @param data The string to encode in the QR code
     * @param size The size of the QR code in pixels
     * @return Bitmap containing the QR code
     * @throws WriterException if encoding fails
     */
    public static Bitmap generateQRCode(String data, int size) throws WriterException {
        // Validate input
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data cannot be null or empty");
        }
        
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // Higher error correction
        hints.put(EncodeHintType.MARGIN, 2);
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, size, size, hints);
        
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        int[] pixels = new int[width * height];
        
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        
        return bitmap;
    }
    
    /**
     * Add product information to the QR code bitmap
     * @param qrBitmap The original QR code bitmap
     * @param product The product with information to add
     * @return Enhanced bitmap with product information
     */
    private static Bitmap addProductInfoToQrCode(Bitmap qrBitmap, Product product) {
        int width = qrBitmap.getWidth();
        int labelHeight = 200;
        int totalHeight = qrBitmap.getHeight() + labelHeight;
        
        Bitmap labeledBitmap = Bitmap.createBitmap(width, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(labeledBitmap);
        
        // Draw white background
        canvas.drawColor(Color.WHITE);
        
        // Draw the QR code
        canvas.drawBitmap(qrBitmap, 0, 0, null);
        
        // Add the text labels
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(LABEL_TEXT_SIZE);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        
        // Draw product name
        canvas.drawText("Product: " + product.getProductName(), 
                QR_CODE_MARGIN, qrBitmap.getHeight() + 50, textPaint);
        
        // Draw serial number
        canvas.drawText("S/N: " + product.getSerialNumber(), 
                QR_CODE_MARGIN, qrBitmap.getHeight() + 100, textPaint);
        
        // Draw manufacturing date if available
        if (product.getManufacturingDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String dateStr = dateFormat.format(product.getManufacturingDate());
            canvas.drawText("Mfg Date: " + dateStr, 
                    QR_CODE_MARGIN, qrBitmap.getHeight() + 150, textPaint);
        }
        
        return labeledBitmap;
    }
    
    /**
     * Save QR code bitmap to a file in the external storage directory
     * @param qrCodeBitmap The QR code bitmap to save
     * @param serialNumber The product serial number for the filename
     * @return The absolute path to the saved file, or null if saving failed
     */
    public static String saveQRCodeToExternalStorage(Bitmap qrCodeBitmap, String serialNumber) {
        if (qrCodeBitmap == null || serialNumber == null || serialNumber.isEmpty()) {
            Log.e(TAG, "Invalid parameters for saving QR code");
            return null;
        }
        
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String fileName = "QR_" + serialNumber + "_" + timeStamp + ".png";
        
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        // Create directory if it doesn't exist
        if (!storageDir.exists() && !storageDir.mkdirs()) {
            Log.e(TAG, "Failed to create directory for QR code storage");
            return null;
        }
        
        File qrCodeFile = new File(storageDir, fileName);
        
        try {
            FileOutputStream fos = new FileOutputStream(qrCodeFile);
            qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Log.i(TAG, "QR code saved successfully at: " + qrCodeFile.getAbsolutePath());
            return qrCodeFile.getAbsolutePath();
        } catch (IOException e) {
            Log.e(TAG, "Error saving QR code image", e);
            return null;
        }
    }
    
    /**
     * Save QR code bitmap to a file in the app's cache directory
     * @param context The application context
     * @param qrCodeBitmap The QR code bitmap to save
     * @param serialNumber The product serial number for the filename
     * @return The absolute path to the saved file, or null if saving failed
     */
    public static String saveQRCodeToCache(Context context, Bitmap qrCodeBitmap, String serialNumber) {
        if (context == null || qrCodeBitmap == null || serialNumber == null || serialNumber.isEmpty()) {
            Log.e(TAG, "Invalid parameters for saving QR code to cache");
            return null;
        }
        
        // Create a temporary file in the cache directory
        String fileName = "qrcode_" + serialNumber + ".png";
        File cacheDir = context.getCacheDir();
        File qrCodeFile = new File(cacheDir, fileName);
        
        try {
            FileOutputStream fos = new FileOutputStream(qrCodeFile);
            qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Log.i(TAG, "QR code saved to cache at: " + qrCodeFile.getAbsolutePath());
            return qrCodeFile.getAbsolutePath();
        } catch (IOException e) {
            Log.e(TAG, "Error saving QR code to cache", e);
            return null;
        }
    }
    
    // Compatibility method for old code
    public static String saveQRCodeImage(Bitmap qrCodeBitmap, String serialNumber) {
        return saveQRCodeToExternalStorage(qrCodeBitmap, serialNumber);
    }
}
