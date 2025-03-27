package com.kalpapower.qrmanager.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Model class representing a solar product
 */
public class Product {
    private long id;
    private String productName;
    private String productCategory;
    private String serialNumber;
    private String manufacturerName;
    private Date manufacturingDate;
    private String vendorName;
    private int warrantyPeriod; // in years
    private Date sellDate;
    private String technicalSpecs;
    private String additionalNotes;
    private Date qrCodeCreatedDate;
    private Date lastScanned;

    // Default constructor
    public Product() {
    }

    // Constructor with essential fields
    public Product(String productName, String productCategory, String serialNumber,
                  String manufacturerName, Date manufacturingDate, String vendorName,
                  int warrantyPeriod) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.serialNumber = serialNumber;
        this.manufacturerName = manufacturerName;
        this.manufacturingDate = manufacturingDate;
        this.vendorName = vendorName;
        this.warrantyPeriod = warrantyPeriod;
    }

    // Full constructor
    public Product(String productName, String productCategory, String serialNumber,
                  String manufacturerName, Date manufacturingDate, String vendorName,
                  int warrantyPeriod, Date sellDate, String technicalSpecs,
                  String additionalNotes) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.serialNumber = serialNumber;
        this.manufacturerName = manufacturerName;
        this.manufacturingDate = manufacturingDate;
        this.vendorName = vendorName;
        this.warrantyPeriod = warrantyPeriod;
        this.sellDate = sellDate;
        this.technicalSpecs = technicalSpecs;
        this.additionalNotes = additionalNotes;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public Date getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(Date manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public Date getSellDate() {
        return sellDate;
    }

    public void setSellDate(Date sellDate) {
        this.sellDate = sellDate;
    }

    public String getTechnicalSpecs() {
        return technicalSpecs;
    }

    public void setTechnicalSpecs(String technicalSpecs) {
        this.technicalSpecs = technicalSpecs;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public Date getQrCodeCreatedDate() {
        return qrCodeCreatedDate;
    }

    public void setQrCodeCreatedDate(Date qrCodeCreatedDate) {
        this.qrCodeCreatedDate = qrCodeCreatedDate;
    }

    public Date getLastScanned() {
        return lastScanned;
    }

    public void setLastScanned(Date lastScanned) {
        this.lastScanned = lastScanned;
    }

    /**
     * Convert product to JSON for QR code generation
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("productName", productName);
        json.put("productCategory", productCategory);
        json.put("serialNumber", serialNumber);
        json.put("manufacturerName", manufacturerName);
        
        if (manufacturingDate != null) {
            json.put("manufacturingDate", manufacturingDate.getTime());
        }
        
        json.put("vendorName", vendorName);
        json.put("warrantyPeriod", warrantyPeriod);
        
        if (sellDate != null) {
            json.put("sellDate", sellDate.getTime());
        }
        
        json.put("technicalSpecs", technicalSpecs);
        json.put("additionalNotes", additionalNotes);
        
        return json;
    }

    /**
     * Create product from JSON (from QR code scan)
     */
    public static Product fromJSON(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        
        Product product = new Product();
        
        if (json.has("id")) {
            product.setId(json.getLong("id"));
        }
        
        product.setProductName(json.getString("productName"));
        product.setProductCategory(json.getString("productCategory"));
        product.setSerialNumber(json.getString("serialNumber"));
        product.setManufacturerName(json.getString("manufacturerName"));
        
        if (json.has("manufacturingDate")) {
            product.setManufacturingDate(new Date(json.getLong("manufacturingDate")));
        }
        
        product.setVendorName(json.getString("vendorName"));
        product.setWarrantyPeriod(json.getInt("warrantyPeriod"));
        
        if (json.has("sellDate") && !json.isNull("sellDate")) {
            product.setSellDate(new Date(json.getLong("sellDate")));
        }
        
        if (json.has("technicalSpecs") && !json.isNull("technicalSpecs")) {
            product.setTechnicalSpecs(json.getString("technicalSpecs"));
        }
        
        if (json.has("additionalNotes") && !json.isNull("additionalNotes")) {
            product.setAdditionalNotes(json.getString("additionalNotes"));
        }
        
        return product;
    }
}
