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


    SearchScreen(
        state = state,
        query = "",
        onQueryChange = { query ->
            viewModel.searchVacancies(query)
        },
        onClearClick = {
            viewModel.clearSearch()
        },
        onSearchClick = {
        },
        onFilterClick = onFilterClick,
        onVacancyClick = { vacancy ->
            onVacancyClick(vacancy.id)
        }
    )
}


