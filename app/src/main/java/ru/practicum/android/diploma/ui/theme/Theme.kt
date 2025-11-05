package ru.practicum.android.diploma.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColorScheme(
    primary = Blue,
    onPrimary = White,
    secondary = Gray,
    onSecondary = Black,
    background = White,
    onBackground = Black,
    surface = LightGray,
    onSurface = Gray,
    error = Red,
    onError = White,
    scrim = BackgroundOverlay
)

private val DarkColorPalette = darkColorScheme(
    primary = Blue,
    onPrimary = Black,
    secondary = LightGray,
    onSecondary = Black,
    background = Black,
    onBackground = White,
    surface = LightGray,
    onSurface = Gray,
    error = Red,
    onError = Black,
    scrim = BackgroundOverlay
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colorScheme = colors,
        content = content,
        typography = AppTypography
    )
}
