package ru.practicum.android.diploma.data.dto.requests

/**
 * Параметры для поиска вакансий
 */
data class VacancySearchRequest(
    val text: String? = null,
    val area: Int? = null,
    val industry: Int? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean? = null,
    val page: Int = 0
)
