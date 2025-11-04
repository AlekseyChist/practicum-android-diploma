package ru.practicum.android.diploma.data

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.api.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareVacancy(url: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 👈 добавлено
        }
        context.startActivity(
            Intent.createChooser(
                shareIntent,
                context.getString(R.string.share_vacancy)
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 👈 добавлено
            }
        )
    }

    override fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:$email".toUri()
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 👈 добавлено
        }
        context.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.email_app)
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 👈 добавлено
            }
        )
    }

    override fun dialPhone(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$phone".toUri()
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 👈 добавлено
        }
        context.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.call_app)
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 👈 добавлено
            }
        )
    }
}
