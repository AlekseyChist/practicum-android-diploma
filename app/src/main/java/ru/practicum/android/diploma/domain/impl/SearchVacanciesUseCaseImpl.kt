package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.data.dto.requests.VacancySearchRequest
import ru.practicum.android.diploma.data.repository.VacancyRepository
import ru.practicum.android.diploma.domain.api.SearchVacanciesUseCase
import ru.practicum.android.diploma.domain.models.SearchResult

/**
 * Реализация UseCase для поиска вакансий
 */
class SearchVacanciesUseCaseImpl(
    private val vacancyRepository: VacancyRepository
) : SearchVacanciesUseCase {

    override suspend fun execute(searchRequest: VacancySearchRequest): Result<SearchResult> {
        return vacancyRepository.searchVacancies(searchRequest)
    }
}
