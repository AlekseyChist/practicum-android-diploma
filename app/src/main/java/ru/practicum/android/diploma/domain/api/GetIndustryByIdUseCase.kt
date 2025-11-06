package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Industry

/**
 * UseCase для получения индустрии
 */
interface GetIndustryByIdUseCase {

    /**
     * Получить индустрию по идентификатору
     * @param industryId - идентификатор индустрии
     * @return Индустрия или null если не найдена
     */
    suspend fun execute(industryId: Int): Result<Industry>
}
