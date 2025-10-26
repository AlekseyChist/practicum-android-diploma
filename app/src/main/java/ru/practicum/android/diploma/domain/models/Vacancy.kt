package ru.practicum.android.diploma.domain.models

/**
 * Основная модель вакансии для всего приложения
 * Эта модель используется везде: в UI, ViewModel, Repository
 * Не зависит от API или БД - это НАША модель
 */
data class Vacancy(
    val id: String,
    val name: String,
    val employer: Employer,
    val area: Area,
    val salary: Salary?,
    val experience: Experience?,
    val employment: Employment?,
    val schedule: Schedule?,
    val description: String,
    val keySkills: List<String>,
    val contacts: Contacts?,
    val address: String?,
    val url: String,
    val isFavorite: Boolean = false
)
