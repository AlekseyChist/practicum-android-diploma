package ru.practicum.android.diploma.presentation.search

import ru.practicum.android.diploma.domain.models.VacancyUi

/**
 * Состояния экрана поиска вакансий
 * Работает с UI моделями (VacancyUi) для отображения
 */
sealed interface SearchState {

    /**
     * Начальное состояние (пустой экран)
     */
    data object Initial : SearchState

    /**
     * Загрузка первой страницы результатов
     */
    data object Loading : SearchState

    data class Typing(val query: String) : SearchState

    /**
     * Загрузка следующей страницы (пагинация)
     * Показывает текущие результаты + индикатор загрузки внизу
     */
    data class LoadingNextPage(
        val currentVacancies: List<VacancyUi>,
        val currentPage: Int
    ) : SearchState

    /**
     * Успешная загрузка результатов
     */
    data class Success(
        val vacancies: List<VacancyUi>,
        val found: Int,
        val currentPage: Int,
        val totalPages: Int,
        val hasMorePages: Boolean
    ) : SearchState

    /**
     * Пустой результат (ничего не найдено)
     */
    data class EmptyResult(
        val query: String
    ) : SearchState

    /**
     * Нет подключения к интернету
     */
    data object NoConnection : SearchState

    /**
     * Ошибка при поиске
     */
    data class Error(
        val message: String
    ) : SearchState
}
