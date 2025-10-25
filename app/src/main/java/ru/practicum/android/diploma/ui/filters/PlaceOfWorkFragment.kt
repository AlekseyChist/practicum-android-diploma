package ru.practicum.android.diploma.ui.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R

class PlaceOfWorkFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
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
