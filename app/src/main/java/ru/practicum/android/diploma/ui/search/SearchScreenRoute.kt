package ru.practicum.android.diploma.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.presentation.search.SearchState
import ru.practicum.android.diploma.presentation.search.SearchViewModel

/**
 * Route компонент который связывает SearchScreen (UI) и SearchViewModel (логика)
 * Этот компонент должен использоваться в Navigation вместо прямого вызова SearchScreen
 */
@Composable
fun SearchScreenRoute(
    onVacancyClick: (String) -> Unit,
    onFilterClick: () -> Unit,
    viewModel: SearchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val uiState = mapToUiState(state)
    val query by viewModel.query.collectAsState()

    SearchScreen(
        state = uiState,
        query = query,
        onQueryChange = { q -> viewModel.searchVacancies(q) },
        onClearClick = { viewModel.clearSearch() },
        onSearchClick = { viewModel.searchNow(query) },
        onFilterClick = onFilterClick,
        onVacancyClick = { vacancy -> onVacancyClick(vacancy.id) }
    )
}

/**
 * Маппинг SearchState (от ViewModel) → SearchUiState (для UI)
 */
private fun mapToUiState(state: SearchState): SearchUiState {
    return when (state) {
        is SearchState.Initial -> SearchUiState.Idle

        is SearchState.Loading -> SearchUiState.Loading

        is SearchState.LoadingNextPage -> {
            SearchUiState.Success(items = state.currentVacancies)
        }

        is SearchState.Success -> {
            SearchUiState.Success(items = state.vacancies)
        }

        is SearchState.EmptyResult -> SearchUiState.EmptyResult

        is SearchState.NoConnection -> SearchUiState.NoInternet

        is SearchState.Error -> SearchUiState.Error(message = state.message)

        is SearchState.Typing -> SearchUiState.Typing
    }
}
