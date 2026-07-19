package com.activemap.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme

data class ActiveMapColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val error: Color,
    val onError: Color,
    val isDark: Boolean
)

val LightColors = ActiveMapColors(
    primary = Color(0xFF1A73E8),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD3E3FD),
    onPrimaryContainer = Color(0xFF041E49),
    secondary = Color(0xFF5F6368),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8EAED),
    onSecondaryContainer = Color(0xFF1F1F1F),
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1F1F1F),
    surface = Color.White,
    onSurface = Color(0xFF1F1F1F),
    error = Color(0xFFD93025),
    onError = Color.White,
    isDark = false
)

val DarkColors = ActiveMapColors(
    primary = Color(0xFF8AB4F8),
    onPrimary = Color(0xFF062E6F),
    primaryContainer = Color(0xFF1A56DB),
    onPrimaryContainer = Color(0xFFD3E3FD),
    secondary = Color(0xFF9AA0A6),
    onSecondary = Color(0xFF3C4043),
    secondaryContainer = Color(0xFF5F6368),
    onSecondaryContainer = Color(0xFFE8EAED),
    background = Color(0xFF1F1F1F),
    onBackground = Color(0xFFE8EAED),
    surface = Color(0xFF2D2D2D),
    onSurface = Color(0xFFE8EAED),
    error = Color(0xFFF28B82),
    onError = Color(0xFF601410),
    isDark = true
)

@Composable
fun ActiveMapTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = colors.primary,
            onPrimary = colors.onPrimary,
            primaryContainer = colors.primaryContainer,
            onPrimaryContainer = colors.onPrimaryContainer,
            secondary = colors.secondary,
            onSecondary = colors.onSecondary,
            secondaryContainer = colors.secondaryContainer,
            onSecondaryContainer = colors.onSecondaryContainer,
            background = colors.background,
            onBackground = colors.onBackground,
            surface = colors.surface,
            onSurface = colors.onSurface,
            error = colors.error,
            onError = colors.onError
        )
    } else {
        lightColorScheme(
            primary = colors.primary,
            onPrimary = colors.onPrimary,
            primaryContainer = colors.primaryContainer,
            onPrimaryContainer = colors.onPrimaryContainer,
            secondary = colors.secondary,
            onSecondary = colors.onSecondary,
            secondaryContainer = colors.secondaryContainer,
            onSecondaryContainer = colors.onSecondaryContainer,
            background = colors.background,
            onBackground = colors.onBackground,
            surface = colors.surface,
            onSurface = colors.onSurface,
            error = colors.error,
            onError = colors.onError
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
