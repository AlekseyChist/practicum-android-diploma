package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Industry

/**
 * Репозиторий для работы с фильтрами поиска вакансий
 * Предоставляет доступ к спискам регионов и индустрий
 */
interface FiltersRepository {

    /**
     * Получить список стран с регионами
     * @return Result со списком стран или ошибка
     */
    suspend fun getAreas(): Result<List<Country>>

    /**
     * Получить список индустрий (отраслей)
     * @return Result со списком индустрий или ошибка
     */
    suspend fun getIndustries(): Result<List<Industry>>

    /**
     * Получить индустрию(отрасль)
     * @return Result со элементом индустрии или ошибка
     */
    suspend fun getIndustryById(industryId: Int): Result<Industry>
}
