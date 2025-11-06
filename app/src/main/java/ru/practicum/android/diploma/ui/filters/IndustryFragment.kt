package ru.practicum.android.diploma.ui.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.presentation.filters.IndustryState
import ru.practicum.android.diploma.presentation.filters.IndustryViewModel
import ru.practicum.android.diploma.ui.theme.AppTheme

class IndustryFragment : Fragment() {
    private val viewModel: IndustryViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.loadIndustries()
        return ComposeView(requireContext()).apply {
            setContent {
                val state by viewModel.state.collectAsStateWithLifecycle()
                val query = when (val s = state) {
                    is IndustryState.Content -> s.searchQuery
                    else -> ""
                }
                AppTheme {
                    IndustryScreen(
                        state = state,
                        query = "",
                        onBackClick = { requireActivity().onBackPressedDispatcher.onBackPressed() },
                        onQueryChange = { newQuery ->
                            viewModel.onSearchQueryChanged(newQuery)
                        },
                        onClearClick = {
                            viewModel.onSearchQueryChanged("")
                        },
                        onSearchClick = {
                            viewModel.onSearchQueryChanged(query)
                        },
                        onClickIndustry = { industry ->
                            viewModel.selectIndustry(industry)
                        }
                    )
                }
            }
        }
    }
}
