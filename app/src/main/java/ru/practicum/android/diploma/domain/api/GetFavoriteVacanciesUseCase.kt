package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * UseCase для получения списка избранных вакансий
 */
interface GetFavoriteVacanciesUseCase {

    /**
     * Получить список всех избранных вакансий
     * @return Flow со списком вакансий (автоматически обновляется при изменениях)
     */
    fun execute(): Flow<List<Vacancy>>
}
