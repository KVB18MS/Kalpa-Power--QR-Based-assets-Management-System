package com.kalpapower.qrmanager.model;

import java.util.Date;

/**
 * Model class representing a fault report
 */
public class Fault {
    private long id;
    private long productId;
    private String faultDescription;
    private Date faultDate;
    private String severity;
    private String technicianName;
    private Date reportDate;

    // Severity levels
    public static final String SEVERITY_LOW = "Low";
    public static final String SEVERITY_MEDIUM = "Medium";
    public static final String SEVERITY_HIGH = "High";

    // Default constructor
    public Fault() {
    }

    // Full constructor
    public Fault(long productId, String faultDescription, Date faultDate,
                String severity, String technicianName) {
        this.productId = productId;
        this.faultDescription = faultDescription;
        this.faultDate = faultDate;
        this.severity = severity;
        this.technicianName = technicianName;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public void setFaultDescription(String faultDescription) {
        this.faultDescription = faultDescription;
    }

    public Date getFaultDate() {
        return faultDate;
    }

    public void setFaultDate(Date faultDate) {
        this.faultDate = faultDate;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }
}
