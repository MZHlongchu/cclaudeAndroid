#!/usr/bin/env python3
"""
CClaude Android Icon Generator
Generates PNG icons from SVG source

Requirements: cairosvg (pip install cairosvg)
Or use online converter: https://convertio.co/svg-png/
"""

import os
import sys

# Icon sizes for Android
SIZES = {
    'mipmap-mdpi': 48,
    'mipmap-hdpi': 72,
    'mipmap-xhdpi': 96,
    'mipmap-xxhdpi': 144,
    'mipmap-xxxhdpi': 192,
}

PLAYSTORE_SIZE = 512

def generate_icons():
    svg_path = "docs/icons/cclaude-icon.svg"
    
    if not os.path.exists(svg_path):
        print(f"Error: {svg_path} not found")
        return False
    
    try:
        import cairosvg
    except ImportError:
        print("Error: cairosvg not installed")
        print("Install with: pip install cairosvg")
        print("\nAlternative: Use online converter:")
        print("https://convertio.co/svg-png/")
        return False
    
    # Generate mipmap icons
    for folder, size in SIZES.items():
        output_path = f"app/src/main/res/{folder}/ic_launcher.png"
        output_round_path = f"app/src/main/res/{folder}/ic_launcher_round.png"
        
        print(f"Generating {size}x{size} -> {folder}")
        
        cairosvg.svg2png(
            url=svg_path,
            write_to=output_path,
            output_width=size,
            output_height=size
        )
        
        # Copy to round icon
        import shutil
        shutil.copy(output_path, output_round_path)
    
    # Generate Play Store icon
    print(f"Generating {PLAYSTORE_SIZE}x{PLAYSTORE_SIZE} -> Play Store")
    cairosvg.svg2png(
        url=svg_path,
        write_to="app/src/main/ic_launcher-playstore.png",
        output_width=PLAYSTORE_SIZE,
        output_height=PLAYSTORE_SIZE
    )
    
    print("\n✅ Icons generated successfully!")
    return True

if __name__ == "__main__":
    generate_icons()
