package com.kalpapower.qrmanager.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kalpapower.qrmanager.R
import com.kalpapower.qrmanager.adapter.FaultAdapter
import com.kalpapower.qrmanager.database.DatabaseHelper
import com.kalpapower.qrmanager.model.Fault
import com.kalpapower.qrmanager.model.Product
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Fragment for displaying product details and fault history.
 */
class ProductDetailFragment : Fragment() {
    private lateinit var textProductName: TextView
    private lateinit var textCategory: TextView
    private lateinit var textSerialNumber: TextView
    private lateinit var textManufacturer: TextView
    private lateinit var textManufacturingDate: TextView
    private lateinit var textVendor: TextView
    private lateinit var textWarranty: TextView
    private lateinit var textSellDate: TextView
    private lateinit var textTechnicalSpecs: TextView
    private lateinit var textAdditionalNotes: TextView
    private lateinit var textLastScanned: TextView
    private lateinit var recyclerFaults: RecyclerView
    private lateinit var buttonReportFault: Button
    private lateinit var textNoFaults: TextView

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var faultAdapter: FaultAdapter

    // Instead of Safe Args, retrieve the serial number from the Bundle
    private var serialNumber: String? = null
    private var product: Product? = null

    // Date formatters
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val timestampFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve the serial number argument from the Bundle
        serialNumber = arguments?.getString("serialNumber")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_detail, container, false)

        // Initialize views
        textProductName = view.findViewById(R.id.text_product_name)
        textCategory = view.findViewById(R.id.text_category)
        textSerialNumber = view.findViewById(R.id.text_serial_number)
        textManufacturer = view.findViewById(R.id.text_manufacturer)
        textManufacturingDate = view.findViewById(R.id.text_manufacturing_date)
        textVendor = view.findViewById(R.id.text_vendor)
        textWarranty = view.findViewById(R.id.text_warranty)
        textSellDate = view.findViewById(R.id.text_sell_date)
        textTechnicalSpecs = view.findViewById(R.id.text_technical_specs)
        textAdditionalNotes = view.findViewById(R.id.text_additional_notes)
        textLastScanned = view.findViewById(R.id.text_last_scanned)
        recyclerFaults = view.findViewById(R.id.recycler_faults)
        buttonReportFault = view.findViewById(R.id.button_report_fault)
        textNoFaults = view.findViewById(R.id.text_no_faults)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize database helper
        databaseHelper = DatabaseHelper.getInstance(requireContext())

        // Get product by serial number
        serialNumber?.let {
            product = databaseHelper.getProductBySerialNumber(it)
        }

        if (product != null) {
            // Display product details
            displayProductDetails()

            // Set up fault history list
            setupFaultHistory()

            // Set up report fault button
            buttonReportFault.setOnClickListener {
                navigateToReportFault()
            }
        } else {
            // Handle case when product is not found
            handleProductNotFound()
        }
    }

    private fun displayProductDetails() {
        product?.let { p ->
            textProductName.text = p.productName
            textCategory.text = p.productCategory
            textSerialNumber.text = p.serialNumber
            textManufacturer.text = p.manufacturerName

            // Format manufacturing date
            if (p.manufacturingDate != null) {
                textManufacturingDate.text = dateFormat.format(p.manufacturingDate)
            }

            textVendor.text = p.vendorName

            // Display warranty status
            val warrantyValid = databaseHelper.isWarrantyValid(p)
            val warrantyEndDate = databaseHelper.getWarrantyEndDate(p)
            val warrantyStatus = if (warrantyValid) "Active" else "Expired"
            val warrantyEndText = if (warrantyEndDate != null)
                " (Until ${dateFormat.format(warrantyEndDate)})"
            else
                ""
            textWarranty.text = "${p.warrantyPeriod} years - $warrantyStatus$warrantyEndText"

            // Set text color based on warranty status
            val warrantyColor = if (warrantyValid)
                resources.getColor(R.color.warranty_valid, null)
            else
                resources.getColor(R.color.warranty_expired, null)
            textWarranty.setTextColor(warrantyColor)

            // Format sell date
            if (p.sellDate != null) {
                textSellDate.text = dateFormat.format(p.sellDate)
            } else {
                textSellDate.text = "Not sold yet"
            }

            // Display technical specs and notes
            textTechnicalSpecs.text = p.technicalSpecs ?: "N/A"
            textAdditionalNotes.text = p.additionalNotes ?: "N/A"

            // Display last scanned time
            if (p.lastScanned != null) {
                textLastScanned.text = "Last scanned: ${timestampFormat.format(p.lastScanned)}"
            } else {
                textLastScanned.text = "Never scanned before"
            }
        }
    }

    private fun setupFaultHistory() {
        product?.let { p ->
            // Initialize RecyclerView
            recyclerFaults.layoutManager = LinearLayoutManager(context)
            faultAdapter = FaultAdapter()
            recyclerFaults.adapter = faultAdapter

            // Get faults for this product
            val faults = databaseHelper.getFaultsForProduct(p.id)

            if (faults.isEmpty()) {
                textNoFaults.visibility = View.VISIBLE
                recyclerFaults.visibility = View.GONE
            } else {
                textNoFaults.visibility = View.GONE
                recyclerFaults.visibility = View.VISIBLE
                faultAdapter.setFaults(faults)
            }
        }
    }

    private fun navigateToReportFault() {
        product?.let { p ->
            // Create a bundle to pass the product ID
            val bundle = Bundle().apply {
                putLong("productId", p.id)
            }
            // Navigate to ReportFaultFragment using the action defined in your navigation graph.
            findNavController().navigate(R.id.action_productDetail_to_reportFault, bundle)
        }
    }

    private fun handleProductNotFound() {
        // Hide all detail views
        val container = view?.findViewById<ViewGroup>(R.id.container_product_details)
        container?.visibility = View.GONE

        // Show error message
        val errorText = view?.findViewById<TextView>(R.id.text_error_message)
        errorText?.visibility = View.VISIBLE
        errorText?.text = "Product not found with serial number: $serialNumber"

        // Disable report fault button
        buttonReportFault.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        // Refresh fault history when returning to fragment
        product?.let {
            setupFaultHistory()
        }
    }
}
