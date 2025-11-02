package ru.practicum.android.diploma.ui.favorites.mock

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.practicum.android.diploma.presentation.favorites.FavoritesState

class FavoritesStateProvider : PreviewParameterProvider<FavoritesState> {
    override val values: Sequence<FavoritesState>
        get() = sequenceOf(
            FavoritesState.Initial,
            FavoritesState.Empty,
            FavoritesState.Content(mockVacancies),
            FavoritesState.Error("Error"),
            FavoritesState.Loading
        )
}
