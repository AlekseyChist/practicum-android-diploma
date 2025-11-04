package ru.practicum.android.diploma.data.dto.responses

import com.google.gson.annotations.SerializedName

data class VacancyResponse(
    @SerializedName("found")
    val found: Int,

    @SerializedName("pages")
    val pages: Int,

    @SerializedName("page")
    val page: Int,

    @SerializedName("items")
    val vacancies: List<VacancyDto>
)
