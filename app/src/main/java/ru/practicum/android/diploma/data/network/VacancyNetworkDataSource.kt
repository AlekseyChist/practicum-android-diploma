package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.responses.FilterAreaDto
import ru.practicum.android.diploma.data.dto.responses.FilterIndustryDto
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDto
import ru.practicum.android.diploma.data.dto.responses.VacancyResponse
import ru.practicum.android.diploma.data.network.api.VacancyApi

class VacancyNetworkDataSource(
    private val api: VacancyApi,
    private val networkClient: NetworkClient
) {

    suspend fun getAreas(): NetworkResult<List<FilterAreaDto>> {
        return networkClient.executeRequest {
            api.getAreas()
        }
    }

    suspend fun getIndustries(): NetworkResult<List<FilterIndustryDto>> {
        return networkClient.executeRequest {
            api.getIndustries()
        }
    }

    suspend fun searchVacancies(
        text: String? = null,
        area: Int? = null,
        industry: Int? = null,
        salary: Int? = null,
        onlyWithSalary: Boolean? = null,
        page: Int = 0
    ): NetworkResult<VacancyResponse> {
        return networkClient.executeRequest {
            api.searchVacancies(
                text = text,
                area = area,
                industry = industry,
                salary = salary,
                onlyWithSalary = onlyWithSalary,
                page = page
            )
        }
    }

    suspend fun getVacancyDetails(vacancyId: String): NetworkResult<VacancyDetailDto> {
        return networkClient.executeRequest {
            api.getVacancyDetails(vacancyId = vacancyId)
        }
    }
}
