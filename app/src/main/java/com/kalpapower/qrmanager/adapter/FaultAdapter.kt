package com.kalpapower.qrmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kalpapower.qrmanager.R
import com.kalpapower.qrmanager.model.Fault
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Adapter for displaying fault reports in a RecyclerView
 */
class FaultAdapter : RecyclerView.Adapter<FaultAdapter.FaultViewHolder>() {
    
    private var faults: List<Fault> = ArrayList()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    
    fun setFaults(faults: List<Fault>) {
        this.faults = faults
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fault, parent, false)
        return FaultViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: FaultViewHolder, position: Int) {
        val fault = faults[position]
        holder.bind(fault)
    }
    
    override fun getItemCount(): Int = faults.size
    
    inner class FaultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textFaultDescription: TextView = itemView.findViewById(R.id.text_fault_description)
        private val textFaultDate: TextView = itemView.findViewById(R.id.text_fault_date)
        private val textSeverity: TextView = itemView.findViewById(R.id.text_severity)
        private val textTechnician: TextView = itemView.findViewById(R.id.text_technician)
        
        fun bind(fault: Fault) {
            textFaultDescription.text = fault.faultDescription
            
            if (fault.faultDate != null) {
                textFaultDate.text = "Reported on: ${dateFormat.format(fault.faultDate)}"
            } else {
                textFaultDate.text = "Reported on: Unknown date"
            }
            
            textSeverity.text = "Severity: ${fault.severity}"
            
            // Set color based on severity
            val severityColor = when (fault.severity) {
                Fault.SEVERITY_HIGH -> ContextCompat.getColor(itemView.context, R.color.severity_high)
                Fault.SEVERITY_MEDIUM -> ContextCompat.getColor(itemView.context, R.color.severity_medium)
                else -> ContextCompat.getColor(itemView.context, R.color.severity_low)
            }
            textSeverity.setTextColor(severityColor)
            
            textTechnician.text = "Technician: ${fault.technicianName}"
        }
    }
}
