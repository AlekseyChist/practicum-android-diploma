package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.search.SearchState
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.ui.theme.AppTheme

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    val searchState by viewModel.state.collectAsState()
                    val searchQuery by viewModel.searchQuery.collectAsState()

                    SearchScreen(
                        state = searchState.toUiState(),
                        query = searchQuery,
                        onQueryChange = { query ->
                            viewModel.searchVacancies(query)
                        },
                        onClearClick = {
                            viewModel.clearSearch()
                        },

                        onSubmit = {
                            viewModel.forceSearch()
                        },
                        onFilterClick = {
                            findNavController().navigate(
                                R.id.action_searchFragment_to_filtersSettingsFragment
                            )
                        },
                        onVacancyClick = { vacancyId ->
                            // Передаем vacancyId в Bundle для VacancyFragment
                            val bundle = Bundle().apply {
                                putString("vacancyId", vacancyId)
                            }
                            findNavController().navigate(
                                R.id.action_searchFragment_to_vacancyFragment,
                                bundle
                            )
                        }
                    )
                }
            }
        }
    }

    // функцию конвертации состояний
    private fun SearchState.toUiState(): SearchUiState {
        return when (this) {
            is SearchState.Initial -> SearchUiState.Idle
            is SearchState.Typing -> SearchUiState.Typing
            is SearchState.Loading -> SearchUiState.Loading
            is SearchState.LoadingNextPage -> SearchUiState.Loading
            is SearchState.Success -> SearchUiState.Success(
                items = this.vacancies.map {
                    VacancyUi(
                        id = it.id,
                        title = it.name ?: "Без названия",
                        city = it.area?.name ?: "",
                        salary = it.salary?.let { salary ->
                            // Форматирование зарплаты
                            buildString {
                                salary.from?.let { from -> append("$from") }
                                if (salary.from != null && salary.to != null) append(" - ")
                                salary.to?.let { to -> append("$to") }
                                salary.currency?.let { currency -> append(" $currency") }
                            }.ifEmpty { null }
                        },
                        company = it.employer?.name
                    )
                }
            )

            is SearchState.EmptyResult -> SearchUiState.EmptyResult
            is SearchState.Error -> SearchUiState.Error(this.message)
            is SearchState.NoConnection -> SearchUiState.NoInternet
        }
    }
}
