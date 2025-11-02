package ru.practicum.android.diploma.ui.vacancy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.practicum.android.diploma.presentation.vacancy.VacancyDetailViewModel
import ru.practicum.android.diploma.ui.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R

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
                        onShareClick = { url -> shareVacancy(url) },
                        onFavoriteClick = { viewModel.toggleFavorite() },
                        onEmailClick = { email -> sendEmail(email) },
                        onPhoneClick = { phone -> dialPhone(phone) }
                    )
                }
            }
        }
    }

    private fun shareVacancy(url: String) {
        val context = requireContext()
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
        }
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_vacancy)))
    }

    private fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:$email".toUri()
        }
        startActivity(Intent.createChooser(intent, getString(R.string.email_app)))
    }

    private fun dialPhone(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$phone".toUri()
        }
        startActivity(Intent.createChooser(intent, getString(R.string.call_app)))
    }
}
