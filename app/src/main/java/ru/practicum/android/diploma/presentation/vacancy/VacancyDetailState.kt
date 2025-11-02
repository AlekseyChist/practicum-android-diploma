package ru.practicum.android.diploma.presentation.vacancy

import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * Состояния экрана деталей вакансии
 */
sealed class VacancyDetailState {

    /**
     * Начальное состояние
     */
    object Initial : VacancyDetailState()

    /**
     * Загрузка данных о вакансии
     */
    object Loading : VacancyDetailState()

    /**
     * Данные успешно загружены
     */
    data class Success(
        val vacancy: Vacancy,
        val isFavorite: Boolean
    ) : VacancyDetailState()

    /**
     * Вакансия не найдена или удалена (404)
     */
    object NotFound : VacancyDetailState()

    /**
     * Ошибка сервера (5xx, 403 и другие)
     */
    data class ServerError(val message: String) : VacancyDetailState()

    /**
     * Нет подключения к интернету
     */
    object NoConnection : VacancyDetailState()
}
