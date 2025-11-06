package ru.practicum.android.diploma.ui.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
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
    ): View? {
        parentFragmentManager.setFragmentResultListener(
            "selectIndustry",
            viewLifecycleOwner,
        ) { _, bundle ->
            val selectedIndustryId = bundle.getInt("selectedIndustry")
            viewModel.setSelectedIndustry(selectedIndustryId)
        }

        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    var salary by remember { mutableStateOf("") }
                    var onlyWith by remember { mutableStateOf(false) }
                    var industry by remember { mutableStateOf<String?>(null) }

                    FiltersSettingsScreen(
                        salaryText = salary,
                        onlyWithSalary = onlyWith,
                        industryName = industry,
                        onSalaryChange = { salary = it },
                        onClearSalary = { salary = "" },
                        onToggleOnlyWithSalary = { onlyWith = it },
                        onIndustryClick = {
                            findNavController().navigate(
                                R.id.action_filtersSettingsFragment_to_industryFragment,
                            )
                        },
                        onClearIndustry = { industry = null },
                        onApplyClick = { findNavController().popBackStack() },
                        onResetClick = {
                            salary = ""
                            onlyWith = false
                            industry = null
                        },
                        onBackClick = { findNavController().popBackStack() },
                    )
                }
            }
        }
    }
}
