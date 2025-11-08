package ru.practicum.android.diploma.presentation.filters

import ru.practicum.android.diploma.domain.models.Industry

/**
 * Состояния экрана выбора отрасли
 * Управляет списком отраслей, поиском и выбором
 */
sealed interface IndustryState {

    /**
     * Начальное состояние
     */
    data object Initial : IndustryState

    /**
     * Загрузка списка отраслей с сервера
     */
    data object Loading : IndustryState

    /**
     * Успешная загрузка списка отраслей
     * @param industries - полный список отраслей с сервера
     * @param filteredIndustries - отфильтрованный список (по поисковому запросу)
     * @param selectedIndustry - выбранная отрасль (для отображения чекбокса)
     * @param searchQuery - текущий поисковый запрос (для фильтрации списка)
     */
    data class Content(
        val industries: List<Industry>,
        val filteredIndustries: List<Industry>,
        val selectedIndustry: Industry? = null,
        val searchQuery: String = ""
    ) : IndustryState

    /**
     * Ошибка при загрузке списка отраслей
     */
    data class Error(val message: String) : IndustryState

    /**
     * Нет подключения к интернету
     */
    data object NoConnection : IndustryState

    /**
     * Ничего не найдено по поисковому запросу
     * используется когда отрасли загружены но фильтр пуст
     */
    data object EmptySearch : IndustryState
}
