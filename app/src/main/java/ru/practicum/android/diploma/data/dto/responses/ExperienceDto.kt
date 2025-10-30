package ru.practicum.android.diploma.data.dto.responses

import com.google.gson.annotations.SerializedName

data class ExperienceDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String
)
