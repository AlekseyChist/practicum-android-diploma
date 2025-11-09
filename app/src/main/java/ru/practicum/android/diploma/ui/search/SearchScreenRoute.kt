package ru.practicum.android.diploma.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import ru.practicum.android.diploma.presentation.search.SearchViewModel

@Composable
fun SearchScreenRoute(
    onVacancyClick: (String) -> Unit,
    onFilterClick: () -> Unit,
    viewModel: SearchViewModel
) {
    val state by viewModel.state.collectAsState()
    val vmQuery by viewModel.searchQuery.collectAsState()
    val hasActiveFilters by viewModel.hasActiveFilters.collectAsState()
    var currentQuery by remember(vmQuery) { mutableStateOf(vmQuery) }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.loadFilters()
        }
    }

    SearchScreen(
        state = state,
        query = currentQuery,
        hasActiveFilters = hasActiveFilters,
        onQueryChange = { query ->
            currentQuery = query
            viewModel.searchVacancies(query)
        },
        onClearClick = {
            currentQuery = ""
            viewModel.clearSearch()
        },
        onSearchClick = {
            // Поиск уже запускается через debounce в onQueryChange
        },
        onFilterClick = onFilterClick,
        onVacancyClick = { vacancy ->
            onVacancyClick(vacancy.id)
        },
        onLoadNextPage = viewModel::loadNextPage,
        onDismissPaginationError = viewModel::dismissPaginationError
    )
}
