# Kalpa Power QR Based assets management

<img src="generated-icon.png" alt="Kalpa Power App Icon" width="100" align="right"/>
## Overview
Kalpa Power QR Manager is an Android application developed for Kalpa Power, a solar-based company, to manage their inventory of solar products using QR codes. The app allows for QR code generation, scanning, product management, and fault reporting.

## Features
- **QR Code Generation**: Create unique QR codes for each solar product with detailed product information
- **QR Code Scanning**: Scan QR codes to retrieve product information and history
- **Inventory Management**: View and manage all registered solar products
- **Fault Reporting**: Record and track issues reported by technicians during maintenance
- **Product Details**: Access comprehensive information about each product including warranty status
- **Data Export**: Export inventory data as CSV files for external analysis and backup
- **Printable QR Codes**: Generate printer-friendly QR codes with product labels

## Recent Enhancements
1. **Improved QR Code Generator**:
   - Enhanced QR code quality and error resistance
   - Added product information labels directly on the QR code image
   - Improved file saving with better error handling and storage options

2. **Enhanced QR Code Scanner**:
   - Added callbacks for better error handling and user feedback
   - Improved permission handling for both camera and storage
   - Added compatibility with device feature detection

3. **Robust Database Operations**:
   - Added transaction support for all database operations
   - Implemented comprehensive input validation
   - Enhanced error handling and reporting

4. **Data Export Feature**:
   - Added CSV export functionality for inventory data
   - Implemented file sharing capabilities for exported data
   - Added proper CSV encoding for all special characters

5. **Custom App Icons**:
   - Added custom green solar-themed launcher icons for all density buckets
   - Implemented both regular square and round icon versions
   - Designed with solar panel imagery and QR code elements representing app functionality

## Technology Stack
- **Languages**: Java and Kotlin
- **Database**: SQLite for local storage with transaction support
- **QR Code Libraries**: ZXing for generation and scanning
- **UI Framework**: Android Navigation Component for fragment navigation
- **Image Processing**: Canvas API for QR code enhancement
