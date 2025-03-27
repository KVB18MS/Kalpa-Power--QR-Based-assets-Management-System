package com.kalpapower.qrmanager.ui.generate

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.kalpapower.qrmanager.R
import com.kalpapower.qrmanager.database.DatabaseHelper
import com.kalpapower.qrmanager.model.Product
import com.kalpapower.qrmanager.utils.QRCodeGenerator
import org.json.JSONException
import java.io.File
import java.io.FileOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * Fragment for generating QR codes for new products
 */
class GenerateQRFragment : Fragment() {
    private lateinit var editProductName: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var editSerialNumber: EditText
    private lateinit var editManufacturerName: EditText
    private lateinit var editManufacturingDate: EditText
    private lateinit var editVendorName: EditText
    private lateinit var editWarrantyPeriod: EditText
    private lateinit var editSellDate: EditText
    private lateinit var editTechnicalSpecs: EditText
    private lateinit var editAdditionalNotes: EditText
    private lateinit var buttonGenerateQR: Button
    private lateinit var buttonPrintQR: Button
    private lateinit var buttonShareQR: Button
    private lateinit var imageQrCode: ImageView
    
    private lateinit var databaseHelper: DatabaseHelper
    private var generatedQRBitmap: Bitmap? = null
    private var currentProduct: Product? = null
    private var qrCodeFilePath: String? = null
    
    // Date format for displayed dates
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_generate_qr, container, false)
        
        // Initialize views
        editProductName = view.findViewById(R.id.edit_product_name)
        spinnerCategory = view.findViewById(R.id.spinner_category)
        editSerialNumber = view.findViewById(R.id.edit_serial_number)
        editManufacturerName = view.findViewById(R.id.edit_manufacturer_name)
        editManufacturingDate = view.findViewById(R.id.edit_manufacturing_date)
        editVendorName = view.findViewById(R.id.edit_vendor_name)
        editWarrantyPeriod = view.findViewById(R.id.edit_warranty_period)
        editSellDate = view.findViewById(R.id.edit_sell_date)
        editTechnicalSpecs = view.findViewById(R.id.edit_technical_specs)
        editAdditionalNotes = view.findViewById(R.id.edit_additional_notes)
        buttonGenerateQR = view.findViewById(R.id.button_generate_qr)
        buttonPrintQR = view.findViewById(R.id.button_print_qr)
        buttonShareQR = view.findViewById(R.id.button_share_qr)
        imageQrCode = view.findViewById(R.id.image_qr_code)
        
        // Set up database helper
        databaseHelper = DatabaseHelper.getInstance(requireContext())
        
        // Set up date pickers
        setupDatePickers()
        
        // Set up category spinner
        setupCategorySpinner()
        
        // Generate a random serial number (can be overwritten by user)
        generateRandomSerialNumber()
        
        // Set up button click listeners
        setupButtonListeners()
        
        // Initially disable print button until QR is generated
        buttonPrintQR.isEnabled = false
        buttonShareQR.isEnabled = false
        
        return view
    }
    
    private fun setupCategorySpinner() {
        // Product categories
        val categories = arrayOf(
            "Solar Panel",
            "Inverter",
            "Battery",
            "UPS",
            "Charge Controller",
            "Solar Pump",
            "Other"
        )
        
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter
    }
    
    private fun setupDatePickers() {
        // Set up manufacturing date picker
        editManufacturingDate.setOnClickListener {
            showDatePicker(editManufacturingDate)
        }
        
        // Set up sell date picker
        editSellDate.setOnClickListener {
            showDatePicker(editSellDate)
        }
    }
    
    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        
        // If date is already set, use it as starting date
        try {
            val text = editText.text.toString()
            if (text.isNotEmpty()) {
                val date = dateFormat.parse(text)
                if (date != null) {
                    calendar.time = date
                }
            }
        } catch (e: ParseException) {
            // Ignore parse error, use current date
        }
        
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                editText.setText(dateFormat.format(calendar.time))
            },
            year, month, day
        )
        
        datePickerDialog.show()
    }
    
    private fun generateRandomSerialNumber() {
        // Generate a random serial number (format: KP-YYYYMMDD-XXXX)
        val today = dateFormat.format(Date())
        val random = UUID.randomUUID().toString().substring(0, 4).toUpperCase(Locale.US)
        val serialNumber = "KP-${today.replace("-", "")}-$random"
        
        editSerialNumber.setText(serialNumber)
    }
    
    private fun setupButtonListeners() {
        buttonGenerateQR.setOnClickListener {
            if (validateInputs()) {
                generateQRCode()
            }
        }
        
        buttonPrintQR.setOnClickListener {
            printQRCode()
        }
        
        buttonShareQR.setOnClickListener {
            shareQRCode()
        }
    }
    
    private fun validateInputs(): Boolean {
        // Check required fields
        if (editProductName.text.toString().trim().isEmpty()) {
            showError(editProductName, "Product name is required")
            return false
        }
        
        if (editSerialNumber.text.toString().trim().isEmpty()) {
            showError(editSerialNumber, "Serial number is required")
            return false
        }
        
        if (editManufacturerName.text.toString().trim().isEmpty()) {
            showError(editManufacturerName, "Manufacturer name is required")
            return false
        }
        
        if (editManufacturingDate.text.toString().trim().isEmpty()) {
            showError(editManufacturingDate, "Manufacturing date is required")
            return false
        }
        
        if (editVendorName.text.toString().trim().isEmpty()) {
            showError(editVendorName, "Vendor name is required")
            return false
        }
        
        if (editWarrantyPeriod.text.toString().trim().isEmpty()) {
            showError(editWarrantyPeriod, "Warranty period is required")
            return false
        }
        
        // Check if serial number already exists
        val existingProduct = databaseHelper.getProductBySerialNumber(editSerialNumber.text.toString().trim())
        if (existingProduct != null) {
            showError(editSerialNumber, "Serial number already exists")
            return false
        }
        
        return true
    }
    
    private fun showError(editText: EditText, errorMessage: String) {
        val parent = editText.parent.parent
        if (parent is TextInputLayout) {
            parent.error = errorMessage
        } else {
            editText.error = errorMessage
        }
        editText.requestFocus()
    }
    
    private fun clearErrors() {
        val views = listOf(
            editProductName, editSerialNumber, editManufacturerName,
            editManufacturingDate, editVendorName, editWarrantyPeriod
        )
        
        for (editText in views) {
            val parent = editText.parent.parent
            if (parent is TextInputLayout) {
                parent.error = null
            } else {
                editText.error = null
            }
        }
    }
    
    private fun createProductFromInputs(): Product {
        val product = Product()
        
        // Required fields
        product.productName = editProductName.text.toString().trim()
        product.productCategory = spinnerCategory.selectedItem.toString()
        product.serialNumber = editSerialNumber.text.toString().trim()
        product.manufacturerName = editManufacturerName.text.toString().trim()
        product.vendorName = editVendorName.text.toString().trim()
        product.warrantyPeriod = editWarrantyPeriod.text.toString().toInt()
        
        // Parse dates
        try {
            val manufacturingDateStr = editManufacturingDate.text.toString().trim()
            if (manufacturingDateStr.isNotEmpty()) {
                product.manufacturingDate = dateFormat.parse(manufacturingDateStr)
            }
            
            val sellDateStr = editSellDate.text.toString().trim()
            if (sellDateStr.isNotEmpty()) {
                product.sellDate = dateFormat.parse(sellDateStr)
            }
        } catch (e: ParseException) {
            Log.e("GenerateQRFragment", "Error parsing dates", e)
        }
        
        // Optional fields
        product.technicalSpecs = editTechnicalSpecs.text.toString().trim()
        product.additionalNotes = editAdditionalNotes.text.toString().trim()
        
        return product
    }
    
    private fun generateQRCode() {
        try {
            currentProduct = createProductFromInputs()
            
            // Generate QR code bitmap
            generatedQRBitmap = QRCodeGenerator.generateQRCode(currentProduct)
            
            // Show QR code image
            imageQrCode.setImageBitmap(generatedQRBitmap)
            imageQrCode.visibility = View.VISIBLE
            
            // Enable print and share buttons
            buttonPrintQR.isEnabled = true
            buttonShareQR.isEnabled = true
            
            // Save product to database
            val id = databaseHelper.addProduct(currentProduct)
            if (id > 0) {
                currentProduct?.id = id
                Toast.makeText(
                    requireContext(),
                    "QR Code Generated for Serial Number: ${currentProduct?.serialNumber}",
                    Toast.LENGTH_LONG
                ).show()
                
                // Create temporary file for printing/sharing
                saveQRCodeToTempFile()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error saving product to database",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Log.e("GenerateQRFragment", "Error generating QR code", e)
            Toast.makeText(
                requireContext(),
                "Error generating QR code: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    private fun saveQRCodeToTempFile() {
        try {
            val bitmap = generatedQRBitmap ?: return
            
            // Create a temporary file
            val file = File(requireContext().cacheDir, "qrcode_${currentProduct?.serialNumber}.png")
            
            // Write bitmap to file
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            
            qrCodeFilePath = file.absolutePath
        } catch (e: Exception) {
            Log.e("GenerateQRFragment", "Error saving QR code to temp file", e)
        }
    }
    
    private fun printQRCode() {
        if (generatedQRBitmap == null || currentProduct == null) {
            Toast.makeText(requireContext(), "Please generate QR code first", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Create a WebView with the QR code and product details for printing
        val webView = WebView(requireContext())
        
        // Generate HTML content
        val htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body { font-family: Arial, sans-serif; text-align: center; }
                    .qr-container { margin: 20px auto; }
                    .product-info { margin: 20px; text-align: left; }
                    .info-row { margin: 5px 0; }
                    .label { font-weight: bold; }
                    img { max-width: 100%; height: auto; }
                </style>
            </head>
            <body>
                <h1>Kalpa Power - Solar Products</h1>
                <div class="qr-container">
                    <img src="file://${qrCodeFilePath}" alt="QR Code" />
                </div>
                <div class="product-info">
                    <div class="info-row">
                        <span class="label">Product:</span> ${currentProduct?.productName}
                    </div>
                    <div class="info-row">
                        <span class="label">Serial Number:</span> ${currentProduct?.serialNumber}
                    </div>
                    <div class="info-row">
                        <span class="label">Manufacturing Date:</span> ${editManufacturingDate.text}
                    </div>
                    <div class="info-row">
                        <span class="label">Warranty:</span> ${currentProduct?.warrantyPeriod} years
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()
        
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        
        // Set WebViewClient to handle page load completion
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
            
            override fun onPageFinished(view: WebView?, url: String?) {
                // Start print job when page is loaded
                if (isAdded) {
                    val printManager = requireContext().getSystemService(PrintManager::class.java)
                    val jobName = "Kalpa_Power_QR_${currentProduct?.serialNumber}"
                    
                    val printAdapter = webView.createPrintDocumentAdapter(jobName)
                    
                    printManager?.print(
                        jobName,
                        printAdapter,
                        PrintAttributes.Builder().build()
                    )
                }
            }
        }
    }
    
    private fun shareQRCode() {
        if (qrCodeFilePath == null) {
            Toast.makeText(requireContext(), "Error: QR code not available", Toast.LENGTH_SHORT).show()
            return
        }
        
        val file = File(qrCodeFilePath!!)
        if (!file.exists()) {
            Toast.makeText(requireContext(), "Error: QR code file not found", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Get content URI via FileProvider
        val contentUri = FileProvider.getUriForFile(
            requireContext(),
            "com.kalpapower.qrmanager.fileprovider",
            file
        )
        
        // Create intent to share the QR code
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/png"
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "QR Code for ${currentProduct?.productName}")
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Product: ${currentProduct?.productName}\n" +
            "Serial Number: ${currentProduct?.serialNumber}\n" +
            "Manufacturer: ${currentProduct?.manufacturerName}\n" +
            "Warranty: ${currentProduct?.warrantyPeriod} years"
        )
        
        // Grant temporary read permission
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        
        startActivity(Intent.createChooser(shareIntent, "Share QR Code"))
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Clean up temporary file
        if (qrCodeFilePath != null) {
            val file = File(qrCodeFilePath!!)
            if (file.exists()) {
                file.delete()
            }
        }
    }
}
