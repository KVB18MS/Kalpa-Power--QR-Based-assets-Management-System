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
import java.util.*

/**
 * Dashboard fragment that displays the inventory of solar products.
 */
class DashboardFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var emptyView: TextView
    private lateinit var searchView: SearchView
    private lateinit var categorySpinner: Spinner

    private var allProducts: List<Product> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            // Create a Bundle to pass the product serial number
            val bundle = Bundle().apply {
                putString("serialNumber", product.serialNumber)
            }
            // Navigate using the action defined in nav_graph.xml.
            // Ensure that your navigation graph defines an action with the ID action_dashboard_to_productDetail.
            findNavController().navigate(R.id.action_dashboard_to_productDetail, bundle)
        }
        recyclerView.adapter = adapter

        // Load products and set up filters
        loadProducts()
        setupSearch()
        setupCategoryFilter()
    }

    private fun loadProducts() {
        allProducts = databaseHelper.allProducts
        adapter.setProducts(allProducts)

        // Show empty view if no products are available
        if (allProducts.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
            emptyView.text = getString(R.string.no_products_available)
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
        val categories = HashSet<String>()
        categories.add(getString(R.string.all_categories)) // Default option

        for (product in allProducts) {
            product.productCategory?.let { category ->
                if (category.isNotEmpty()) {
                    categories.add(category)
                }
            }
        }

        val categoriesArray = categories.toTypedArray()
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoriesArray)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterProducts(searchView.query.toString(), getCategoryFilter())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing.
            }
        }
    }

    private fun getCategoryFilter(): String? {
        val selectedCategory = categorySpinner.selectedItem.toString()
        return if (selectedCategory == getString(R.string.all_categories)) null else selectedCategory
    }

    private fun filterProducts(searchQuery: String?, categoryFilter: String?) {
        var filteredList = allProducts

        // Apply category filter
        categoryFilter?.let { filter ->
            filteredList = filteredList.filter { product ->
                product.productCategory == filter
            }
        }

        // Apply search filter
        if (!searchQuery.isNullOrEmpty()) {
            val query = searchQuery.lowercase(Locale.getDefault())
            filteredList = filteredList.filter { product ->
                (product.productName?.lowercase(Locale.getDefault())?.contains(query) == true ||
                        product.serialNumber?.lowercase(Locale.getDefault())?.contains(query) == true ||
                        product.manufacturerName?.lowercase(Locale.getDefault())?.contains(query) == true)
            }
        }

        adapter.setProducts(filteredList)

        // Update empty view visibility
        if (filteredList.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
            emptyView.text = getString(R.string.no_matching_products)
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
        setupCategoryFilter()
    }
}
