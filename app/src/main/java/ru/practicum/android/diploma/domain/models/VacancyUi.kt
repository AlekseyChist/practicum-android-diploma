package ru.practicum.android.diploma.domain.models

data class VacancyUi(
    val id: String,
    val title: String,
    val city: String,
    val salary: String?,
    val company: String?,
    val logoUrl: String?
)
