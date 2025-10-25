package ru.practicum.android.diploma.ui.filters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
fun CountryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(top = 40.dp),
            text = stringResource(R.string.select_country),
            fontSize = 22.sp,
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
