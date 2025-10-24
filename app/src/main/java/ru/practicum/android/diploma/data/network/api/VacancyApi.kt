package ru.practicum.android.diploma.data.network.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.responses.FilterAreaDto
import ru.practicum.android.diploma.data.dto.responses.FilterIndustryDto
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDto
import ru.practicum.android.diploma.data.dto.responses.VacancyResponse

interface VacancyApi {

    @GET("/areas")
    suspend fun getAreas(
        @Header("Authorization") token: String
    ): Response<List<FilterAreaDto>>

    @GET("/industries")
    suspend fun getIndustries(
        @Header("Authorization") token: String
    ): Response<List<FilterIndustryDto>>

    @GET("/vacancies")
    suspend fun searchVacancies(
        @Header("Authorization") token: String,
        @Query("text") text: String? = null,
        @Query("area") area: Int? = null,
        @Query("industry") industry: Int? = null,
        @Query("salary") salary: Int? = null,
        @Query("only_with_salary") onlyWithSalary: Boolean? = null,
        @Query("page") page: Int = 0
    ): Response<VacancyResponse>

    @GET("/vacancies/{id}")
    suspend fun getVacancyDetails(
        @Header("Authorization") token: String,
        @Path("id") vacancyId: String
    ): Response<VacancyDetailDto>
}
