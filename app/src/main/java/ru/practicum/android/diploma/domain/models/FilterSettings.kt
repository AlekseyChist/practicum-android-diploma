package ru.practicum.android.diploma.domain.models

/**
 * Модель настроек фильтров для поиска вакансий
 * Все поля nullable, так как фильтры могут быть не заданы
 */
data class FilterSettings(
    val industry: Int? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false
) {
    /**
     * Проверка на наличие активных фильтров
     * Используется для подсветки кнопки фильтра на экране поиска
     */
    fun hasActiveFilters(): Boolean {
        return industry != null || salary != null || onlyWithSalary
    }

    /**
     * Проверка на пустоту настроек
     */
    fun isEmpty(): Boolean {
        return !hasActiveFilters()
    }
}
