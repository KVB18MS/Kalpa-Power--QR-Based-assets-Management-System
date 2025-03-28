# Kalpa Power QR Manager

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

## Project Structure
- **adapters/**: RecyclerView adapters for product and fault lists
- **database/**: SQLite database configuration and data access
  - `DatabaseHelper.java`: Central database management with transaction support
  - `ProductContract.java`: Schema definition for product table
  - `FaultContract.java`: Schema definition for fault reports table
- **model/**: Data model classes for products and faults
  - `Product.java`: Comprehensive product data model with JSON conversion
  - `Fault.java`: Fault reporting data model
- **ui/**: Activity and fragments for user interface
  - `dashboard/`: Inventory dashboard and listing views
  - `details/`: Product detail views
  - `generate/`: QR code generation forms and previews
  - `scan/`: QR code scanning functionality
  - `fault/`: Fault reporting forms and lists
- **utils/**: Utility classes for QR code generation and scanning
  - `QRCodeGenerator.java`: Enhanced QR code generation with labeling
  - `QRCodeScanner.java`: Improved scanning with error handling
  - `ExportUtils.java`: Data export functionality for inventory reports

## Setup and Build Instructions

### Prerequisites
- Android Studio Ladybug version (2023.3.1 or later)
- JDK 11 or higher
- Android SDK (minimum API level 24 / Android 7.0)

### Clone the Repository
```bash
git clone <repository-url>
cd KalpaPowerQRManager
```

### Building the Project in Android Studio Ladybug

1. **Open the project in Android Studio**
   - Start Android Studio Ladybug
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned repository and select it

2. **Sync Gradle Files**
   - Android Studio will automatically try to sync the project with Gradle files
   - If this doesn't happen automatically, click on "Sync Project with Gradle Files" button in the toolbar
   - If you see a message about migrating to Gradle 8, click "Don't remind me again for this project"

3. **Enable AndroidX Libraries**
   - Verify gradle.properties contains the following lines:
     ```
     android.useAndroidX=true
     android.enableJetifier=true
     ```
   - These ensure proper migration to AndroidX libraries

4. **Check Safe Args Plugin Configuration**
   - Ensure the top-level build.gradle has the navigation-safe-args-gradle-plugin dependency
   - Verify that the app/build.gradle applies the 'androidx.navigation.safeargs' plugin

5. **Build the Project**
   - Select "Build > Make Project" from the menu
   - Or use the keyboard shortcut (Ctrl+F9 on Windows/Linux, Cmd+F9 on Mac)

6. **Running on an Emulator or Physical Device**
   - Connect your Android device via USB with USB debugging enabled
   - Or set up an Android Virtual Device (AVD) using the AVD Manager
   - Select "Run > Run 'app'" from the menu

### Ladybug-Specific Adjustments

If you encounter issues with Android Studio Ladybug version, try these additional steps:

1. **Update Kotlin Version (if needed)**
   - If you see Kotlin version incompatibility errors, go to:
   - "Tools > Kotlin > Configure Kotlin Plugin Updates"
   - Select the latest stable version and apply

2. **Update Build Tools**
   - Go to "Tools > SDK Manager > SDK Tools" tab
   - Make sure "Android SDK Build-Tools" is updated to the latest version

3. **Invalidate Caches/Restart**
   - If you still have issues, go to "File > Invalidate Caches / Restart"
   - Select "Invalidate and Restart"

### Troubleshooting Common Issues

1. **Gradle Sync Failed**
   - Check your internet connection
   - Make sure you have the required SDK components installed
   - Try clicking on specific error messages in the "Build" tab for more details
   - Try "File > Sync Project with Gradle Files" again

2. **Build Failed**
   - Look at the error messages in the "Build" tab
   - If you see "Log class not found" errors, verify android.util.Log is properly imported
   - If you see "Contract class not found" errors, check for proper imports of ProductContract and FaultContract
   - Ensure all dependencies are available and properly synchronized

3. **App Crashes During Launch**
   - Check Logcat for detailed error messages
   - Verify that all required permissions are properly granted
   - Ensure the device meets minimum API level requirements (24+)
   - Check for database initialization errors in the logs

## Usage Guide

### Generating QR Codes
1. Navigate to the "Generate QR" tab
2. Enter product details (name, serial number, manufacturer, etc.)
3. Click "Generate QR Code"
4. Use the "Print" or "Share" buttons to save or distribute the QR code

### Scanning QR Codes
1. Navigate to the "Scan QR" tab
2. Point the camera at a Kalpa Power product QR code
3. View the product details that appear after successful scanning

### Managing Inventory
1. Navigate to the "Dashboard" tab to see all registered products
2. Use filters to sort or search for specific products
3. Tap on any product to view detailed information

### Reporting Faults
1. Scan a product's QR code or find it in the inventory
2. View the product details screen
3. Click on "Report Fault"
4. Fill in the fault details and submit

### Exporting Inventory Data
1. Navigate to the "Dashboard" tab
2. Click on the "Export" button in the toolbar
3. The app will generate a CSV file containing all inventory data
4. Choose how to share or save the export file from the share dialog
5. The exported data includes all product information, warranty status, and timestamps

## Permissions Required
- Camera: For scanning QR codes
- Storage (Android 9 and below): For saving QR code images and export files

## Technical Details

### Database Structure
The app uses SQLite for local storage with two primary tables:

1. **Products Table**: Stores all product information
   - Product details (name, category, serial number)
   - Manufacturing information (date, manufacturer, technical specs)
   - Warranty information (period, validity calculations)
   - Tracking data (QR code creation date, last scanned timestamp)

2. **Faults Table**: Records all reported faults
   - Fault details (description, severity)
   - Reporting information (date, technician name)
   - Foreign key relationship to the Products table

### Database Transactions
The app implements proper transaction support for all database operations:

- **Data Consistency**: All operations either complete entirely or are rolled back completely
- **Validation**: Input validation is performed before any database operation
- **Error Handling**: Comprehensive error logging and exception handling for database operations
- **Referential Integrity**: Foreign key constraints ensure data relationships remain valid

### CSV Export Format
The CSV export includes the following fields:
- ID
- Product Name
- Category
- Serial Number
- Manufacturer
- Manufacturing Date
- Vendor
- Warranty Period (Years)
- Sell Date
- Technical Specifications
- Notes
- QR Code Created Date
- Last Scanned Date
- Warranty Status (Active/Expired)

## App Icons

The application uses custom-designed icons that represent Kalpa Power's solar focus:

### Icon Design Elements
- **Green Background (#4CAF50)**: Represents the eco-friendly, sustainable nature of solar power
- **Solar Panel Graphic**: The main design element showcasing the company's solar focus
- **Sun Rays**: Symbolizes solar energy and power generation
- **QR Code Element**: Represents the app's core inventory management functionality

### Icon Resources
- All icon densities are included for both standard square and round icons:
  - mdpi (48x48)
  - hdpi (72x72)
  - xhdpi (96x96)
  - xxhdpi (144x144)
  - xxxhdpi (192x192)

### Icon Source Files
The following source files are included for future modifications:
- `icon_template.svg`: Standard icon template (square)
- `icon_template_round.svg`: Round icon template
- `kalpa_power_icons.zip`: Complete icon package with all densities

### Implementation
The icons are properly referenced in the AndroidManifest.xml file:
```xml
android:icon="@mipmap/ic_launcher"
android:roundIcon="@mipmap/ic_launcher_round"
```

## Contributing
1. Fork the repository
2. Create your feature branch: `git checkout -b feature/my-new-feature`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/my-new-feature`
5. Submit a pull request