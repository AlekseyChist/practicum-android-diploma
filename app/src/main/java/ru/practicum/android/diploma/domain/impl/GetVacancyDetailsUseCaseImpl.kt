package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.data.repository.VacancyRepository
import ru.practicum.android.diploma.domain.api.GetVacancyDetailsUseCase
import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * Реализация Use Case для получения детальной информации о вакансии
 */
class GetVacancyDetailsUseCaseImpl(
    private val vacancyRepository: VacancyRepository
) : GetVacancyDetailsUseCase {

    override suspend fun execute(vacancyId: String): Result<Vacancy> {
        return vacancyRepository.getVacancyById(vacancyId)
    }
}
