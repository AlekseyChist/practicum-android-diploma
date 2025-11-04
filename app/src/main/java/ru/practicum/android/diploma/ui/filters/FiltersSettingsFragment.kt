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
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.AppTheme

class FiltersSettingsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
                                R.id.action_filtersSettingsFragment_to_industryFragment
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
