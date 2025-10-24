package ru.practicum.android.diploma.ui.filters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.practicum.android.diploma.R

@Composable
fun CountryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.select_country)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CountryScreenPreview() {
    MaterialTheme {
        Surface {
            CountryScreen()
        }
    }
}
