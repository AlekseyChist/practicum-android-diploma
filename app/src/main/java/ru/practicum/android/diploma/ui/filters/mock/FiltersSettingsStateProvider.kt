package ru.practicum.android.diploma.ui.filters.mock

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.practicum.android.diploma.presentation.filters.FiltersSettingsState

class FiltersSettingsStateProvider : PreviewParameterProvider<FiltersSettingsState> {
    override val values: Sequence<FiltersSettingsState>
        get() = sequenceOf(
            FiltersSettingsState.Initial,
            FiltersSettingsState.Content(
                expectedSalary = "",
                onlyWithSalary = false,
                selectedIndustry = null,
                hasActiveFilters = false
            ),
            FiltersSettingsState.Content(
                expectedSalary = "120000",
                onlyWithSalary = true,
                selectedIndustry = mockIndustries[0],
                hasActiveFilters = true
            ),
            FiltersSettingsState.Content(
                expectedSalary = "200000",
                onlyWithSalary = false,
                selectedIndustry = mockIndustries[1],
                hasActiveFilters = true
            )
        )
}
