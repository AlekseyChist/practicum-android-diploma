package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.data.dto.requests.VacancySearchRequest
import ru.practicum.android.diploma.domain.models.SearchResult

/**
 * UseCase для поиска вакансий
 */
interface SearchVacanciesUseCase {

    /**
     * Поиск вакансий с параметрами
     * @param searchRequest параметры поиска (текст, фильтры, страница)
     * @return Result с результатами поиска или ошибкой
     */
    suspend fun execute(searchRequest: VacancySearchRequest): Result<SearchResult>
}
