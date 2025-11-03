package ru.practicum.android.diploma.ui.search

import ru.practicum.android.diploma.R
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.practicum.android.diploma.ui.theme.AppTheme

@Composable
fun VacancyListItem(item: VacancyUi, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 20.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.logoUrl)
                .crossfade(true)
                .build(),
            contentDescription = "company logo",
            placeholder = painterResource(R.drawable.placeholder_32px),
            error = painterResource(R.drawable.placeholder_32px),
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.size(12.dp))

        Column {
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
            Text(text = item.city, style = MaterialTheme.typography.bodyMedium)

            item.company?.let {
                Text(text = item.company, style = MaterialTheme.typography.bodyMedium)
            }

            item.salary?.let {
                Text(
                    text = item.salary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(name = "Basic Vacancy", showBackground = true)
@Composable
fun PreviewVacancyListItem() {
    AppTheme {
        VacancyListItem(
            item = VacancyUi(
                id = "1",
                title = "Android Developer",
                city = "Москва",
                salary = "150 000 ₽",
                company = "VK"
            ),
            onClick = {}
        )
    }
}
