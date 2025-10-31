package ru.practicum.android.diploma.presentation.vacancy

import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * Состояния экрана деталей вакансии
 * Обновлено для поддержки функционала избранного
 */
sealed class VacancyDetailState {

    /**
     * Начальное состояние - ничего не происходит
     */
    object Initial : VacancyDetailState()

    /**
     * Загрузка данных о вакансии
     */
    object Loading : VacancyDetailState()

    /**
     * Данные успешно загружены
     * @param vacancy - загруженная вакансия
     * @param isFavorite - находится ли вакансия в избранном
     */
    data class Success(
        val vacancy: Vacancy,
        val isFavorite: Boolean
    ) : VacancyDetailState()

    /**
     * Ошибка при загрузке (проблемы с сервером, вакансия не найдена и т.д.)
     * @param message - сообщение об ошибке
     */
    data class Error(val message: String) : VacancyDetailState()

    /**
     * Нет подключения к интернету
     */
    object NoConnection : VacancyDetailState()
}
