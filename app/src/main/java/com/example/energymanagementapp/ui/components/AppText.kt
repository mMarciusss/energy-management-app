package com.example.energymanagementapp.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp
import com.example.energymanagementapp.ui.accessibility.LocalAccessibilitySettings

@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    val accessibility = LocalAccessibilitySettings.current

    val scaledStyle = if (style.fontSize.isSpecified) {
        style.copy(
            fontSize = (style.fontSize.value * accessibility.fontMultiplier).sp,
            fontWeight = fontWeight ?: style.fontWeight
        )
    } else {
        style.copy(
            fontWeight = fontWeight ?: style.fontWeight
        )
    }

    Text(
        text = text,
        modifier = modifier,
        style = scaledStyle,
        color = color,
        maxLines = maxLines,
        overflow = overflow
    )
}