package ru.practicum.android.diploma.presentation.search

import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * Состояния экрана поиска вакансий
 */
sealed class SearchState {

    /**
     * Начальное состояние - пустой экран
     */
    object Initial : SearchState()

    /**
     * Загрузка первой страницы результатов
     */
    object Loading : SearchState()

    /**
     * Загрузка следующей страницы (пагинация)
     * @param currentVacancies - уже загруженные вакансии
     * @param currentPage - текущая страница
     */
    data class LoadingNextPage(
        val currentVacancies: List<Vacancy>,
        val currentPage: Int
    ) : SearchState()

    /**
     * Успешная загрузка результатов
     * @param vacancies - список найденных вакансий
     * @param found - общее количество найденных вакансий
     * @param currentPage - текущая страница
     * @param totalPages - всего страниц
     * @param hasMorePages - есть ли еще страницы для загрузки
     */
    data class Success(
        val vacancies: List<Vacancy>,
        val found: Int,
        val currentPage: Int,
        val totalPages: Int,
        val hasMorePages: Boolean
    ) : SearchState()

    /**
     * Пустой результат поиска
     * @param query - поисковый запрос, по которому ничего не найдено
     */
    data class EmptyResult(val query: String) : SearchState()

    /**
     * Ошибка при загрузке
     * @param message - сообщение об ошибке
     */
    data class Error(val message: String) : SearchState()

    /**
     * Нет подключения к интернету
     */
    object NoConnection : SearchState()
}
