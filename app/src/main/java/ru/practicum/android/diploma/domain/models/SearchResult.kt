package ru.practicum.android.diploma.domain.models

/**
 * Результат поиска вакансий
 */
data class SearchResult(
    val vacancies: List<Vacancy>,  // Список найденных вакансий
    val found: Int,                // Всего найдено результатов
    val pages: Int,                // Всего страниц
    val page: Int                  // Текущая страница (начинается с 0)
)
