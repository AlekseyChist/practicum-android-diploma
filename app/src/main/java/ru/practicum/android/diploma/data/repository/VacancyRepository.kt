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
     */
    suspend fun searchVacancies(
        searchRequest: VacancySearchRequest
    ): Result<SearchResult> {
        Log.d(TAG, "searchVacancies: начинаем с request=$searchRequest")

        return when (val result = networkDataSource.searchVacancies(
            text = searchRequest.text,
            area = searchRequest.area,
            industry = searchRequest.industry,
            salary = searchRequest.salary,
            onlyWithSalary = searchRequest.onlyWithSalary,
            page = searchRequest.page
        )) {
            is NetworkResult.Success -> {
                Log.d(TAG, "searchVacancies: SUCCESS от networkDataSource")
                Log.d(TAG, "searchVacancies: result.data = ${result.data}")
                Log.d(TAG, "searchVacancies: result.data.found = ${result.data.found}")
                Log.d(TAG, "searchVacancies: result.data.page = ${result.data.page}")
                Log.d(TAG, "searchVacancies: result.data.pages = ${result.data.pages}")
                Log.d(TAG, "searchVacancies: result.data.vacancies = ${result.data.vacancies}")
                Log.d(TAG, "searchVacancies: result.data.vacancies?.size = ${result.data.vacancies?.size}")

                val vacanciesList = result.data.vacancies ?: emptyList()
                Log.d(TAG, "searchVacancies: vacanciesList.size = ${vacanciesList.size}")

                if (vacanciesList.isEmpty()) {
                    Log.w(TAG, "searchVacancies: ВНИМАНИЕ! vacancies список ПУСТОЙ, хотя found=${result.data.found}")
                    Log.w(TAG, "searchVacancies: Это означает что либо API не вернул массив vacancies, либо он пришел пустым")
                }

                val vacancies = vacanciesList.map { dto ->
                    Log.d(TAG, "searchVacancies: маппинг вакансии: id=${dto.id}, name=${dto.name}")
                    VacancyDtoMapper.mapShortToDomain(dto)
                }

                Log.d(TAG, "searchVacancies: маппинг завершен, итого вакансий=${vacancies.size}")

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
                Log.e(TAG, "searchVacancies: ERROR от networkDataSource: code=${result.code}, message=${result.message}")
                Result.failure(Exception("Ошибка сервера: ${result.code}"))
            }
            is NetworkResult.NoConnection -> {
                Log.w(TAG, "searchVacancies: NoConnection от networkDataSource")
                Result.failure(Exception(NO_CONNECTION_MESSAGE))
            }
        }
    }

    /**
     * Получить детальную информацию о вакансии по ID
     */
    suspend fun getVacancyById(vacancyId: String): Result<Vacancy> {
        Log.d(TAG, "getVacancyById: начинаем для vacancyId=$vacancyId")

        return when (val result = networkDataSource.getVacancyDetails(vacancyId)) {
            is NetworkResult.Success -> {
                Log.d(TAG, "getVacancyById: SUCCESS от networkDataSource")
                val vacancy = VacancyDtoMapper.mapDetailToDomain(result.data)
                Log.d(TAG, "getVacancyById: маппинг завершен, vacancy.id=${vacancy.id}")
                Result.success(vacancy)
            }

            is NetworkResult.Error -> {
                Log.e(TAG, "getVacancyById: ERROR: vacancyId=$vacancyId, code=${result.code}")

                val errorMessage = when (result.code) {
                    HTTP_NOT_FOUND -> NOT_FOUND_MESSAGE
                    HTTP_FORBIDDEN -> AUTHORIZATION_ERROR_MESSAGE
                    in HTTP_SERVER_ERROR_START..HTTP_SERVER_ERROR_END -> "SERVER_ERROR:${result.code}"
                    else -> "SERVER_ERROR:${result.code}"
                }

                Result.failure(Exception(errorMessage))
            }

            is NetworkResult.NoConnection -> {
                Log.w(TAG, "getVacancyById: NoConnection для vacancyId=$vacancyId")
                Result.failure(Exception(NO_CONNECTION_MESSAGE))
            }
        }
    }

    companion object {
        private const val TAG = "VacancyRepository"
        private const val HTTP_NOT_FOUND = 404
        private const val HTTP_FORBIDDEN = 403
        private const val HTTP_SERVER_ERROR_START = 500
        private const val HTTP_SERVER_ERROR_END = 599
        private const val NOT_FOUND_MESSAGE = "VACANCY_NOT_FOUND"
        private const val AUTHORIZATION_ERROR_MESSAGE = "AUTHORIZATION_ERROR"
        private const val NO_CONNECTION_MESSAGE = "Нет подключения к интернету"
    }
}
