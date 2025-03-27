package com.kalpapower.qrmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kalpapower.qrmanager.R
import com.kalpapower.qrmanager.database.DatabaseHelper
import com.kalpapower.qrmanager.model.Product
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Adapter for displaying products in a RecyclerView
 */
class ProductAdapter(private val onProductClick: (Product) -> Unit) : 
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    
    private var products: List<Product> = ArrayList()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    
    fun setProducts(products: List<Product>) {
        this.products = products
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
        
        // Set click listener for the item
        holder.itemView.setOnClickListener {
            onProductClick(product)
        }
    }
    
    override fun getItemCount(): Int = products.size
    
    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textProductName: TextView = itemView.findViewById(R.id.text_product_name)
        private val textSerialNumber: TextView = itemView.findViewById(R.id.text_serial_number)
        private val textCategory: TextView = itemView.findViewById(R.id.text_category)
        private val textManufacturer: TextView = itemView.findViewById(R.id.text_manufacturer)
        private val textWarranty: TextView = itemView.findViewById(R.id.text_warranty_status)
        
        fun bind(product: Product) {
            textProductName.text = product.productName
            textSerialNumber.text = "S/N: ${product.serialNumber}"
            textCategory.text = product.productCategory
            textManufacturer.text = "Manufacturer: ${product.manufacturerName}"
            
            // Check warranty status
            val databaseHelper = DatabaseHelper.getInstance(itemView.context)
            val warrantyValid = databaseHelper.isWarrantyValid(product)
            
            // Set warranty text and color
            val warrantyText = if (warrantyValid) "Warranty: Active" else "Warranty: Expired"
            textWarranty.text = warrantyText
            
            val warrantyColor = if (warrantyValid) 
                itemView.context.getColor(R.color.warranty_valid) 
            else 
                itemView.context.getColor(R.color.warranty_expired)
            
            textWarranty.setTextColor(warrantyColor)
        }
    }
}
