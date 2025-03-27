package com.kalpapower.qrmanager.ui.scan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.kalpapower.qrmanager.R
import com.kalpapower.qrmanager.database.DatabaseHelper
import com.kalpapower.qrmanager.model.Product
import com.kalpapower.qrmanager.utils.QRCodeScanner
import org.json.JSONException

/**
 * Fragment for scanning QR codes
 */
class ScanQRFragment : Fragment() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var scannerView: CodeScannerView
    private lateinit var textResult: TextView
    private lateinit var buttonViewDetails: Button
    private lateinit var databaseHelper: DatabaseHelper
    
    private var scannedProduct: Product? = null
    
    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scan_qr, container, false)
        
        scannerView = view.findViewById(R.id.scanner_view)
        textResult = view.findViewById(R.id.text_scan_result)
        buttonViewDetails = view.findViewById(R.id.button_view_details)
        
        return view
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize database helper
        databaseHelper = DatabaseHelper.getInstance(requireContext())
        
        // Set up button click listener
        buttonViewDetails.setOnClickListener {
            navigateToProductDetails()
        }
        
        // Initially hide button
        buttonViewDetails.visibility = View.GONE
        
        // Check camera permission
        if (hasCameraPermission()) {
            initializeScanner()
        } else {
            requestCameraPermission()
        }
    }
    
    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeScanner()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Camera permission is required to scan QR codes",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    
    private fun initializeScanner() {
        codeScanner = CodeScanner(requireContext(), scannerView)
        
        // Set scanner properties
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false
        
        // Set decode callback
        codeScanner.decodeCallback = DecodeCallback { result ->
            activity?.runOnUiThread {
                try {
                    // Parse scanned product
                    scannedProduct = Product.fromJSON(result.text)
                    
                    // Check if product exists in database
                    val dbProduct = databaseHelper.getProductBySerialNumber(scannedProduct?.serialNumber ?: "")
                    
                    if (dbProduct != null) {
                        // Update last scanned timestamp
                        databaseHelper.updateProductLastScanned(dbProduct.serialNumber)
                        
                        // Show success message and enable button
                        textResult.text = "Product found: ${dbProduct.productName}\nSerial: ${dbProduct.serialNumber}"
                        buttonViewDetails.visibility = View.VISIBLE
                    } else {
                        // Product not found in database
                        textResult.text = "Unknown QR code scanned. This product is not registered in the system."
                        buttonViewDetails.visibility = View.GONE
                    }
                } catch (e: JSONException) {
                    // Invalid QR code
                    Log.e("ScanQRFragment", "Error parsing QR code", e)
                    textResult.text = "Invalid QR code. Please scan a valid Kalpa Power product QR code."
                    buttonViewDetails.visibility = View.GONE
                }
            }
        }
        
        // Handle errors
        codeScanner.errorCallback = { error ->
            activity?.runOnUiThread {
                Log.e("ScanQRFragment", "Camera initialization error: ${error.message}")
                Toast.makeText(
                    requireContext(),
                    "Camera error: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        
        // Set scanner click listener to restart preview
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }
    
    private fun navigateToProductDetails() {
        if (scannedProduct != null) {
            val action = ScanQRFragmentDirections.actionScanToProductDetail(scannedProduct!!.serialNumber)
            findNavController().navigate(action)
        }
    }
    
    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized) {
            codeScanner.startPreview()
        }
    }
    
    override fun onPause() {
        if (::codeScanner.isInitialized) {
            codeScanner.releaseResources()
        }
        super.onPause()
    }
}
