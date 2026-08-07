package com.davidferrandiz.mangostore.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Tema de la app, en :core:ui para que TODAS las features compartan
 * la misma identidad visual sin duplicarla.
 *
 * Paralelo iOS: tu extensión de Color/Font + un ViewModifier de tema
 * en el paquete DesignSystem.
 */

// Paleta mínima inspirada en la marca (negro elegante + acento mango).
// Material 3 deriva el resto de roles de color a partir de estos.
private val LightColors = lightColorScheme(
    primary = Color(0xFF1A1A1A),        // negro Mango: botones, elementos activos
    onPrimary = Color(0xFFFFFFFF),      // contenido sobre primary
    secondary = Color(0xFFE8A33D),      // acento "mango": favoritos, detalles
    onSecondary = Color(0xFF1A1A1A),
    surface = Color(0xFFFFFFFF),        // fondos de cards/barras
    onSurface = Color(0xFF1A1A1A),
    background = Color(0xFFFAFAFA),     // fondo general ligeramente roto
    onBackground = Color(0xFF1A1A1A),
)

/**
 * Envoltorio de MaterialTheme: todo Composable bajo él lee estos
 * colores vía MaterialTheme.colorScheme.*
 *
 * Decisión consciente: solo tema claro en la prueba (alcance controlado);
 * añadir darkColorScheme + isSystemInDarkTheme() sería el siguiente paso
 * y así se defiende si lo preguntan.
 */
@Composable
fun MangoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
