package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.dto.requests.VacancySearchRequest
import ru.practicum.android.diploma.domain.api.SearchVacanciesUseCase
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.debounce
import java.net.ConnectException
import java.net.UnknownHostException
import ru.practicum.android.diploma.domain.models.VacancyUi
import ru.practicum.android.diploma.presentation.mappers.VacancyUiMapper

/**
 * ViewModel для экрана поиска вакансий
 * Использует VacancyUiMapper для преобразования Domain → UI моделей
 */
class SearchViewModel(
    private val searchVacanciesUseCase: SearchVacanciesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Initial)
    val state = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private var currentSearchQuery = ""
    private var currentFilters: VacancySearchRequest? = null

    private val debouncedSearch = debounce<String>(
        delayMillis = DEBOUNCE_DELAY_MS,
        coroutineScope = viewModelScope
    ) { query ->
        val trimmedQuery = query.trim()

        if (_searchQuery.value.isEmpty()) {
            _state.value = SearchState.Initial
        } else {
            currentSearchQuery = trimmedQuery
            startSearch()
        }
    }

    fun searchVacancies(query: String, filters: VacancySearchRequest? = null) {
        currentSearchQuery = query.trim()
        _searchQuery.value = currentSearchQuery
        currentFilters = filters

        if (currentSearchQuery.isEmpty()) {
            _state.value = SearchState.Initial
            return
        }

        _state.value = SearchState.Typing(currentSearchQuery)

        debouncedSearch(currentSearchQuery)
    }

    fun retry() {
        if (currentSearchQuery.isNotEmpty()) {
            startSearch()
        }
    }

    private fun startSearch() {
        _state.value = SearchState.Loading
        viewModelScope.launch {
            performSearch(page = 0)
        }
    }

    fun loadNextPage() {
        val currentState = _state.value
        if (currentState !is SearchState.Success || !currentState.hasMorePages) return

        _state.value = SearchState.LoadingNextPage(
            currentVacancies = currentState.vacancies,
            currentPage = currentState.currentPage
        )

        viewModelScope.launch {
            performSearch(currentState.currentPage + 1)
        }
    }

    private suspend fun performSearch(page: Int) {
        val request = createSearchRequest(page)
        searchVacanciesUseCase.execute(request)
            .onSuccess { result ->
                val vacanciesUi = VacancyUiMapper.mapToUi(result.vacancies)
                handleSearchSuccess(vacanciesUi, result.found, result.page, result.pages)
            }
            .onFailure { exception ->
                handleSearchFailure(exception)
            }
    }

    private fun createSearchRequest(page: Int) = VacancySearchRequest(
        text = currentSearchQuery,
        area = currentFilters?.area,
        industry = currentFilters?.industry,
        salary = currentFilters?.salary,
        onlyWithSalary = currentFilters?.onlyWithSalary,
        page = page
    )

    private fun handleSearchSuccess(
        vacancies: List<VacancyUi>,
        found: Int,
        page: Int,
        totalPages: Int
    ) {
        val currentState = _state.value
        if (vacancies.isEmpty() && page == 0) {
            _state.value = SearchState.EmptyResult(currentSearchQuery)
            return
        }

        val allVacancies = if (currentState is SearchState.LoadingNextPage) {
            currentState.currentVacancies + vacancies
        } else {
            vacancies
        }

        _state.value = SearchState.Success(
            vacancies = allVacancies,
            found = found,
            currentPage = page,
            totalPages = totalPages,
            hasMorePages = page < totalPages - 1
        )
    }

    private fun handleSearchFailure(exception: Throwable) {
        val message = exception.message ?: "Неизвестная ошибка"

        _state.value = when (exception) {
            is UnknownHostException,
            is ConnectException -> SearchState.NoConnection

            else -> SearchState.Error(message)
        }
    }

    fun clearSearch() {
        currentSearchQuery = ""
        _searchQuery.value = ""
        currentFilters = null
        _state.value = SearchState.Initial
    }

    private fun isNoConnectionError(message: String): Boolean {
        return message.contains("интернет", ignoreCase = true) ||
            message.contains("connection", ignoreCase = true) ||
            message.contains("подключения", ignoreCase = true)
    }

    companion object {
        private const val DEBOUNCE_DELAY_MS = 2000L
    }

    fun forceSearch() {
        debouncedSearch.cancelPending()
        if (currentSearchQuery.isNotEmpty()) {
            startSearch()
        }
    }
}
