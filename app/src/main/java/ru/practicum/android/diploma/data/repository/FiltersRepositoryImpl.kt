package ru.practicum.android.diploma.data.repository

import ru.practicum.android.diploma.data.mappers.FilterDtoMapper
import ru.practicum.android.diploma.data.network.NetworkResult
import ru.practicum.android.diploma.data.network.VacancyNetworkDataSource
import ru.practicum.android.diploma.domain.api.FiltersRepository
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Industry

/**
 * Реализация репозитория для работы с фильтрами
 * Получает данные из сети и преобразует их в domain-модели
 */
class FiltersRepositoryImpl(
    private val networkDataSource: VacancyNetworkDataSource
) : FiltersRepository {

    override suspend fun getAreas(): Result<List<Country>> {
        return when (val result = networkDataSource.getAreas()) {
            is NetworkResult.Success -> {
                val countries = FilterDtoMapper.mapAreasToCountries(result.data)
                Result.success(countries)
            }
            is NetworkResult.Error -> {
                Result.failure(Exception("Ошибка сервера: ${result.code}"))
            }
            is NetworkResult.NoConnection -> {
                Result.failure(Exception(NO_CONNECTION_MESSAGE))
            }
        }
    }

    override suspend fun getIndustries(): Result<List<Industry>> {
        return when (val result = networkDataSource.getIndustries()) {
            is NetworkResult.Success -> {
                val industries = FilterDtoMapper.mapIndustriesToDomain(result.data)
                Result.success(industries)
            }
            is NetworkResult.Error -> {
                Result.failure(Exception("Ошибка сервера: ${result.code}"))
            }
            is NetworkResult.NoConnection -> {
                Result.failure(Exception(NO_CONNECTION_MESSAGE))
            }
        }
    }

    override suspend fun getIndustryById(industryId: Int): Result<Industry> {
        return when (val result = networkDataSource.getIndustryById(industryId)) {
            is NetworkResult.Success -> {
                val industry = FilterDtoMapper.mapIndustryToDomain(result.data)
                Result.success(industry)
            }
            is NetworkResult.Error -> {
                Result.failure(Exception("Ошибка сервера: ${result.code}"))
            }
            is NetworkResult.NoConnection -> {
                Result.failure(Exception(NO_CONNECTION_MESSAGE))
            }
        }
    }

    companion object {
        private const val NO_CONNECTION_MESSAGE = "Нет подключения к интернету"
    }
}
