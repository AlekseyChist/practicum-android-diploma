package ru.practicum.android.diploma.ui.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.filters.FiltersSettingsViewModel
import ru.practicum.android.diploma.ui.theme.AppTheme

class FiltersSettingsFragment : Fragment() {

    private val viewModel: FiltersSettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        parentFragmentManager.setFragmentResultListener(
            "selectIndustry",
            viewLifecycleOwner,
        ) { _, bundle ->
            val selectedIndustryId = bundle.getInt("selectedIndustry")
            viewModel.setSelectedIndustry(selectedIndustryId)
        }

        return ComposeView(requireContext()).apply {
            setContent {
                val state by viewModel.state.collectAsStateWithLifecycle()
                AppTheme {
                    FiltersSettingsScreen(
                        state = state,
                        onSalaryChange = { salary ->
                            viewModel.onSalaryChanged(salary)
                        },
                        onClearSalary = { viewModel.clearSalary() },
                        onToggleOnlyWithSalary = { isEnabled ->
                            viewModel.onOnlyWithSalaryChanged(isEnabled)
                        },
                        onIndustryClick = {
                            findNavController().navigate(
                                R.id.action_filtersSettingsFragment_to_industryFragment,
                            )
                        },
                        onClearIndustry = { viewModel.clearIndustry() },
                        onApplyClick = { viewModel.applyFilters() },
                        onResetClick = { viewModel.resetFilters() },
                        onBackClick = { findNavController().popBackStack() },
                    )
                }
            }
        }
    }
}
