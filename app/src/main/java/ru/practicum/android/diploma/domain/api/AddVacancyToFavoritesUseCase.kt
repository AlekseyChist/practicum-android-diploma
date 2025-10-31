package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * UseCase для добавления вакансии в избранное
 */
interface AddVacancyToFavoritesUseCase {

    /**
     * Добавить вакансию в избранное
     * @param vacancy - вакансия для добавления
     * @return Result с успехом или ошибкой
     */
    suspend fun execute(vacancy: Vacancy): Result<Unit>
}
