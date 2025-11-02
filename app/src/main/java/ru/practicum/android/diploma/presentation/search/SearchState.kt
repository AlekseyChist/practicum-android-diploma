package ru.practicum.android.diploma.presentation.search

import ru.practicum.android.diploma.ui.search.VacancyUi

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
        val vacancies: List<VacancyUi>,  // UI модели для отображения
        val found: Int,                   // Общее количество найденных вакансий
        val currentPage: Int,             // Текущая страница
        val totalPages: Int,              // Общее количество страниц
        val hasMorePages: Boolean         // Есть ли еще страницы для загрузки
    ) : SearchState

    /**
     * Пустой результат (ничего не найдено)
     */
    data class EmptyResult(
        val query: String  // Поисковый запрос для отображения
    ) : SearchState

    /**
     * Нет подключения к интернету
     */
    data object NoConnection : SearchState

    /**
     * Ошибка при поиске
     */
    data class Error(
        val message: String  // Сообщение об ошибке
    ) : SearchState
}
