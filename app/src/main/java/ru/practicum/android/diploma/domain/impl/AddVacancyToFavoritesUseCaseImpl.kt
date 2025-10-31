package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.data.repository.FavoritesRepository
import ru.practicum.android.diploma.domain.api.AddVacancyToFavoritesUseCase
import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * Реализация UseCase для добавления вакансии в избранное
 */
class AddVacancyToFavoritesUseCaseImpl(
    private val favoritesRepository: FavoritesRepository
) : AddVacancyToFavoritesUseCase {

    override suspend fun execute(vacancy: Vacancy): Result<Unit> {
        return favoritesRepository.addToFavorites(vacancy)
    }
}
