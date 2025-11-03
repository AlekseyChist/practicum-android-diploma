package ru.practicum.android.diploma.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.presentation.favorites.FavoritesViewModel
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.util.debounce

class FavoritesFragment : Fragment() {
    private val viewModel: FavoritesViewModel by viewModel()

    private val onItemClickDebounced by lazy {
        debounce<String>(
            delayMillis = CLICK_DEBOUNCE,
            coroutineScope = lifecycleScope,
            useLastParam = true
        ) { vacancyId ->
            val action = FavoritesFragmentDirections
                .actionFavoritesFragmentToVacancyFragment(vacancyId)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val state by viewModel.state.collectAsStateWithLifecycle()
                AppTheme {
                    FavoritesScreen(
                        state = state,
                        onItemClick = { vacancyId ->
                            onItemClickDebounced(vacancyId)
                        }
                    )
                }
            }
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE = 300L
    }
}
