package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Industry

/**
 * UseCase для получения списка индустрий
 */
interface GetIndustriesUseCase {

    /**
     * Получить список всех индустрий
     * @return Result со списком индустрий или ошибкой
     */
    suspend fun execute(): Result<List<Industry>>
}
