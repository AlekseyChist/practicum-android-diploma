package ru.practicum.android.diploma.presentation.favorites

import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * Состояния экрана списка избранных вакансий
 */
sealed class FavoritesState {

    /**
     * Начальное состояние - ничего не происходит
     */
    object Initial : FavoritesState()

    /**
     * Загрузка данных из базы
     */
    object Loading : FavoritesState()

    /**
     * Список избранного пуст
     */
    object Empty : FavoritesState()

    /**
     * Есть избранные вакансии
     * @param vacancies - список избранных вакансий
     */
    data class Content(val vacancies: List<Vacancy>) : FavoritesState()

    /**
     * Ошибка при работе с базой данных
     * @param message - сообщение об ошибке
     */
    data class Error(val message: String) : FavoritesState()
}
