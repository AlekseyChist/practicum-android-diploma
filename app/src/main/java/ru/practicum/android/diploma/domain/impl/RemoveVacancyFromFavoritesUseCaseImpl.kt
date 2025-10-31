package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.data.repository.FavoritesRepository
import ru.practicum.android.diploma.domain.api.RemoveVacancyFromFavoritesUseCase

/**
 * Реализация UseCase для удаления вакансии из избранного
 */
class RemoveVacancyFromFavoritesUseCaseImpl(
    private val favoritesRepository: FavoritesRepository
) : RemoveVacancyFromFavoritesUseCase {

    override suspend fun execute(vacancyId: String): Result<Unit> {
        return favoritesRepository.removeFromFavorites(vacancyId)
    }
}
