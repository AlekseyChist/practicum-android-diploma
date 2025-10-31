package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.data.repository.FavoritesRepository
import ru.practicum.android.diploma.domain.api.GetFavoriteVacancyByIdUseCase
import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * Реализация UseCase для получения избранной вакансии по ID
 */
class GetFavoriteVacancyByIdUseCaseImpl(
    private val favoritesRepository: FavoritesRepository
) : GetFavoriteVacancyByIdUseCase {

    override suspend fun execute(vacancyId: String): Vacancy? {
        return favoritesRepository.getFavoriteVacancyById(vacancyId)
    }
}
