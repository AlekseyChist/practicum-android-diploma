package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R

class SearchFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    SearchScreen(
                        onNavigateNext = {
                            findNavController().navigate(R.id.action_searchFragment_to_vacancyFragment)
                        },
                        onNavigateNext2 = {
                            findNavController().navigate(R.id.action_searchFragment_to_filtersSettingsFragment)
                        }
                    )
                }
            }
        }
    }
}
