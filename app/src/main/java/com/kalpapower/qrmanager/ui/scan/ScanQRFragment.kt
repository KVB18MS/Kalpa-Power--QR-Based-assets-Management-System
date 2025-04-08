//
//package com.kalpapower.qrmanager.ui.scan
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.findNavController
//import com.budiyev.android.codescanner.*
//import com.kalpapower.qrmanager.R
//import com.kalpapower.qrmanager.database.DatabaseHelper
//import com.kalpapower.qrmanager.model.Product
//import org.json.JSONException
//
//class ScanQRFragment : Fragment() {
//    private lateinit var codeScanner: CodeScanner
//    private lateinit var scannerView: CodeScannerView
//    private lateinit var textResult: TextView
//    private lateinit var buttonViewDetails: Button
//    private lateinit var databaseHelper: DatabaseHelper
//    private var scannedProduct: Product? = null
//
//    companion object {
//        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_scan_qr, container, false)
//        scannerView = view.findViewById(R.id.scanner_view)
//        textResult = view.findViewById(R.id.text_scan_result)
//        buttonViewDetails = view.findViewById(R.id.button_view_details)
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        databaseHelper = DatabaseHelper.getInstance(requireContext().applicationContext)
//
//        buttonViewDetails.setOnClickListener { navigateToProductDetails() }
//        buttonViewDetails.visibility = View.GONE
//
//        scannerView.setOnClickListener {
//            if (::codeScanner.isInitialized) {
//                codeScanner.startPreview()
//            }
//        }
//
//        if (hasCameraPermission()) {
//            initializeScanner()
//        } else {
//            requestCameraPermission()
//        }
//    }
//
//    private fun hasCameraPermission(): Boolean {
//        return ContextCompat.checkSelfPermission(
//            requireContext(),
//            Manifest.permission.CAMERA
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun requestCameraPermission() {
//        ActivityCompat.requestPermissions(
//            requireActivity(),
//            arrayOf(Manifest.permission.CAMERA),
//            CAMERA_PERMISSION_REQUEST_CODE
//        )
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                initializeScanner()
//            } else {
//                Toast.makeText(requireContext(), "Camera permission denied!", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//    private fun initializeScanner() {
//        codeScanner = CodeScanner(requireContext(), scannerView).apply {
//            camera = CodeScanner.CAMERA_BACK
//            formats = CodeScanner.ALL_FORMATS
//            autoFocusMode = AutoFocusMode.SAFE
//            scanMode = ScanMode.SINGLE
//            isAutoFocusEnabled = true
//            isFlashEnabled = false
//
//            decodeCallback = DecodeCallback { result ->
//                activity?.runOnUiThread {
//                    try {
//                        // Extract product data from QR Code
//                        scannedProduct = Product.fromJSON(result.text)
//
//                        val dbProduct = databaseHelper.getProductBySerialNumber(
//                            scannedProduct?.serialNumber ?: ""
//                        )
//
//                        if (dbProduct != null) {
//                            databaseHelper.updateProductLastScanned(dbProduct.serialNumber)
//
//                            textResult.text = """
//                                Product: ${dbProduct.productName}
//                                Serial: ${dbProduct.serialNumber}
//                                Manufacturer: ${dbProduct.manufacturerName}
//                                Vendor: ${dbProduct.vendorName}
//                            """.trimIndent()
//
//                            // Store scanned product for navigation
//                            scannedProduct = dbProduct
//                            buttonViewDetails.visibility = View.VISIBLE
//                        } else {
//                            textResult.text = "Unknown product scanned. Not registered in the system."
//                            buttonViewDetails.visibility = View.GONE
//                        }
//                    } catch (e: JSONException) {
//                        Log.e("ScanQRFragment", "Error parsing QR code", e)
//                        textResult.text = "Invalid QR code. Please scan a valid product QR code."
//                        buttonViewDetails.visibility = View.GONE
//                    }
//                }
//            }
//
//            errorCallback = ErrorCallback { error ->
//                activity?.runOnUiThread {
//                    Log.e("ScanQRFragment", "Camera error: ${error.message}")
//                    Toast.makeText(
//                        requireContext(),
//                        "Camera error: ${error.message}",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//        }
//
//        scannerView.setOnClickListener { codeScanner.startPreview() }
//    }
//
//    private fun navigateToProductDetails() {
//        scannedProduct?.let { product ->
//            val bundle = Bundle().apply {
//                putString("productName", product.productName)
//                putString("category", product.productCategory)
//                putString("serialNumber", product.serialNumber)
//                putString("manufacturerName", product.manufacturerName)
//                putString("manufacturingDate", product.manufacturingDate.toString())
//                putString("vendorName", product.vendorName)
//                putString("warrantyPeriod", product.warrantyPeriod.toString())
//                putString("sellDate", product.sellDate.toString())
//                putString("technicalSpecs", product.technicalSpecs)
//                putString("additionalNotes", product.additionalNotes)
//            }
//            findNavController().navigate(R.id.action_scan_to_productDetail, bundle)
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (::codeScanner.isInitialized) {
//            codeScanner.startPreview()
//        }
//    }
//
//    override fun onPause() {
//        if (::codeScanner.isInitialized) {
//            codeScanner.releaseResources()
//        }
//        super.onPause()
//    }
//}
package com.kalpapower.qrmanager.ui.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
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
import com.budiyev.android.codescanner.*
import com.kalpapower.qrmanager.R
import com.kalpapower.qrmanager.database.DatabaseHelper
import com.kalpapower.qrmanager.model.Product
import org.json.JSONException
import android.text.util.Linkify

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_scan_qr, container, false)
        scannerView = view.findViewById(R.id.scanner_view)
        textResult = view.findViewById(R.id.text_scan_result)
        buttonViewDetails = view.findViewById(R.id.button_view_details)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseHelper = DatabaseHelper.getInstance(requireContext().applicationContext)

        buttonViewDetails.setOnClickListener { navigateToProductDetails() }
        buttonViewDetails.visibility = View.GONE

        scannerView.setOnClickListener { codeScanner.startPreview() }

        if (hasCameraPermission()) {
            initializeScanner()
        } else {
            requestCameraPermission()
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializeScanner()
        } else {
            Toast.makeText(requireContext(), "Camera permission denied!", Toast.LENGTH_LONG).show()
        }
    }

    private fun initializeScanner() {
        codeScanner = CodeScanner(requireContext(), scannerView).apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback { result ->
                activity?.runOnUiThread {
                    try {
                        scannedProduct = Product.fromJSON(result.text)
                        val dbProduct = databaseHelper.getProductBySerialNumber(scannedProduct!!.serialNumber)

                        if (dbProduct != null) {
                            databaseHelper.updateProductLastScanned(dbProduct.serialNumber)
                            displayProductDetails(dbProduct)
                            scannedProduct = dbProduct
                            buttonViewDetails.visibility = View.VISIBLE
                        } else {
                            textResult.text = "Unknown product scanned. Not registered in the system."
                            buttonViewDetails.visibility = View.GONE
                        }
                    } catch (e: JSONException) {
                        Log.e("ScanQRFragment", "Error parsing QR code", e)
                        textResult.text = "Invalid QR code. Please scan a valid product QR code."
                        buttonViewDetails.visibility = View.GONE
                    }
                }
            }

            errorCallback = ErrorCallback { error ->
                activity?.runOnUiThread {
                    Log.e("ScanQRFragment", "Camera error: ${error.message}")
                    Toast.makeText(requireContext(), "Camera error: ${error.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

//    private fun displayProductDetails(product: Product) {
//        val details = """
//            Product: ${product.productName}
//            Category: ${product.productCategory}
//            Serial: ${product.serialNumber}
//            Manufacturer: ${product.manufacturerName}
//            Manufacturing Date: ${product.manufacturingDate}
//            Warranty: ${product.warrantyPeriod} years
//            Sell Date: ${product.sellDate}
//            Technical Specs: ${product.technicalSpecs}
//            Warranty Certificate: ${product.vendorName}
//            Warranty Details: ${product.additionalNotes}
//        """.trimIndent()
//
//        val spannableDetails = SpannableString(details)
//        textResult.text = spannableDetails
//    }


      // Add this import

    private fun displayProductDetails(product: Product) {
        val details = """
        Product: ${product.productName}
        Category: ${product.productCategory}
        Serial: ${product.serialNumber}
        Manufacturer: ${product.manufacturerName}
        Manufacturing Date: ${product.manufacturingDate}
        Warranty: ${product.warrantyPeriod} years
        Sell Date: ${product.sellDate}
        Technical Specs: ${product.technicalSpecs}
        Warranty Certificate: ${product.vendorName}
        Warranty Details: ${product.additionalNotes}
    """.trimIndent()

        textResult.text = details

        // Enable automatic link detection in the TextView
        Linkify.addLinks(textResult, Linkify.WEB_URLS)

        // Ensure links are clickable
        textResult.movementMethod = LinkMovementMethod.getInstance()

        // Enable text selection
        textResult.setTextIsSelectable(true)
    }



    private fun navigateToProductDetails() {
        scannedProduct?.let {
            val bundle = Bundle().apply {
                putString("productName", it.productName)
                putString("category", it.productCategory)
                putString("serialNumber", it.serialNumber)
                putString("manufacturerName", it.manufacturerName)
                putString("manufacturingDate", it.manufacturingDate.toString())
                putString("vendorName", it.vendorName)
                putString("warrantyPeriod", it.warrantyPeriod.toString())
                putString("sellDate", it.sellDate.toString())
                putString("technicalSpecs", it.technicalSpecs)
                putString("additionalNotes", it.additionalNotes)
            }
            findNavController().navigate(R.id.action_scan_to_productDetail, bundle)
        }
    }

    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized) codeScanner.startPreview()
    }

    override fun onPause() {
        if (::codeScanner.isInitialized) codeScanner.releaseResources()
        super.onPause()
    }
}
