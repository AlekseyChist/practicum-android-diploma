package ru.practicum.android.diploma.data.repository

import android.util.Log
import ru.practicum.android.diploma.data.dto.requests.VacancySearchRequest
import ru.practicum.android.diploma.data.mappers.VacancyDtoMapper
import ru.practicum.android.diploma.data.network.NetworkResult
import ru.practicum.android.diploma.data.network.VacancyNetworkDataSource
import ru.practicum.android.diploma.domain.models.SearchResult
import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * Репозиторий для работы с вакансиями
 * Отвечает за получение данных о вакансиях из сети
 */
class VacancyRepository(
    private val networkDataSource: VacancyNetworkDataSource
) {

    /**
     * Поиск вакансий с фильтрами и пагинацией
     * @param searchRequest параметры поиска
     * @return Result со списком вакансий, общим количеством и текущей страницей
     */
    suspend fun searchVacancies(
        searchRequest: VacancySearchRequest
    ): Result<SearchResult> {
        return when (val result = networkDataSource.searchVacancies(
            text = searchRequest.text,
            area = searchRequest.area,
            industry = searchRequest.industry,
            salary = searchRequest.salary,
            onlyWithSalary = searchRequest.onlyWithSalary,
            page = searchRequest.page
        )) {
            is NetworkResult.Success -> {
                val vacancies = result.data.vacancies.map { dto ->
                    VacancyDtoMapper.mapShortToDomain(dto)
                }
                Result.success(
                    SearchResult(
                        vacancies = vacancies,
                        found = result.data.found,
                        pages = result.data.pages,
                        page = result.data.page
                    )
                )
            }
            is NetworkResult.Error -> {
                Log.e(TAG, "Network error searching vacancies: code ${result.code}")
                Result.failure(Exception("Ошибка сервера: ${result.code}"))
            }
            is NetworkResult.NoConnection -> {
                Log.w(TAG, "No internet connection when searching vacancies")
                Result.failure(Exception("Нет подключения к интернету"))
            }
        }
    }

    /**
     * Получить детальную информацию о вакансии по ID
     * @param vacancyId - идентификатор вакансии
     * @return Result с вакансией или ошибкой
     */
    suspend fun getVacancyById(vacancyId: String): Result<Vacancy> {
        return when (val result = networkDataSource.getVacancyDetails(vacancyId)) {
            is NetworkResult.Success -> {
                val vacancy = VacancyDtoMapper.mapDetailToDomain(result.data)
                Result.success(vacancy)
            }
            is NetworkResult.Error -> {
                Log.e(TAG, "Network error getting vacancy: $vacancyId, code: ${result.code}")
                Result.failure(Exception("Ошибка сервера: ${result.code}"))
            }
            is NetworkResult.NoConnection -> {
                Log.w(TAG, "No internet connection when getting vacancy: $vacancyId")
                Result.failure(Exception("Нет подключения к интернету"))
            }
        }
    }

    companion object {
        private const val TAG = "VacancyRepository"
    }
}
