package ru.practicum.android.diploma.ui.vacancy.mock

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.practicum.android.diploma.presentation.vacancy.VacancyDetailState

class VacancyStateProvider : PreviewParameterProvider<VacancyDetailState> {
    override val values: Sequence<VacancyDetailState>
        get() = sequenceOf(
            VacancyDetailState.Loading,
            VacancyDetailState.NoConnection,
            VacancyDetailState.Error("Произошла ошибка"),
            VacancyDetailState.Success(vacancy = mockVacancy, isFavorite = false)
        )
}
