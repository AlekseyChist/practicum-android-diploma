package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.data.repository.FavoritesRepository
import ru.practicum.android.diploma.domain.api.CheckIfVacancyFavoriteUseCase

/**
 * Реализация UseCase для проверки статуса избранного
 */
class CheckIfVacancyFavoriteUseCaseImpl(
    private val favoritesRepository: FavoritesRepository
) : CheckIfVacancyFavoriteUseCase {

    override suspend fun execute(vacancyId: String): Boolean {
        return favoritesRepository.isVacancyFavorite(vacancyId)
    }
}
