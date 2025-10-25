package ru.practicum.android.diploma.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColorScheme(
    primary = Blue,
    onPrimary = WhiteUniversal,
    secondary = Gray,
    onSecondary = BlackUniversal,
    background = WhiteUniversal,
    onBackground = BlackUniversal,
    surface = LightGray,
    onSurface = Gray,
    error = Red,
    onError = WhiteUniversal
)

private val DarkColorPalette = darkColorScheme(
    primary = Blue,
    onPrimary = BlackUniversal,
    secondary = LightGray,
    onSecondary = BlackUniversal,
    background = BlackUniversal,
    onBackground = WhiteUniversal,
    surface = Gray,
    onSurface = WhiteUniversal,
    error = Red,
    onError = BlackUniversal
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
