package ru.practicum.android.diploma.presentation.search

/**
 * Состояния экрана поиска вакансий
 */
sealed class SearchState{
    /**
     * Начальное состояние - пустой экран
     */
    object Initial : SearchState()
}
