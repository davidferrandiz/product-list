package com.davidferrandiz.mangostore.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MangoBlack = Color(0xFF141414)
private val MangoAmber = Color(0xFFE0993C)
private val MangoAmberDark = Color(0xFFF2B863)

private val LightColors = lightColorScheme(
    primary = MangoBlack,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF2F2F2),
    onPrimaryContainer = MangoBlack,
    secondary = MangoAmber,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFBEEDC),
    onSecondaryContainer = Color(0xFF6B4715),
    background = Color(0xFFFBFBFB),
    onBackground = MangoBlack,
    surface = Color.White,
    onSurface = MangoBlack,
    surfaceVariant = Color(0xFFF2F2F2),
    onSurfaceVariant = Color(0xFF6B6B6B),
    outlineVariant = Color(0xFFE4E4E4),
    error = Color(0xFFB3261E),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFF5F5F5),
    onPrimary = MangoBlack,
    primaryContainer = Color(0xFF2A2A2A),
    onPrimaryContainer = Color(0xFFF5F5F5),
    secondary = MangoAmberDark,
    onSecondary = MangoBlack,
    secondaryContainer = Color(0xFF3D2E14),
    onSecondaryContainer = Color(0xFFF7DDB6),
    background = Color(0xFF121212),
    onBackground = Color(0xFFEDEDED),
    surface = Color(0xFF1C1C1C),
    onSurface = Color(0xFFEDEDED),
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFB0B0B0),
    outlineVariant = Color(0xFF333333),
    error = Color(0xFFF2B8B5),
)

@Composable
fun MangoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
