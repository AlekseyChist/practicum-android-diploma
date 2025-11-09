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
import ru.practicum.android.diploma.domain.api.GetFilterSettingsUseCase
import ru.practicum.android.diploma.domain.api.SearchVacanciesUseCase
import ru.practicum.android.diploma.domain.models.VacancyUi
import ru.practicum.android.diploma.domain.models.toApiParams
import ru.practicum.android.diploma.presentation.mappers.VacancyUiMapper

class SearchViewModel(
    private val searchVacanciesUseCase: SearchVacanciesUseCase,
    private val getFilterSettingsUseCase: GetFilterSettingsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Initial)
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private var currentSearchQuery: String = ""
    private var currentFilters: VacancySearchRequest? = null
    private var searchJob: Job? = null
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _hasActiveFilters = MutableStateFlow(false)
    val hasActiveFilters: StateFlow<Boolean> = _hasActiveFilters.asStateFlow()

    private var lastSuccessFound: Int = 0
    private var lastSuccessTotalPages: Int = 0

    init {
        // Загружаем фильтры при инициализации
        viewModelScope.launch {
            loadFilters()
        }
    }

    /**
     * Загрузить актуальные фильтры из SharedPreferences
     * ИЗМЕНЕНО: теперь это suspend-функция без внутреннего launch
     */
    suspend fun loadFilters() {
        val settings = getFilterSettingsUseCase.execute()
        val apiParams = settings.toApiParams()

        // Обновляем флаг активных фильтров
        _hasActiveFilters.value = settings.industry != null ||
            settings.expectedSalary != null ||
            settings.onlyWithSalary

        // Конвертируем в VacancySearchRequest
        currentFilters = VacancySearchRequest(
            text = null,
            area = apiParams.area,
            industry = apiParams.industry,
            salary = apiParams.salary,
            onlyWithSalary = apiParams.onlyWithSalary,
            page = 0
        )
    }

    /**
     * Перезагрузить фильтры и повторить последний поиск
     */
    fun reloadFiltersAndSearch() {
        viewModelScope.launch {
            // Загружаем новые фильтры
            loadFilters()

            // Если есть активный поисковый запрос - повторяем поиск
            if (currentSearchQuery.isNotEmpty()) {
                _state.value = SearchState.Loading
                performSearch(page = 0)
            }
        }
    }

    /**
     * Поиск вакансий с debounce
     */
    fun searchVacancies(query: String) {
        currentSearchQuery = query.trim()
        _searchQuery.value = query

        if (currentSearchQuery.isEmpty()) {
            _state.value = SearchState.Initial
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(DEBOUNCE_DELAY_MS)

            // Перезагружаем фильтры ПЕРЕД поиском и ждем завершения!
            loadFilters()

            // Устанавливаем Loading ПЕРЕД запросом
            _state.value = SearchState.Loading

            performSearch(page = 0)
        }
    }

    /**
     * Загрузить следующую страницу результатов
     */
    fun loadNextPage() {
        val currentState = _state.value

        if (currentState !is SearchState.Success) {
            return
        }

        if (!currentState.hasMorePages) {
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
     * Повторить последний поиск
     */
    fun retry() {
        if (currentSearchQuery.isNotEmpty()) {
            _state.value = SearchState.Loading
            viewModelScope.launch {
                // Перезагружаем фильтры перед retry
                loadFilters()
                performSearch(page = 0)
            }
        }
    }

    /**
     * Отменить ошибку пагинации
     */
    fun dismissPaginationError() {
        val currentState = _state.value
        if (currentState is SearchState.PaginationError) {
            _state.value = SearchState.Success(
                vacancies = currentState.currentVacancies,
                found = currentState.found,
                currentPage = currentState.currentPage,
                totalPages = currentState.totalPages,
                hasMorePages = currentState.currentPage < currentState.totalPages - 1
            )
        }
    }

    /**
     * Выполнить поиск вакансий с применением фильтров
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

        val hasMorePages = page < totalPages - 1

        lastSuccessFound = found
        lastSuccessTotalPages = totalPages

        _state.value = SearchState.Success(
            vacancies = allVacancies,
            found = found,
            currentPage = page,
            totalPages = totalPages,
            hasMorePages = hasMorePages
        )
    }

    private fun handleSearchFailure(exception: Throwable) {
        val message = exception.message ?: "Неизвестная ошибка"
        val currentState = _state.value

        if (currentState is SearchState.LoadingNextPage) {
            val errorType = if (isNoConnectionError(message)) {
                SearchState.PaginationError.ErrorType.NO_CONNECTION
            } else {
                SearchState.PaginationError.ErrorType.SERVER_ERROR
            }

            _state.value = SearchState.PaginationError(
                currentVacancies = currentState.currentVacancies,
                currentPage = currentState.currentPage,
                found = lastSuccessFound,
                totalPages = lastSuccessTotalPages,
                errorType = errorType
            )
        } else {
            _state.value = when {
                isNoConnectionError(message) -> SearchState.NoConnection
                else -> SearchState.Error(message)
            }
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
        _searchQuery.value = ""
        _state.value = SearchState.Initial
    }

    companion object {
        private const val DEBOUNCE_DELAY_MS = 2000L
    }
}
