package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * UseCase для получения избранной вакансии по ID
 */
interface GetFavoriteVacancyByIdUseCase {

    /**
     * Получить избранную вакансию по идентификатору
     * @param vacancyId - идентификатор вакансии
     * @return Вакансия или null если не найдена
     */
    suspend fun execute(vacancyId: String): Vacancy?
}
