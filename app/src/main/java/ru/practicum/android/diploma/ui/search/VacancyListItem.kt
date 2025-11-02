package ru.practicum.android.diploma.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
private fun VacancyListItem(item: VacancyUi, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = TOP_BAR_ACTION_END_PADDING)
    ) {
        Text(text = item.title, style = MaterialTheme.typography.titleMedium)
        item.company?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
        Row {
            Text(text = item.city, style = MaterialTheme.typography.bodyMedium)
            item.salary?.let {
                Text(
                    text = "  •  $it",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
