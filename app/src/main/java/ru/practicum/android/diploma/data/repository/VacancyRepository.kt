package ru.practicum.android.diploma.data.repository

import android.util.Log
import ru.practicum.android.diploma.data.mappers.VacancyDtoMapper
import ru.practicum.android.diploma.data.network.NetworkResult
import ru.practicum.android.diploma.data.network.VacancyNetworkDataSource
import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * Репозиторий для работы с вакансиями
 * Отвечает за получение данных о вакансиях из сети
 */
class VacancyRepository(
    private val networkDataSource: VacancyNetworkDataSource
) {

    /**
     * Получить детальную информацию о вакансии по ID
     * @param vacancyId - идентификатор вакансии
     * @return Result с вакансией или ошибкой
     */
    suspend fun getVacancyById(vacancyId: String): Result<Vacancy> {
        return when (val result = networkDataSource.getVacancyDetails(vacancyId)) {
            is NetworkResult.Success -> {
                try {
                    val vacancy = VacancyDtoMapper.mapDetailToDomain(result.data)
                    Result.success(vacancy)
                } catch (e: Exception) {
                    Log.e(TAG, "Error mapping vacancy details: $vacancyId", e)
                    Result.failure(e)
                }
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
