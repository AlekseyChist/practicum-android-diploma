package ru.practicum.android.diploma.presentation.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.GetIndustriesUseCase
import ru.practicum.android.diploma.domain.models.Industry

/**
 * ViewModel для экрана выбора отрасли
 * Загружает список отраслей с HH.ru API
 * Поддерживает локальный поиск и выбор отрасли
 */
class IndustryViewModel(
    private val getIndustries: GetIndustriesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<IndustryState>(IndustryState.Initial)
    val state: StateFlow<IndustryState> = _state.asStateFlow()

    // сохраняем полный список для поиска
    private var allIndustries: List<Industry> = emptyList()
    private var selectedIndustry: Industry? = null
    private var searchQuery: String = ""

    /**
     * Загрузить список отраслей при открытии экрана
     * Вызывается из UI (Fragment/Composable)
     */
    fun loadIndustries(preselectedIndustry: Industry? = null) {
        // если уже загружено - не грузим повторно
        if (allIndustries.isNotEmpty()) {
            selectedIndustry = preselectedIndustry
            updateState()
            return
        }

        _state.value = IndustryState.Loading

        viewModelScope.launch {
            getIndustries.execute()
                .onSuccess { industries ->
                    allIndustries = industries
                    selectedIndustry = preselectedIndustry
                    updateState()
                }
                .onFailure { exception ->
                    handleError(exception)
                }
        }
    }

    /**
     * Обработать ошибку загрузки
     */
    private fun handleError(exception: Throwable) {
        val message = exception.message ?: "Неизвестная ошибка"

        _state.value = if (isNoConnectionError(message)) {
            IndustryState.NoConnection
        } else {
            IndustryState.Error(message)
        }
    }

    /**
     * Проверить, является ли ошибка ошибкой подключения
     */
    private fun isNoConnectionError(message: String): Boolean {
        return message.contains("интернет", ignoreCase = true) ||
            message.contains("connection", ignoreCase = true) ||
            message.contains("подключения", ignoreCase = true)
    }

    /**
     * Изменить поисковый запрос
     * Фильтрует список локально (без обращения к серверу)
     */
    fun onSearchQueryChanged(query: String) {
        searchQuery = query
        updateState()
    }

    /**
     * Выбрать отрасль
     * При выборе другой отрасли - снимаем выбор с предыдущей
     */
    fun selectIndustry(industry: Industry) {
        selectedIndustry = if (selectedIndustry?.id == industry.id) {
            // если кликнули на уже выбранную - снимаем выбор
            null
        } else {
            // выбираем новую отрасль
            industry
        }
        updateState()
    }

    /**
     * Получить выбранную отрасль
     * Используется при нажатии кнопки "Выбрать"
     */
    fun getSelectedIndustry(): Industry? {
        return selectedIndustry
    }

    /**
     * Повторить загрузку при ошибке
     */
    fun retry() {
        // сбрасываем кеш и грузим заново
        allIndustries = emptyList()
        loadIndustries(selectedIndustry)
    }

    /**
     * Обновить состояние экрана
     * Фильтрует список по поисковому запросу
     */
    private fun updateState() {
        val filteredList = filterIndustries()

        _state.value = IndustryState.Content(
            industries = allIndustries,
            filteredIndustries = filteredList,
            selectedIndustry = selectedIndustry,
            searchQuery = searchQuery
        )
    }

    /**
     * Отфильтровать список отраслей по поисковому запросу
     * Поиск по вхождению подстроки (игнорируя регистр)
     */
    private fun filterIndustries(): List<Industry> {
        if (searchQuery.isEmpty()) {
            return allIndustries
        }

        return allIndustries.filter { industry ->
            industry.name.contains(searchQuery, ignoreCase = true)
        }
    }
}
