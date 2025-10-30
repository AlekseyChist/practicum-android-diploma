package ru.practicum.android.diploma.domain.api

/**
 * UseCase для проверки, находится ли вакансия в избранном
 */
interface CheckIfVacancyFavoriteUseCase {

    /**
     * Проверить, находится ли вакансия в избранном
     * @param vacancyId - идентификатор вакансии
     * @return true если вакансия в избранном, false если нет
     */
    suspend fun execute(vacancyId: String): Boolean
}
