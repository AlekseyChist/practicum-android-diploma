package ru.practicum.android.diploma.presentation.filters

import ru.practicum.android.diploma.domain.models.Industry

/**
 * Состояния экрана настроек фильтров
 * Управляет отображением полей фильтрации
 */
sealed interface FiltersSettingsState {

    /**
     * Начальное состояние (загрузка настроек)
     */
    data object Initial : FiltersSettingsState

    /**
     * Состояние с данными фильтров
     * @param expectedSalary - ожидаемая зарплата (строка для удобства ввода)
     * @param onlyWithSalary - флаг "только с зарплатой"
     * @param selectedIndustry - выбранная отрасль (или null)
     * @param hasActiveFilters - есть ли хотя бы один активный фильтр
     *        (для видимости кнопок "Применить" и "Сбросить")
     */
    data class Content(
        val expectedSalary: String = "",
        val onlyWithSalary: Boolean = false,
        val selectedIndustry: Industry? = null,
        val hasActiveFilters: Boolean = false
    ) : FiltersSettingsState
}
