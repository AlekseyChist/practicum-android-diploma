package ru.practicum.android.diploma.presentation.search

import android.util.Log
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
     * Поиск вакансий с debounce
     * @param query - поисковый запрос
     * @param filters - фильтры поиска (регион, отрасль, зарплата)
     */
    fun searchVacancies(
        query: String,
        filters: VacancySearchRequest? = null
    ) {
        Log.d(TAG, "searchVacancies: вызван с query='$query'")
        currentSearchQuery = query.trim()
        currentFilters = filters

        if (currentSearchQuery.isEmpty()) {
            Log.d(TAG, "searchVacancies: запрос пустой, переход в Initial state")
            _state.value = SearchState.Initial
            return
        }

        Log.d(TAG, "searchVacancies: отменяем предыдущий searchJob")
        searchJob?.cancel()

        Log.d(TAG, "searchVacancies: запускаем debounce на $DEBOUNCE_DELAY_MS мс")
        searchJob = viewModelScope.launch {
            delay(DEBOUNCE_DELAY_MS)
            Log.d(TAG, "searchVacancies: debounce завершен")

            // Устанавливаем Loading ПЕРЕД запросом!
            _state.value = SearchState.Loading
            Log.d(TAG, "searchVacancies: установлено состояние Loading")

            performSearch(page = 0)
        }
    }

    /**
     * Загрузить следующую страницу результатов
     */
    fun loadNextPage() {
        Log.d(TAG, "loadNextPage: вызван")
        val currentState = _state.value

        if (currentState !is SearchState.Success) {
            Log.d(TAG, "loadNextPage: текущее состояние не Success, выходим")
            return
        }

        if (!currentState.hasMorePages) {
            Log.d(TAG, "loadNextPage: больше нет страниц, выходим")
            return
        }

        Log.d(TAG, "loadNextPage: загружаем страницу ${currentState.currentPage + 1}")
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
        Log.d(TAG, "retry: вызван")
        if (currentSearchQuery.isNotEmpty()) {
            Log.d(TAG, "retry: повторяем поиск для '$currentSearchQuery'")
            _state.value = SearchState.Loading
            viewModelScope.launch {
                performSearch(page = 0)
            }
        } else {
            Log.d(TAG, "retry: запрос пустой, ничего не делаем")
        }
    }

    /**
     * Выполнить поиск вакансий
     */
    private suspend fun performSearch(page: Int) {
        Log.d(TAG, "performSearch: начинаем поиск, page=$page")
        val request = createSearchRequest(page)
        Log.d(TAG, "performSearch: создан request=$request")

        searchVacanciesUseCase.execute(request)
            .onSuccess { result ->
                Log.d(TAG, "performSearch: SUCCESS! Получено ${result.vacancies.size} вакансий")
                Log.d(TAG, "performSearch: found=${result.found}, page=${result.page}, totalPages=${result.pages}")

                val vacanciesUi = VacancyUiMapper.mapToUi(result.vacancies)
                Log.d(TAG, "performSearch: маппинг завершен, vacanciesUi.size=${vacanciesUi.size}")

                handleSearchSuccess(vacanciesUi, result.found, result.page, result.pages)
            }
            .onFailure { exception ->
                Log.e(TAG, "performSearch: FAILURE! Ошибка: ${exception.message}", exception)
                handleSearchFailure(exception)
            }
    }

    private fun createSearchRequest(page: Int): VacancySearchRequest {
        val request = VacancySearchRequest(
            text = currentSearchQuery,
            area = currentFilters?.area,
            industry = currentFilters?.industry,
            salary = currentFilters?.salary,
            onlyWithSalary = currentFilters?.onlyWithSalary,
            page = page
        )
        Log.d(TAG, "createSearchRequest: создан request для page=$page: $request")
        return request
    }

    private fun handleSearchSuccess(
        vacancies: List<VacancyUi>,
        found: Int,
        page: Int,
        totalPages: Int
    ) {
        Log.d(
            TAG,
            "handleSearchSuccess: vacancies.size=${vacancies.size}, found=$found, page=$page, totalPages=$totalPages"
        )

        val currentState = _state.value

        if (vacancies.isEmpty() && page == 0) {
            Log.d(TAG, "handleSearchSuccess: результаты пустые на первой странице, EmptyResult")
            _state.value = SearchState.EmptyResult(currentSearchQuery)
            return
        }

        val allVacancies = if (currentState is SearchState.LoadingNextPage) {
            Log.d(TAG, "handleSearchSuccess: добавляем к существующим ${currentState.currentVacancies.size} вакансиям")
            currentState.currentVacancies + vacancies
        } else {
            Log.d(TAG, "handleSearchSuccess: первая страница, используем только новые вакансии")
            vacancies
        }

        val hasMorePages = page < totalPages - 1
        Log.d(TAG, "handleSearchSuccess: итого вакансий=${allVacancies.size}, hasMorePages=$hasMorePages")

        _state.value = SearchState.Success(
            vacancies = allVacancies,
            found = found,
            currentPage = page,
            totalPages = totalPages,
            hasMorePages = hasMorePages
        )
        Log.d(TAG, "handleSearchSuccess: state обновлен в Success")
    }

    private fun handleSearchFailure(exception: Throwable) {
        Log.e(TAG, "handleSearchFailure: обрабатываем ошибку")
        val message = exception.message ?: "Неизвестная ошибка"
        Log.e(TAG, "handleSearchFailure: сообщение ошибки='$message'")

        _state.value = when {
            isNoConnectionError(message) -> {
                Log.d(TAG, "handleSearchFailure: определена как NoConnection")
                SearchState.NoConnection
            }
            else -> {
                Log.d(TAG, "handleSearchFailure: определена как Error")
                SearchState.Error(message)
            }
        }
        Log.d(TAG, "handleSearchFailure: state обновлен")
    }

    private fun isNoConnectionError(message: String): Boolean {
        val isNoConnection = message.contains("интернет", ignoreCase = true) ||
            message.contains("connection", ignoreCase = true) ||
            message.contains("подключения", ignoreCase = true)
        Log.d(TAG, "isNoConnectionError: message='$message', result=$isNoConnection")
        return isNoConnection
    }

    /**
     * Очистить поиск и вернуться к начальному состоянию
     */
    fun clearSearch() {
        Log.d(TAG, "clearSearch: очищаем поиск")
        searchJob?.cancel()
        currentSearchQuery = ""
        currentFilters = null
        _state.value = SearchState.Initial
        Log.d(TAG, "clearSearch: state сброшен в Initial")
    }

    companion object {
        private const val TAG = "SearchViewModel"
        private const val DEBOUNCE_DELAY_MS = 2000L // 2 секунды согласно ТЗ
    }
}
