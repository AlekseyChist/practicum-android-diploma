package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.FiltersRepository
import ru.practicum.android.diploma.domain.api.GetIndustryByIdUseCase
import ru.practicum.android.diploma.domain.models.Industry

class GetIndustryByIdUseCaseImpl(
    private val filtersRepository: FiltersRepository
) : GetIndustryByIdUseCase {
    override suspend fun execute(industryId: Int): Result<Industry> {
        return filtersRepository.getIndustryById(industryId)
    }
}
