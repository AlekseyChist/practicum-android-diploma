package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.dto.requests.VacancySearchRequest
import ru.practicum.android.diploma.domain.api.SearchVacanciesUseCase
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
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private var currentSearchQuery: String = ""
    private var currentFilters: VacancySearchRequest? = null
    private var searchJob: Job? = null

    /**
     *
     * Поиск вакансий с debounce
     * @param query - поисковый запрос
     * @param filters - фильтры поиска (регион, отрасль, зарплата)
     */
    fun searchVacancies(
        query: String,
        filters: VacancySearchRequest? = null
    ) {
        currentSearchQuery = query.trim()
        currentFilters = filters

        if (currentSearchQuery.isEmpty()) {
            _state.value = SearchState.Initial
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(DEBOUNCE_DELAY_MS)
            performSearch(page = 0)
        }
    }

    /**
     * Загрузить следующую страницу результатов
     */
    fun loadNextPage() {
        val currentState = _state.value
        if (currentState !is SearchState.Success || !currentState.hasMorePages) {
            return
        }

        _state.value = SearchState.LoadingNextPage(
            currentVacancies = currentState.vacancies,
            currentPage = currentState.currentPage
        )

        viewModelScope.launch {
            performSearch(page = currentState.currentPage + 1)
        }
    }

    /**
     * Повторить последний поиск (при ошибке или отсутствии сети)
     */
    fun retry() {
        if (currentSearchQuery.isNotEmpty()) {
            _state.value = SearchState.Loading
            viewModelScope.launch {
                performSearch(page = 0)
            }
        }
    }

    /**
     * Выполнить поиск вакансий
     */
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

    private fun createSearchRequest(page: Int): VacancySearchRequest {
        return VacancySearchRequest(
            text = currentSearchQuery,
            area = currentFilters?.area,
            industry = currentFilters?.industry,
            salary = currentFilters?.salary,
            onlyWithSalary = currentFilters?.onlyWithSalary,
            page = page
        )
    }

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

        _state.value = when {
            isNoConnectionError(message) -> SearchState.NoConnection
            else -> SearchState.Error(message)
        }
    }

    private fun isNoConnectionError(message: String): Boolean {
        return message.contains("интернет", ignoreCase = true) ||
            message.contains("connection", ignoreCase = true) ||
            message.contains("подключения", ignoreCase = true)
    }

    /**
     * Очистить поиск и вернуться к начальному состоянию
     */
    fun clearSearch() {
        searchJob?.cancel()
        currentSearchQuery = ""
        currentFilters = null
        _state.value = SearchState.Initial
    }

    companion object {
        private const val DEBOUNCE_DELAY_MS = 500L
    }
}
