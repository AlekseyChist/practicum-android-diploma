package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.FiltersRepository
import ru.practicum.android.diploma.domain.api.GetIndustriesUseCase
import ru.practicum.android.diploma.domain.models.Industry

/**
 * Реализация UseCase для получения списка индустрий
 */
class GetIndustriesUseCaseImpl(
    private val filtersRepository: FiltersRepository
) : GetIndustriesUseCase {

    override suspend fun execute(): Result<List<Industry>> {
        return filtersRepository.getIndustries()
    }
}
