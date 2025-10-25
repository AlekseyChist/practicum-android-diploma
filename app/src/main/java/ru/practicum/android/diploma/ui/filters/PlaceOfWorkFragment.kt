package ru.practicum.android.diploma.ui.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.AppTheme

class PlaceOfWorkFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    PlaceOfWorkScreen(
                        onNavigateNext = {
                            findNavController().navigate(
                                R.id.action_placeOfWorkFragment_to_countryFragment
                            )
                        },
                        onNavigateNext2 = {
                            findNavController().navigate(
                                R.id.action_placeOfWorkFragment_to_regionFragment
                            )
                        }
                    )
                }
            }
        }
    }
}
