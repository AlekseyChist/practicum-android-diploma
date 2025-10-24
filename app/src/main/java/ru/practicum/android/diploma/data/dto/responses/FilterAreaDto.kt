package ru.practicum.android.diploma.data.dto.responses

import com.google.gson.annotations.SerializedName

data class FilterAreaDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("parentId")
    val parentId: String?,

    @SerializedName("areas")
    val areas: List<FilterAreaDto>?
)
