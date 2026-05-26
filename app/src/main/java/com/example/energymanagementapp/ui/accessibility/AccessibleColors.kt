package com.example.energymanagementapp.ui.accessibility

import androidx.compose.ui.graphics.Color

data class AppColors(
    val background: Color,
    val card: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val primary: Color,
    val secondary: Color,
    val accent: Color,
    val disabledBackground: Color,
    val disabledText: Color,
    val successBackground: Color,
    val border: Color
)

object AppColorPalettes {

    val Default = AppColors(
        background = Color(0xFFF7F7F7),
        card = Color.White,
        textPrimary = Color.Black,
        textSecondary = Color(0xFF6B6B6B),
        primary = Color(0xFF6BCB9A),
        secondary = Color(0xFF6982B5),
        accent = Color(0xFF6C63FF),
        disabledBackground = Color(0xFFF0F0F0),
        disabledText = Color.Gray,
        successBackground = Color(0xFFE8F5EE),
        border = Color(0xFF6BCB9A)
    )

    val Accessible = AppColors(
        background = Color(0xFFF2F4F7),          // šaltesnis fonas, ne grynai baltas
        card = Color(0xFFFFFFFF),
        textPrimary = Color(0xFF111111),
        textSecondary = Color(0xFF3F3F3F),
        primary = Color(0xFF006B4F),             // tamsesnė žalia, geresnis kontrastas
        secondary = Color(0xFF2F4F8F),           // tamsesnė mėlyna
        accent = Color(0xFF3B35B3),              // tamsesnė violetinė
        disabledBackground = Color(0xFFD6D6D6),
        disabledText = Color(0xFF4A4A4A),
        successBackground = Color(0xFFDFF3E8),
        border = Color(0xFF006B4F)
    )
}