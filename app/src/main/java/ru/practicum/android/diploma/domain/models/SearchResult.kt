package ru.practicum.android.diploma.domain.models

/**
 * Результат поиска вакансий
 */
data class SearchResult(
    val vacancies: List<Vacancy>,
    val found: Int,
    val pages: Int,
    val page: Int
)
