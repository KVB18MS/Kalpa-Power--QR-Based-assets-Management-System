# Kalpa Power App Icons

This zip file contains Android launcher icons for the Kalpa Power app with a solar panel theme using the green color #4CAF50.

## Contents

The icons are organized in the standard Android mipmap directory structure:

### Regular Icons (ic_launcher.png)
- mipmap-mdpi: 48x48 pixels
- mipmap-hdpi: 72x72 pixels
- mipmap-xhdpi: 96x96 pixels
- mipmap-xxhdpi: 144x144 pixels
- mipmap-xxxhdpi: 192x192 pixels

### Round Icons (ic_launcher_round.png)
- mipmap-mdpi: 48x48 pixels
- mipmap-hdpi: 72x72 pixels
- mipmap-xhdpi: 96x96 pixels
- mipmap-xxhdpi: 144x144 pixels
- mipmap-xxxhdpi: 192x192 pixels

## Installation

To use these icons in your Android project:
1. Copy the mipmap folders to your Android project's `res` directory
2. Ensure your AndroidManifest.xml references these icons:
```xml
<application
    android:icon="@mipmap/ic_launcher"
    android:roundIcon="@mipmap/ic_launcher_round"
    ... >
</application>
```

## Design Notes

These icons feature:
- A green (#4CAF50) circular background representing Kalpa Power's eco-friendly mission
- A solar panel design with cell grid pattern
- Sunlight rays to symbolize solar energy
- A QR code element to represent the app's inventory management functionality

The source SVG files are also included if you need to make further adjustments.