package com.davidferrandiz.mangostore.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF1A1A1A),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFFE8A33D),
    onSecondary = Color(0xFF1A1A1A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1A1A),
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1A1A1A),
)

@Composable
fun MangoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
