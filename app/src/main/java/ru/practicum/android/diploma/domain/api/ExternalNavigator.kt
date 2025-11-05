package ru.practicum.android.diploma.domain.api

interface ExternalNavigator {
    fun shareVacancy(url: String)
    fun sendEmail(email: String)
    fun dialPhone(phone: String)
}
