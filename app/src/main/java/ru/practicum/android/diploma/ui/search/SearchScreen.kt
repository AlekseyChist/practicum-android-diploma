package ru.practicum.android.diploma.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.practicum.android.diploma.R

@Composable
fun SearchScreen(
    onNavigateNext: () -> Unit,
    onNavigateNext2: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(top = 40.dp),
            text = stringResource(R.string.search_job),
            fontSize = 22.sp,
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { onNavigateNext() }) {
            Text(text = stringResource(R.string.vacancy))
        }

        Button(onClick = { onNavigateNext2() }) {
            Text(text = stringResource(R.string.filters_settings))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    MaterialTheme {
        Surface {
            SearchScreen(onNavigateNext = {}, onNavigateNext2 = {})
        }
    }
}
