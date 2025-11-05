package ru.practicum.android.diploma.domain.api

/**
 * UseCase для удаления вакансии из избранного
 */
interface RemoveVacancyFromFavoritesUseCase {

    /**
     * Удалить вакансию из избранного
     * @param vacancyId - идентификатор вакансии
     * @return Result с успехом или ошибкой
     */
    suspend fun execute(vacancyId: String): Result<Unit>
}
