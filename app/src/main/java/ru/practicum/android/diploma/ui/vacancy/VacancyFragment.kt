package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.practicum.android.diploma.presentation.vacancy.VacancyDetailViewModel
import ru.practicum.android.diploma.ui.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class VacancyFragment : Fragment() {
    private val viewModel: VacancyDetailViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val state by viewModel.state.collectAsStateWithLifecycle()
                AppTheme {
                    VacancyScreen(
                        state = state,
                        onBackClick = { requireActivity().onBackPressedDispatcher.onBackPressed() },
                        onShareClick = {},
                        onFavoriteClick = {}
                    )
                }
            }
        }
    }
}
