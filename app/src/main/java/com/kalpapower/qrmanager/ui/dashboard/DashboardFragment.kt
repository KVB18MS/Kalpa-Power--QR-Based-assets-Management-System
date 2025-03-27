package com.kalpapower.qrmanager.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kalpapower.qrmanager.R
import com.kalpapower.qrmanager.adapter.ProductAdapter
import com.kalpapower.qrmanager.database.DatabaseHelper
import com.kalpapower.qrmanager.model.Product
import java.util.HashSet

/**
 * Dashboard fragment that displays the inventory of solar products
 */
class DashboardFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var emptyView: TextView
    private lateinit var searchView: SearchView
    private lateinit var categorySpinner: Spinner
    
    private var allProducts: List<Product> = ArrayList()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        
        // Initialize views
        recyclerView = view.findViewById(R.id.recycler_products)
        emptyView = view.findViewById(R.id.empty_view)
        searchView = view.findViewById(R.id.search_view)
        categorySpinner = view.findViewById(R.id.spinner_category)
        
        return view
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize database helper
        databaseHelper = DatabaseHelper.getInstance(requireContext())
        
        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ProductAdapter { product ->
            // Navigate to product details on item click
            val action = DashboardFragmentDirections.actionDashboardToProductDetail(product.serialNumber)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
        
        // Populate data
        loadProducts()
        
        // Set up search functionality
        setupSearch()
        
        // Set up category filter
        setupCategoryFilter()
    }
    
    private fun loadProducts() {
        allProducts = databaseHelper.allProducts
        adapter.setProducts(allProducts)
        
        // Show empty view if no products
        if (allProducts.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }
    }
    
    private fun setupSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterProducts(query, getCategoryFilter())
                return true
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                filterProducts(newText, getCategoryFilter())
                return true
            }
        })
    }
    
    private fun setupCategoryFilter() {
        // Get unique categories from products
        val categories = HashSet<String>()
        categories.add("All Categories") // Default option
        
        for (product in allProducts) {
            if (product.productCategory != null && product.productCategory.isNotEmpty()) {
                categories.add(product.productCategory)
            }
        }
        
        // Create adapter for spinner
        val categoriesArray = categories.toTypedArray()
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoriesArray)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter
        
        // Set listener
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterProducts(searchView.query.toString(), getCategoryFilter())
            }
            
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }
    
    private fun getCategoryFilter(): String? {
        val selectedCategory = categorySpinner.selectedItem.toString()
        return if (selectedCategory == "All Categories") null else selectedCategory
    }
    
    private fun filterProducts(searchQuery: String?, categoryFilter: String?) {
        var filteredList = allProducts
        
        // Apply category filter
        if (categoryFilter != null) {
            filteredList = filteredList.filter { it.productCategory == categoryFilter }
        }
        
        // Apply search filter
        if (!searchQuery.isNullOrEmpty()) {
            val query = searchQuery.toLowerCase()
            filteredList = filteredList.filter {
                it.productName.toLowerCase().contains(query) ||
                it.serialNumber.toLowerCase().contains(query) ||
                it.manufacturerName.toLowerCase().contains(query)
            }
        }
        
        adapter.setProducts(filteredList)
        
        // Update empty view visibility
        if (filteredList.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
            emptyView.text = "No products found matching your search"
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Reload products when fragment resumes
        loadProducts()
        setupCategoryFilter()
    }
}
