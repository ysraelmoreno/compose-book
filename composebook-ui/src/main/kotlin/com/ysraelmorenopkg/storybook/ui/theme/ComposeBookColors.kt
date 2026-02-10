package com.ysraelmorenopkg.storybook.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Custom color system inspired by Storybook JS.
 * 
 * Dark theme colors matching the professional Storybook UI:
 * - Dark backgrounds for reduced eye strain
 * - Blue accent for interactive elements
 * - Subtle borders and separators
 * - High contrast text for readability
 */
@Immutable
data class ComposeBookColors(
    // Background colors
    val background: Color,
    val backgroundElevated: Color,
    val backgroundHighlight: Color,
    
    // Surface colors
    val surface: Color,
    val surfaceSelected: Color,
    val surfaceHover: Color,
    
    // Border colors
    val border: Color,
    val borderSubtle: Color,
    
    // Text colors
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    
    // Accent colors
    val accent: Color,
    val accentHover: Color,
    val accentPressed: Color,
    
    // Status colors
    val success: Color,
    val warning: Color,
    val error: Color,
    val info: Color
) {
    companion object {
        /**
         * Dark theme inspired by Storybook JS 7+
         */
        val Dark = ComposeBookColors(
            // Backgrounds - Deep dark blues/grays
            background = Color(0xFF1A1A1A),
            backgroundElevated = Color(0xFF262626),
            backgroundHighlight = Color(0xFF333333),
            
            // Surfaces
            surface = Color(0xFF262626),
            surfaceSelected = Color(0xFF2E3438),
            surfaceHover = Color(0xFF2A2A2A),
            
            // Borders
            border = Color(0xFF3D3D3D),
            borderSubtle = Color(0xFF2D2D2D),
            
            // Text
            textPrimary = Color(0xFFE8E8E8),
            textSecondary = Color(0xFFB3B3B3),
            textTertiary = Color(0xFF808080),
            
            // Accent - Storybook blue
            accent = Color(0xFF029CFD),
            accentHover = Color(0xFF1EA7FD),
            accentPressed = Color(0xFF0087E0),
            
            // Status
            success = Color(0xFF66BF3C),
            warning = Color(0xFFFFC400),
            error = Color(0xFFFF4785),
            info = Color(0xFF029CFD)
        )
        
        /**
         * Light theme for daylight usage
         */
        val Light = ComposeBookColors(
            // Backgrounds
            background = Color(0xFFF6F9FC),
            backgroundElevated = Color(0xFFFFFFFF),
            backgroundHighlight = Color(0xFFF0F0F0),
            
            // Surfaces
            surface = Color(0xFFFFFFFF),
            surfaceSelected = Color(0xFFE3F3FF),
            surfaceHover = Color(0xFFF5F5F5),
            
            // Borders
            border = Color(0xFFE0E0E0),
            borderSubtle = Color(0xFFF0F0F0),
            
            // Text
            textPrimary = Color(0xFF2E3438),
            textSecondary = Color(0xFF5C6870),
            textTertiary = Color(0xFF999999),
            
            // Accent
            accent = Color(0xFF029CFD),
            accentHover = Color(0xFF1EA7FD),
            accentPressed = Color(0xFF0087E0),
            
            // Status
            success = Color(0xFF66BF3C),
            warning = Color(0xFFFFAB00),
            error = Color(0xFFFF4785),
            info = Color(0xFF029CFD)
        )
    }
}
