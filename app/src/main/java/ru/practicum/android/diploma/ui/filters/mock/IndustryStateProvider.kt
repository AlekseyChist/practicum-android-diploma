package ru.practicum.android.diploma.ui.filters.mock

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.practicum.android.diploma.presentation.filters.IndustryState

class IndustryStateProvider : PreviewParameterProvider<IndustryState> {
    override val values = sequenceOf(
        IndustryState.Initial,
        IndustryState.Loading,
        IndustryState.Content(
            industries = mockIndustries,
            filteredIndustries = mockIndustries,
            selectedIndustry = null,
            searchQuery = ""
        ),
        IndustryState.Content(
            industries = mockIndustries,
            filteredIndustries = mockIndustries,
            selectedIndustry = mockIndustries[1],
            searchQuery = "стр"
        ),
        IndustryState.Error("Ошибка загрузки данных"),
        IndustryState.NoConnection
    )
}
