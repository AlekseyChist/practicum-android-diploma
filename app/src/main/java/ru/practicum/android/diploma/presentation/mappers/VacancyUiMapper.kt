package ru.practicum.android.diploma.presentation.mappers

import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.formatForDisplay
import ru.practicum.android.diploma.ui.search.VacancyUi

/**
 * Маппер для преобразования Domain модели в UI модель
 * Используется в ViewModels для подготовки данных к отображению
 */
object VacancyUiMapper {

    /**
     * Преобразует Domain вакансию в UI модель для списка
     */
    fun mapToUi(vacancy: Vacancy): VacancyUi {
        return VacancyUi(
            id = vacancy.id,
            title = vacancy.name,
            city = vacancy.area.name,
            salary = vacancy.salary?.formatForDisplay(),
            company = vacancy.employer.name
        )
    }

    /**
     * Преобразует список вакансий
     */
    fun mapToUi(vacancies: List<Vacancy>): List<VacancyUi> {
        return vacancies.map { mapToUi(it) }
    }
}
