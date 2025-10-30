package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * Use Case для получения детальной информации о вакансии
 */
interface GetVacancyDetailsUseCase {

    suspend fun execute(vacancyId: String): Result<Vacancy>
}
