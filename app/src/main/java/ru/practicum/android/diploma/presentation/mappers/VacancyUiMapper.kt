package ru.practicum.android.diploma.presentation.mappers

import android.util.Log
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyUi
import ru.practicum.android.diploma.domain.models.formatForDisplay

/**
 * Маппер для преобразования Domain модели в UI модель
 * Используется в ViewModels для подготовки данных к отображению
 */
object VacancyUiMapper {

    private const val TAG = "VacancyUiMapper"

    /**
     * Преобразует Domain вакансию в UI модель для списка
     */
    fun mapToUi(vacancy: Vacancy): VacancyUi {
        Log.d(TAG, "mapToUi: id=${vacancy.id}, name=${vacancy.name}")
        Log.d(TAG, "mapToUi: employer.name=${vacancy.employer.name}")
        Log.d(TAG, "mapToUi: employer.logoUrl=${vacancy.employer.logoUrl}")

        val result = VacancyUi(
            id = vacancy.id,
            title = vacancy.name,
            city = vacancy.area.name,
            salary = vacancy.salary?.formatForDisplay(),
            company = vacancy.employer.name,
            logoUrl = vacancy.employer.logoUrl
        )

        Log.d(TAG, "mapToUi: result.logoUrl=${result.logoUrl}")
        return result
    }

    /**
     * Преобразует список вакансий
     */
    fun mapToUi(vacancies: List<Vacancy>): List<VacancyUi> {
        Log.d(TAG, "mapToUi: маппинг ${vacancies.size} вакансий")
        return vacancies.map { mapToUi(it) }
    }
}
