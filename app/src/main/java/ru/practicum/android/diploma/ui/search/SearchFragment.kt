package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.AppTheme

class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    SearchScreenRoute(
                        onFilterClick = {
                            findNavController().navigate(
                                R.id.action_searchFragment_to_filtersSettingsFragment
                            )
                        },
                        onVacancyClick = { vacancyId ->
                            // Передаем  vacancyId в Bundle для VacancyFragment
                            val bundle = Bundle().apply {
                                putString("vacancyId", vacancyId)
                            }
                            findNavController().navigate(
                                R.id.action_searchFragment_to_vacancyFragment,
                                bundle
                            )
                        }
                    )
                }
            }
        }
    }
}
