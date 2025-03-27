package com.kalpapower.qrmanager.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.kalpapower.qrmanager.model.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

/**
 * Utility class for generating QR codes from product data
 */
public class QRCodeGenerator {
    private static final String TAG = "QRCodeGenerator";
    private static final int QR_CODE_SIZE = 600; // Size in pixels
    
    /**
     * Generate a QR code bitmap for a product
     */
    public static Bitmap generateQRCode(Product product) throws Exception {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        
        String qrCodeData = product.toJSON().toString();
        return generateQRCode(qrCodeData, QR_CODE_SIZE);
    }
    
    /**
     * Generate a QR code bitmap from a string
     */
    public static Bitmap generateQRCode(String data, int size) throws WriterException {
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
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
     * Save QR code bitmap to a file and return the file path
     */
    public static String saveQRCodeImage(Bitmap qrCodeBitmap, String serialNumber) {
        if (qrCodeBitmap == null) {
            return null;
        }
        
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String fileName = "QR_" + serialNumber + "_" + timeStamp + ".png";
        
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File qrCodeFile = new File(storageDir, fileName);
        
        try {
            FileOutputStream fos = new FileOutputStream(qrCodeFile);
            qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return qrCodeFile.getAbsolutePath();
        } catch (IOException e) {
            Log.e(TAG, "Error saving QR code image", e);
            return null;
        }
    }
}
