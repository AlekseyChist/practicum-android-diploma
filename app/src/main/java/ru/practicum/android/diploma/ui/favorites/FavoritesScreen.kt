package ru.practicum.android.diploma.ui.favorites


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.practicum.android.diploma.ui.theme.AppTheme

@Composable
fun FavoritesScreen() {

}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    AppTheme {
        FavoritesScreen()
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun FavoritesScreenDarkPreview() {
    AppTheme {
        FavoritesScreen()
    }
}
