package com.example.energymanagementapp.ui.accessibility

data class AccessibilitySettings(
    val enabled: Boolean = false
) {
    val fontMultiplier: Float
        get() = if (enabled) 1.2f else 1f

    val buttonHeight: Int
        get() = if (enabled) 64 else 56

    val horizontalPadding: Int
        get() = if (enabled) 28 else 24
}