package com.kalpapower.qrmanager.ui.fault

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import com.kalpapower.qrmanager.R
import com.kalpapower.qrmanager.database.DatabaseHelper
import com.kalpapower.qrmanager.model.Fault
import com.kalpapower.qrmanager.model.Product
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Fragment for reporting product faults
 */
class ReportFaultFragment : Fragment() {
    private lateinit var textProductInfo: TextView
    private lateinit var editFaultDescription: EditText
    private lateinit var editFaultDate: EditText
    private lateinit var spinnerSeverity: Spinner
    private lateinit var editTechnicianName: EditText
    private lateinit var buttonSubmitFault: Button
    
    private lateinit var databaseHelper: DatabaseHelper
    private val args: ReportFaultFragmentArgs by navArgs()
    private var product: Product? = null
    
    // Date format for displayed dates
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report_fault, container, false)
        
        // Initialize views
        textProductInfo = view.findViewById(R.id.text_product_info)
        editFaultDescription = view.findViewById(R.id.edit_fault_description)
        editFaultDate = view.findViewById(R.id.edit_fault_date)
        spinnerSeverity = view.findViewById(R.id.spinner_severity)
        editTechnicianName = view.findViewById(R.id.edit_technician_name)
        buttonSubmitFault = view.findViewById(R.id.button_submit_fault)
        
        return view
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize database helper
        databaseHelper = DatabaseHelper.getInstance(requireContext())
        
        // Get product by ID
        val productId = args.productId
        product = databaseHelper.getProductById(productId)
        
        if (product != null) {
            // Display product info
            displayProductInfo()
            
            // Set up severity spinner
            setupSeveritySpinner()
            
            // Set up date picker
            setupDatePicker()
            
            // Set default fault date to today
            editFaultDate.setText(dateFormat.format(Date()))
            
            // Set up submit button
            buttonSubmitFault.setOnClickListener {
                if (validateInputs()) {
                    submitFaultReport()
                }
            }
        } else {
            // Handle case when product is not found
            handleProductNotFound()
        }
    }
    
    private fun displayProductInfo() {
        product?.let { p ->
            textProductInfo.text = "${p.productName} (${p.productCategory})\n" +
                                  "S/N: ${p.serialNumber}"
        }
    }
    
    private fun setupSeveritySpinner() {
        // Severity levels
        val severityLevels = arrayOf(
            Fault.SEVERITY_LOW,
            Fault.SEVERITY_MEDIUM,
            Fault.SEVERITY_HIGH
        )
        
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            severityLevels
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSeverity.adapter = adapter
    }
    
    private fun setupDatePicker() {
        editFaultDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            
            // If date is already set, use it as starting date
            try {
                val text = editFaultDate.text.toString()
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
                    editFaultDate.setText(dateFormat.format(calendar.time))
                },
                year, month, day
            )
            
            // Don't allow future dates
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            
            datePickerDialog.show()
        }
    }
    
    private fun validateInputs(): Boolean {
        // Check required fields
        if (editFaultDescription.text.toString().trim().isEmpty()) {
            showError(editFaultDescription, "Fault description is required")
            return false
        }
        
        if (editFaultDate.text.toString().trim().isEmpty()) {
            showError(editFaultDate, "Fault date is required")
            return false
        }
        
        if (editTechnicianName.text.toString().trim().isEmpty()) {
            showError(editTechnicianName, "Technician name is required")
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
    
    private fun submitFaultReport() {
        try {
            product?.let { p ->
                // Create fault object
                val fault = Fault(
                    p.id,
                    editFaultDescription.text.toString().trim(),
                    dateFormat.parse(editFaultDate.text.toString().trim()),
                    spinnerSeverity.selectedItem.toString(),
                    editTechnicianName.text.toString().trim()
                )
                
                // Save to database
                val id = databaseHelper.addFault(fault)
                
                if (id > 0) {
                    Toast.makeText(
                        requireContext(),
                        "Fault report submitted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    
                    // Navigate back to product details
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error saving fault report",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Error: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    private fun handleProductNotFound() {
        // Hide form
        val formContainer = view?.findViewById<ViewGroup>(R.id.container_fault_form)
        formContainer?.visibility = View.GONE
        
        // Show error message
        val errorText = view?.findViewById<TextView>(R.id.text_error_message)
        errorText?.visibility = View.VISIBLE
        errorText?.text = "Product not found with ID: ${args.productId}"
        
        // Disable submit button
        buttonSubmitFault.isEnabled = false
    }
}
