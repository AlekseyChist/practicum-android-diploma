package ru.practicum.android.diploma.data.dto.responses

import com.google.gson.annotations.SerializedName

data class VacancyDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("employer")
    val employer: EmployerDto?,

    @SerializedName("salary")
    val salary: SalaryDto?,

    @SerializedName("area")
    val area: AreaDto?,

    @SerializedName("experience")
    val experience: ExperienceDto?,

    @SerializedName("employment")
    val employment: EmploymentDto?,

    @SerializedName("schedule")
    val schedule: ScheduleDto?
)
