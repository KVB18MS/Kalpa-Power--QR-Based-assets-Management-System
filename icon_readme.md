# Kalpa Power App Icons

## Overview
This document provides information about the custom launcher icons created for the Kalpa Power QR Manager Android application.

## Icon Design
The icons were designed specifically for Kalpa Power with the following elements:

- **Green Background (#4CAF50)**: Chosen to represent the eco-friendly, sustainable nature of solar power
- **Solar Panel Graphic**: The main design element showcasing the company's solar focus
- **Sun Rays**: Symbolizes solar energy and power generation
- **QR Code Element**: Represents the app's core inventory management functionality

## Icon Files

### Standard Icons (ic_launcher.png)
Square launcher icons for standard display:

| Density | Size     | Location                                 |
|---------|----------|------------------------------------------|
| mdpi    | 48x48px  | `/app/src/main/res/mipmap-mdpi/`        |
| hdpi    | 72x72px  | `/app/src/main/res/mipmap-hdpi/`        |
| xhdpi   | 96x96px  | `/app/src/main/res/mipmap-xhdpi/`       |
| xxhdpi  | 144x144px| `/app/src/main/res/mipmap-xxhdpi/`      |
| xxxhdpi | 192x192px| `/app/src/main/res/mipmap-xxxhdpi/`     |

### Round Icons (ic_launcher_round.png)
Circular launcher icons for devices that support round icons:

| Density | Size     | Location                                 |
|---------|----------|------------------------------------------|
| mdpi    | 48x48px  | `/app/src/main/res/mipmap-mdpi/`        |
| hdpi    | 72x72px  | `/app/src/main/res/mipmap-hdpi/`        |
| xhdpi   | 96x96px  | `/app/src/main/res/mipmap-xhdpi/`       |
| xxhdpi  | 144x144px| `/app/src/main/res/mipmap-xxhdpi/`      |
| xxxhdpi | 192x192px| `/app/src/main/res/mipmap-xxxhdpi/`     |

## Source Files
The following source files are included for future modifications:

- `icon_template.svg`: Vector source file for the standard (square) icon
- `icon_template_round.svg`: Vector source file for the round icon
- `kalpa_power_icons.zip`: Archive containing all icon files and source SVGs

## Preview Images
For quick reference, the following preview images are included:

- `icon_preview.png`: Preview of the standard launcher icon
- `icon_round_preview.png`: Preview of the round launcher icon

## AndroidManifest.xml Configuration
The icons are properly referenced in the AndroidManifest.xml file with these attributes:

```xml
android:icon="@mipmap/ic_launcher"
android:roundIcon="@mipmap/ic_launcher_round"
```

## Icon Viewer
For an interactive display of all icon versions, open the `icon_viewer.html` file in a web browser. This page shows all icon densities and provides additional information about the design elements.

## Design Specifications

### Colors
- **Primary Green**: #4CAF50 (RGB: 76, 175, 80)
- **Dark Green Shade**: #388E3C (RGB: 56, 142, 60)
- **Light Green Tint**: #A5D6A7 (RGB: 165, 214, 167)
- **Text/Shadow**: #263238 (RGB: 38, 50, 56)

### Technical Details
- **Format**: PNG with transparency
- **Compression**: Optimized for Android display
- **Fore/Background Separation**: Designed for adaptive icon compatibility
- **Visual Balance**: Maintains visual clarity at all sizes/densities

## Usage Guidelines
When updating or modifying these icons:

1. Always preserve the green solar theme to maintain brand consistency
2. Ensure the QR code element remains visible as it represents core functionality
3. Test icon visibility against different wallpapers and in app drawer
4. When regenerating from SVG source, use appropriate export settings for each density
5. Update all density levels to maintain a consistent appearance across devices

## Troubleshooting
If the icons don't appear correctly:

1. Verify all density folders have both icon variants
2. Check AndroidManifest.xml for proper references
3. Clear the app cache or reinstall after icon updates
4. For adaptive icon issues on newer Android versions, ensure foreground/background layers are properly defined