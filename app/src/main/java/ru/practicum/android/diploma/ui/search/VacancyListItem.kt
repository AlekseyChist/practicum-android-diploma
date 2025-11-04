package ru.practicum.android.diploma.ui.search

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyUi
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens

private const val TAG = "VacancyListItem"

@Composable
fun VacancyListItem(item: VacancyUi, onClick: (String) -> Unit) {
    Log.d(TAG, "Рендерим: ${item.title}, logoUrl=${item.logoUrl}")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clickable { onClick(item.id) }
            .padding(vertical = Dimens.padding_8, horizontal = Dimens.padding_16)
    ) {
        val context = LocalContext.current

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(item.logoUrl)
                .crossfade(true)
                .listener(
                    onStart = {
                        Log.d(TAG, "Начало загрузки: ${item.logoUrl}")
                    },
                    onSuccess = { _, _ ->
                        Log.d(TAG, "Успешно: ${item.logoUrl}")
                    },
                    onError = { _, error ->
                        Log.e(TAG, "Ошибка: ${item.logoUrl}", error.throwable)
                    }
                )
                .build(),
            contentDescription = "company logo",
            placeholder = painterResource(R.drawable.placeholder2),
            error = painterResource(R.drawable.placeholder_32px),
            fallback = painterResource(R.drawable.placeholder_32px),
            modifier = Modifier
                .size(Dimens.size_48)
                .border(
                    width = 1.dp,
                    color = colorResource(R.color.light_gray),
                    shape = RoundedCornerShape(Dimens.corner)
                )
                .padding(0.dp)
                .clip(RoundedCornerShape(Dimens.corner)),
            contentScale = ContentScale.Fit
        )

        Spacer(Modifier.size(Dimens.corner))

        Column {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2
            )

            if (item.company != null) {
                Text(
                    text = item.company,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = item.city,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (item.salary != null) {
                Text(
                    text = item.salary,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VacancyListItemPreview() {
    AppTheme {
        VacancyListItem(
            item = VacancyUi(
                id = "1",
                title = "Android Developer",
                city = "Москва",
                salary = "от 100 000 ₽",
                company = "Яндекс",
                logoUrl = "https://upload.wikimedia.org/wikipedia/commons" +
                    "/thumb/f/f1/Yandex_logo_2021_Russian.svg/1024px-Yandex_logo_2021_Russian.svg.png"
            ),
            onClick = {}
        )
    }
}
