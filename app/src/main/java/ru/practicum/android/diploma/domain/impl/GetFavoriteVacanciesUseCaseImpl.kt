package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.repository.FavoritesRepository
import ru.practicum.android.diploma.domain.api.GetFavoriteVacanciesUseCase
import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * Реализация UseCase для получения списка избранных вакансий
 */
class GetFavoriteVacanciesUseCaseImpl(
    private val favoritesRepository: FavoritesRepository
) : GetFavoriteVacanciesUseCase {

    override fun execute(): Flow<List<Vacancy>> {
        return favoritesRepository.getFavoriteVacancies()
    }
}
