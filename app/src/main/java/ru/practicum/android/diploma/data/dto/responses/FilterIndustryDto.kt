package ru.practicum.android.diploma.data.dto.responses

import com.google.gson.annotations.SerializedName

data class FilterIndustryDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String
)
